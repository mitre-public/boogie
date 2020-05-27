package org.mitre.tdp.boogie;

public class UnknownRecordException extends RuntimeException {

  public UnknownRecordException(ArincSpec spec, String rawRecord) {
    super("Unparseable record: " + rawRecord + " with spec: " + spec);
  }
}
