package org.mitre.boogie.xml.exi;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.parser.XMLInputSource;

import com.siemens.ct.exi.core.exceptions.EXIException;
import com.siemens.ct.exi.core.grammars.Grammars;
import com.siemens.ct.exi.grammars.GrammarFactory;

/** A compiled XML Schema and the application-defined ID used to identify it in EXI headers. */
public final class ExiSchema {

  private final String schemaId;
  private final Grammars grammars;

  private ExiSchema(String schemaId, Grammars grammars) {
    this.schemaId = schemaId;
    this.grammars = grammars;
  }

  /**
   * Compile a local XSD once for reuse across codecs and operations.
   *
   * <p>Relative imports and includes are resolved against the containing XSD. All referenced
   * documents must be local files; network retrieval is disabled. Schemas should come from a
   * trusted source. The nonblank ID must identify the same schema set at both endpoints and
   * should change when that set changes. It is an identifier, not a URL to fetch when decoding.
   */
  public static ExiSchema compile(String schemaId, Path rootXsd) throws IOException {
    requireNonNull(schemaId, "schemaId");
    requireNonNull(rootXsd, "rootXsd");
    if (schemaId.isBlank()) {
      throw new IllegalArgumentException("An EXI schema ID must not be blank");
    }
    Path schemaPath = rootXsd.toAbsolutePath().normalize();
    if (!Files.isRegularFile(schemaPath)) {
      throw new IOException("XML Schema is not a regular file: " + schemaPath);
    }
    try {
      Grammars grammars = GrammarFactory.newInstance()
          .createGrammars(schemaPath.toUri().toString(), ExiSchema::resolveLocalFile);
      grammars.setSchemaId(schemaId);
      return new ExiSchema(schemaId, grammars);
    } catch (EXIException | RuntimeException e) {
      throw new IOException("Could not compile EXI schema " + schemaId + " from " + schemaPath, e);
    }
  }

  public String schemaId() {
    return schemaId;
  }

  Grammars grammars() {
    return grammars;
  }

  private static XMLInputSource resolveLocalFile(XMLResourceIdentifier resource) throws IOException {
    String systemId = resource.getExpandedSystemId();
    if (systemId == null) {
      systemId = resource.getLiteralSystemId();
    }
    if (systemId == null) {
      throw new IOException("XML Schema reference has no local file location");
    }
    try {
      URI location = URI.create(systemId);
      if (!location.isAbsolute() && resource.getBaseSystemId() != null) {
        location = URI.create(resource.getBaseSystemId()).resolve(location);
      }
      if (!"file".equalsIgnoreCase(location.getScheme()) || location.getAuthority() != null) {
        throw new IOException("Only local XML Schema references are supported: " + location);
      }
      Path file = Path.of(location);
      if (!Files.isRegularFile(file)) {
        throw new IOException("XML Schema reference is not a regular file: " + file);
      }
      return new XMLInputSource(resource.getPublicId(), location.toString(), resource.getBaseSystemId());
    } catch (IllegalArgumentException e) {
      throw new IOException("Invalid XML Schema reference: " + systemId, e);
    }
  }
}
