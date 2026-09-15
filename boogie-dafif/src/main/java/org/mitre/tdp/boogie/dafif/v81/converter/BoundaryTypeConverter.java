package org.mitre.tdp.boogie.dafif.v81.converter;

import java.util.function.Function;

import org.mitre.tdp.boogie.dafif.model.enums.BoundaryType;

/**
 * Converts a parsed DAFIF 8.1 boundary type code into its model value.
 */
public final class BoundaryTypeConverter implements Function<Integer, BoundaryType> {

  public static final BoundaryTypeConverter INSTANCE = new BoundaryTypeConverter();

  private BoundaryTypeConverter() {
  }

  /**
   * @throws IllegalArgumentException if the code is null or unsupported
   */
  @Override
  public BoundaryType apply(Integer code) {
    if (code == null) {
      throw new IllegalArgumentException("DAFIF boundary type code cannot be null.");
    }
    return switch (code) {
      case 1 -> BoundaryType.ADVISORY_AREA;
      case 2 -> BoundaryType.AIR_DEFENSE_IDENTIFICATION_ZONE;
      case 3 -> BoundaryType.AIR_ROUTE_TRAFFIC_CONTROL_CENTER;
      case 4 -> BoundaryType.AREA_CONTROL_CENTER;
      case 5 -> BoundaryType.BUFFER_ZONE;
      case 6 -> BoundaryType.CONTROL_AREA;
      case 7 -> BoundaryType.CONTROL_ZONE;
      case 8 -> BoundaryType.FIR;
      case 9 -> BoundaryType.OCEAN_CONTROL_AREA;
      case 10 -> BoundaryType.RADAR_AREA;
      case 11 -> BoundaryType.TERMINAL_CONTROL_AREA;
      case 12 -> BoundaryType.UIR;
      case 13 -> BoundaryType.MODE_C_DEFINED_AREA;
      case 14 -> BoundaryType.OTHER;
      case 15 -> BoundaryType.FUNCTIONAL_AIRSPACE_BLOCK;
      default -> throw new IllegalArgumentException("Unsupported DAFIF boundary type code: " + code);
    };
  }
}
