package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The “Runway Length” field defines the total length of the runway surface declared suitable and available for ground
 * operations of aircraft for the runway identified in the records’ Runway Identifier field.
 */
public final class RunwayLength extends ArincInteger {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.57";
  }
}
