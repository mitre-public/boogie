package org.mitre.tdp.boogie;

public class FieldSpecParseException extends RuntimeException {

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
    return "Spec parsing error for spec: " + specClz.getSimpleName() + " with value: " + value;
  }
}
