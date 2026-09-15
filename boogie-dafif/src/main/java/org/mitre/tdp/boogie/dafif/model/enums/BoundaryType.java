package org.mitre.tdp.boogie.dafif.model.enums;

/**
 * Boundary types defined by DAFIF data dictionary field 51.
 */
public enum BoundaryType {

  /**
   * Advisory area (ADA) or upper advisory area (UDA).
   */
  ADVISORY_AREA(1),
  AIR_DEFENSE_IDENTIFICATION_ZONE(2),
  AIR_ROUTE_TRAFFIC_CONTROL_CENTER(3),
  AREA_CONTROL_CENTER(4),
  BUFFER_ZONE(5),

  /**
   * Control area (CTA/UTA) or special rules area (SRA, United Kingdom only).
   */
  CONTROL_AREA(6),

  /**
   * Control zone, including United Kingdom special rules and military aerodrome traffic zones.
   */
  CONTROL_ZONE(7),

  /**
   * Flight information region.
   */
  FIR(8),
  OCEAN_CONTROL_AREA(9),
  RADAR_AREA(10),

  /**
   * Terminal control area (TCA or MTCA).
   */
  TERMINAL_CONTROL_AREA(11),

  /**
   * Upper flight information region.
   */
  UIR(12),
  MODE_C_DEFINED_AREA(13),
  OTHER(14),
  FUNCTIONAL_AIRSPACE_BLOCK(15);

  private final int code;

  BoundaryType(int code) {
    this.code = code;
  }

  /**
   * Returns the numeric DAFIF code, without the file representation's leading zero.
   */
  public int code() {
    return code;
  }
}
