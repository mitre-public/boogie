package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The distance from the extremity of a runway to a threshold not located at that extremity of that runway.
 * <br>
 * Threshold displacement distances derived from official government sources are entered into this field in feet.
 */
public final class ThresholdDisplacementDistance extends ArincInteger {

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.69";
  }
}
