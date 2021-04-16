package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * This field will be used to further define the record by name.
 */
public class NameField implements FreeFormString, FilterTrimEmptyInput<String> {
  @Override
  public int fieldLength() {
    return 30;
  }

  @Override
  public String fieldCode() {
    return "5.71";
  }
}
