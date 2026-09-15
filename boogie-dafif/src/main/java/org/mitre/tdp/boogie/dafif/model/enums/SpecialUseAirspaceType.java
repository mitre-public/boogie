package org.mitre.tdp.boogie.dafif.model.enums;

/**
 * Special use airspace types defined by DAFIF data dictionary field 293.
 */
public enum SpecialUseAirspaceType {
  ALERT("A"),
  DANGER("D"),
  MILITARY_OPERATIONS("M"),
  PROHIBITED("P"),
  RESTRICTED("R"),
  TEMPORARY_RESERVED("T"),
  WARNING("W");

  private final String code;

  SpecialUseAirspaceType(String code) {
    this.code = code;
  }

  /**
   * Returns the DAFIF code.
   */
  public String code() {
    return code;
  }
}
