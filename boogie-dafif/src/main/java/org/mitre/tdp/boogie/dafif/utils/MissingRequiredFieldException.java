package org.mitre.tdp.boogie.dafif.utils;

public final class MissingRequiredFieldException extends RuntimeException {

  public MissingRequiredFieldException(String fieldName) {
    super("Missing required field: ".concat(fieldName));
  }

}
