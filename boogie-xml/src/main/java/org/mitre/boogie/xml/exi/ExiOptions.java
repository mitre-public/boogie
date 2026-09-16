package org.mitre.boogie.xml.exi;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

/**
 * Immutable EXI encoding settings. Decoding uses the settings carried in the EXI header;
 * these settings also supply the defaults for streams without an options document.
 *
 * <p>The default is bit-packed EXI with standard fidelity: comments, processing instructions,
 * formatting and prefix spellings need not survive a round trip, and typed values may be
 * normalized. Preserving lexical values also preserves namespace prefixes in nonstrict mode,
 * so lexical QName values retain their bindings. Strict EXI forbids prefix preservation;
 * combining strict mode and lexical preservation therefore cannot encode {@code xsi:type}.
 * These settings do not make EXI a byte-for-byte XML archive.
 */
public final class ExiOptions {

  private final ExiSchema schema;
  private final boolean compression;
  private final boolean strict;
  private final boolean preserveLexicalValues;

  private ExiOptions(ExiSchema schema, boolean compression, boolean strict, boolean preserveLexicalValues) {
    if (strict && schema == null) {
      throw new IllegalArgumentException("Strict EXI requires a schema");
    }
    this.schema = schema;
    this.compression = compression;
    this.strict = strict;
    this.preserveLexicalValues = preserveLexicalValues;
  }

  public static ExiOptions schemaLess() {
    return new ExiOptions(null, false, false, false);
  }

  public static ExiOptions schemaInformed(ExiSchema schema) {
    return new ExiOptions(requireNonNull(schema, "schema"), false, false, false);
  }

  public ExiOptions withCompression(boolean compression) {
    return new ExiOptions(schema, compression, strict, preserveLexicalValues);
  }

  /** Select schema-strict EXI; this is unavailable for schema-less encoding. */
  public ExiOptions withStrict(boolean strict) {
    return new ExiOptions(schema, compression, strict, preserveLexicalValues);
  }

  public ExiOptions withPreserveLexicalValues(boolean preserveLexicalValues) {
    return new ExiOptions(schema, compression, strict, preserveLexicalValues);
  }

  public Optional<ExiSchema> schema() {
    return Optional.ofNullable(schema);
  }

  public boolean compression() {
    return compression;
  }

  public boolean strict() {
    return strict;
  }

  public boolean preserveLexicalValues() {
    return preserveLexicalValues;
  }
}
