package org.mitre.boogie.xml.fixtures;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.SchemaOutputResolver;
import org.mitre.boogie.xml.exi.ExiSchema;
import org.mitre.boogie.xml.v23_4.generated.AeroPublication;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

/** Schemas for EXI tests that can run without the external official ARINC XSDs. */
public final class ArincFixtureSchema {

  private ArincFixtureSchema() {}

  /** Generate a JAXB test schema and retain its files alongside the EXI artifact for decoding. */
  public static ExiSchema generated(Path outputDirectory) throws Exception {
    Path directory = Files.createDirectories(outputDirectory);
    Map<String, Path> schemas = new HashMap<>();
    JAXBContext.newInstance(AeroPublication.class).generateSchema(new SchemaOutputResolver() {
      @Override
      public Result createOutput(String namespaceUri, String suggestedFileName) {
        Path file = directory.resolve(suggestedFileName);
        schemas.put(namespaceUri, file);
        return new StreamResult(file.toFile());
      }
    });
    Path rootXsd = Objects.requireNonNull(schemas.get(""), "Missing generated publication schema");
    // Generated JAXB schemas must not use the ID of the official ARINC schema set.
    return ExiSchema.compile("urn:boogie:test:arinc424:jaxb:23.4", rootXsd);
  }
}
