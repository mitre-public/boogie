package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import java.util.function.Supplier;

public class MissingRequiredFieldException extends RuntimeException {

  MissingRequiredFieldException(String field) {
    super("Missing required field: " + field);
  }

  static Supplier<MissingRequiredFieldException> supplier(String field) {
    return () -> new MissingRequiredFieldException(field);
  }
}
