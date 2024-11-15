package org.mitre.tdp.boogie.arinc.utils;

import java.util.concurrent.Callable;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;

/**
 * Utilities similar to the {@link com.google.common.base.Preconditions} from Guava - but for throwing a few ARINC-specific
 * exception types.
 */
public final class Preconditions {

  private Preconditions() {
    throw new IllegalStateException("Cannot instantiate static utility class.");
  }

  /**
   * If valid is false then throws a {@link FieldSpecParseException} for the given spec and field.
   */
  public static void checkSpec(FieldSpec<?> spec, String field, boolean valid) {
    if (!valid) {
      throw new FieldSpecParseException(spec, field);
    }
  }

  /**
   * If the callable throws an exception other than a {@link FieldSpecParseException} throw the .
   */
  public static void checkSpec(FieldSpec<?> spec, String field, Callable<?> callable) {
    try {
      callable.call();
    } catch (FieldSpecParseException e) {
      throw e;
    } catch (Exception e) {
      throw new FieldSpecParseException(spec, field, e);
    }
  }
}
