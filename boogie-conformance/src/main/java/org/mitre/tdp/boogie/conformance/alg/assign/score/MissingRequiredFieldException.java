package org.mitre.tdp.boogie.conformance.alg.assign.score;

import java.util.function.Supplier;

public class MissingRequiredFieldException extends RuntimeException {

  MissingRequiredFieldException(String field) {
    super("Missing required field: " + field);
  }

  public static Supplier<MissingRequiredFieldException> supplier(String field) {
    return () -> new MissingRequiredFieldException(field);
  }
}
