package org.mitre.boogie.xml.fixtures;

import static org.junit.jupiter.api.Assertions.assertAll;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.mitre.boogie.xml.exi.ExiAssertions;
import org.mitre.boogie.xml.exi.ExiCodec;
import org.mitre.boogie.xml.exi.ExiOptions;
import org.mitre.boogie.xml.exi.ExiSchema;
import org.mitre.boogie.xml.v23_4.generated.AeroPublication;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

@Tag("XML")
@EnabledIfSystemProperty(named = "boogie.xml.conversionFixtures", matches = "true",
    disabledReason = "Full-dataset fixture conversion is opt-in")
public class ArincExiIntegration {
  @Test
  @Tag("CIFP")
  @Tag("INTEGRATION")
  void writesCifpXmlAndExi() throws Exception {
    Path xmlFile = writeXml(CifpXmlFixture.load().publication(), "cifp-2101");
    writeAndValidateExiVariants(xmlFile);
  }

  @Test
  @Tag("DAFIF")
  @Tag("INTEGRATION")
  void writesDafifXmlAndExi() throws Exception {
    Path xmlFile = writeXml(DafifXmlFixture.load().publication(), "dafif-2601");
    writeAndValidateExiVariants(xmlFile);
  }

  private static Path writeXml(AeroPublication publication, String fileName) throws Exception {
    Path outputDirectory = Path.of("build", "exi");
    Files.createDirectories(outputDirectory);
    Path xmlFile = outputDirectory.resolve(fileName + ".xml");
    Marshaller marshaller = JAXBContext.newInstance(AeroPublication.class).createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    try (OutputStream output = new BufferedOutputStream(Files.newOutputStream(xmlFile))) {
      marshaller.marshal(publication, output);
    }
    return xmlFile;
  }

  private static void writeAndValidateExiVariants(Path xmlFile) throws Exception {
    // The large JAXB publication can be reclaimed before the compressed decoder runs.
    String fileName = xmlFile.getFileName().toString().replaceFirst("\\.xml$", "");
    ExiOptions schemaLess = ExiOptions.schemaLess().withCompression(true);
    assertAll("EXI variants for " + fileName,
        () -> writeAndValidateExi(xmlFile, xmlFile.resolveSibling(fileName + "-no-schema.exi"), schemaLess),
        () -> writeAndValidateSchemaInformedExi(xmlFile, fileName)
    );
  }

  private static void writeAndValidateSchemaInformedExi(Path xmlFile, String fileName) throws Exception {
    URL officialSchema = ArincExiIntegration.class.getResource("/v23_4/schemas/Records/AeroPublication.xsd");
    ExiSchema schema;
    String schemaSuffix;
    if (officialSchema != null) {
      schema = ExiSchema.compile("urn:boogie:arinc424:23.4", Path.of(officialSchema.toURI()));
      schemaSuffix = "-schema-informed.exi";
    } else {
      // A clean checkout can still test schema-informed EXI using the checked-in JAXB classes.
      // Keep its artifact name and schema ID distinct from the official ARINC schema output.
      schema = ArincFixtureSchema.generated(xmlFile.resolveSibling(fileName + "-schemas"));
      schemaSuffix = "-jaxb-schema-informed.exi";
    }
    ExiOptions schemaInformed = ExiOptions.schemaInformed(schema).withCompression(true);
    writeAndValidateExi(xmlFile, xmlFile.resolveSibling(fileName + schemaSuffix), schemaInformed);
  }

  private static void writeAndValidateExi(Path xmlFile, Path exiFile, ExiOptions options) throws Exception {
    try (InputStream input = Files.newInputStream(xmlFile);
         OutputStream output = new BufferedOutputStream(Files.newOutputStream(exiFile))) {
      new ExiCodec(options).encode(input, output);
    }
    assertAll(exiFile.toString(),
        () -> ExiAssertions.assertCookie(exiFile),
        () -> {
          ExiCodec decoder = new ExiCodec(ExiOptions.schemaLess(), options.schema().stream().toList());
          try (InputStream source = Files.newInputStream(xmlFile)) {
            ExiAssertions.assertSameStructure(source, exiFile, decoder);
          }
        }
    );
  }
}
