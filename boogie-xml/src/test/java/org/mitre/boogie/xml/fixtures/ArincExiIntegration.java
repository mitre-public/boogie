package org.mitre.boogie.xml.fixtures;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.exi.ExiAssertions;
import org.mitre.boogie.xml.exi.ExiCodec;
import org.mitre.boogie.xml.exi.ExiOptions;
import org.mitre.boogie.xml.exi.ExiSchema;
import org.mitre.boogie.xml.v23_4.generated.AeroPublication;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

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
    writeAndValidateExi(xmlFile, xmlFile.resolveSibling(fileName + "-no-schema.exi"), schemaLess);

    Path rootXsd = Path.of(ArincExiIntegration.class.getResource("/v23_4/schemas/Records/AeroPublication.xsd").toURI());
    ExiSchema schema = ExiSchema.compile("urn:boogie:arinc424:23.4", rootXsd);
    ExiOptions schemaInformed = ExiOptions.schemaInformed(schema).withCompression(true);
    writeAndValidateExi(xmlFile, xmlFile.resolveSibling(fileName + "-schema-informed.exi"), schemaInformed);
  }

  private static void writeAndValidateExi(Path xmlFile, Path exiFile, ExiOptions options) throws Exception {
    try (InputStream input = Files.newInputStream(xmlFile);
         OutputStream output = new BufferedOutputStream(Files.newOutputStream(exiFile))) {
      new ExiCodec(options).encode(input, output);
    }
    ExiAssertions.assertCookie(exiFile);
    ExiCodec decoder = new ExiCodec(ExiOptions.schemaLess(), options.schema().stream().toList());
    try (InputStream source = Files.newInputStream(xmlFile)) {
      ExiAssertions.assertSameStructure(source, exiFile, decoder);
    }
  }
}
