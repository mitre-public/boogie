package org.mitre.tdp.boogie.dafif.model.enums;

/**
 * Boundary segment shapes defined by DAFIF data dictionary field 289.
 */
public enum Shape {
  POINT("A"),
  GREAT_CIRCLE("B"),
  CIRCLE("C"),

  /**
   * An irregular boundary approximated by plotted coordinates.
   */
  GENERALIZED("G"),
  RHUMB_LINE("H"),
  COUNTERCLOCKWISE_ARC("L"),
  CLOCKWISE_ARC("R");

  private final String code;

  Shape(String code) {
    this.code = code;
  }

  /**
   * Returns the DAFIF code.
   */
  public String code() {
    return code;
  }
}
