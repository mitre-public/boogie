package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * When it is not possible to store all the information needed on a record within the 132 columns of the record itself,
 * the so-called Primary Record; one or more continuation records may be used.
 *
 * e.g. [1-9][A-Z]
 */
public class ContinuationRecordNumber implements FreeFormString {
  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.16";
  }
}
