package org.mitre.tdp.boogie.arinc;

/**
 * Specific subclass of a {@link RuntimeException} thrown when an exception is encountered extracting structured content from a
 * portion of an ARINC record string.
 */
public final class FieldSpecParseException extends RuntimeException {

  public FieldSpecParseException(FieldSpec<?> spec, String value) {
    this(spec.getClass(), value);
  }

  public FieldSpecParseException(Class<? extends FieldSpec> specClz, String value) {
    super(message(specClz, value));
  }

  public FieldSpecParseException(Class<? extends FieldSpec> specClz, String value, Exception e) {
    super(message(specClz, value), e);
  }

  private static String message(Class<? extends FieldSpec> specClz, String value) {
    return "Spec parsing error for spec: ".concat(specClz.getSimpleName()).concat(" with value: ").concat(value);
  }
}
