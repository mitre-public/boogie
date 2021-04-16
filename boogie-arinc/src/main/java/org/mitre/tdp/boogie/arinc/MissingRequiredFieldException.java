package org.mitre.tdp.boogie.arinc;

public class MissingRequiredFieldException extends RuntimeException {

  public MissingRequiredFieldException(String fieldName) {
    super("Missing required field: ".concat(fieldName));
  }
}
