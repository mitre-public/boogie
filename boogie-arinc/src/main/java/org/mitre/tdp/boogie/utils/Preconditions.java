package org.mitre.tdp.boogie.utils;

import java.util.concurrent.Callable;

import org.mitre.tdp.boogie.FieldSpec;
import org.mitre.tdp.boogie.FieldSpecParseException;

public class Preconditions {

  /**
   * If valid is false then throws a {@link FieldSpecParseException} for the given spec and field.
   */
  public static void checkSpec(FieldSpec<?> spec, String field, boolean valid) {
    if (!valid) {
      throw new FieldSpecParseException(spec, field);
    }
  }

  /** If the callable throws an exception other than a {@link FieldSpecParseException} */
  public static void checkSpec(FieldSpec<?> spec, String field, Callable<?> callable) {
    try {
      callable.call();
    } catch (Exception e) {
      if (e instanceof FieldSpecParseException) {
        throw ((FieldSpecParseException) e);
      } else {
        throw new FieldSpecParseException(spec, field);
      }
    }
  }
}
