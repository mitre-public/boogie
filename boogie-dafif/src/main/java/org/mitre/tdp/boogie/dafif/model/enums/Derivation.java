package org.mitre.tdp.boogie.dafif.model.enums;

/**
 * Arc segment derivation methods defined by DAFIF data dictionary field 91.
 */
public enum Derivation {
  DISTANCE_AND_BEARING("B"),
  END_COORDINATES("E"),
  PLOTTED_COORDINATES("R");

  private final String code;

  Derivation(String code) {
    this.code = code;
  }

  /**
   * Returns the DAFIF code.
   */
  public String code() {
    return code;
  }
}
