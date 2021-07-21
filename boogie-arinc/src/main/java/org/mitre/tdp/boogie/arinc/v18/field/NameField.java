package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * This field will be used to further define the record by name.
 */
public final class NameField extends TrimmableString {

  @Override
  public int fieldLength() {
    return 30;
  }

  @Override
  public String fieldCode() {
    return "5.71";
  }
}
