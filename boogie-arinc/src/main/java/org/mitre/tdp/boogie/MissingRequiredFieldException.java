package org.mitre.tdp.boogie;

public class MissingRequiredFieldException extends RuntimeException {
  public MissingRequiredFieldException(String fieldName) {
    super("Missing required field: " + fieldName);
  }
}
