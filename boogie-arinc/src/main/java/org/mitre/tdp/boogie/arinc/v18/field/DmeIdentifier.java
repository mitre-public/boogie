package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The identification of a DME facility, a TACAN facility or the DME (or TACAN) component of a VORDME or VORTAC facility.
 */
public final class DmeIdentifier implements FreeFormString, FilterTrimEmptyInput<String> {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.38";
  }
}
