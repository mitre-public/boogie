package org.mitre.tdp.boogie.dafif.v81.converter;

import java.util.function.Function;

import org.mitre.tdp.boogie.dafif.model.enums.SpecialUseAirspaceType;

/**
 * Converts a parsed DAFIF 8.1 special use airspace type code into its model value.
 */
public final class SpecialUseAirspaceTypeConverter implements Function<String, SpecialUseAirspaceType> {

  public static final SpecialUseAirspaceTypeConverter INSTANCE = new SpecialUseAirspaceTypeConverter();

  private SpecialUseAirspaceTypeConverter() {
  }

  /**
   * @throws IllegalArgumentException if the code is null or unsupported
   */
  @Override
  public SpecialUseAirspaceType apply(String code) {
    if (code == null) {
      throw new IllegalArgumentException("DAFIF special use airspace type code cannot be null.");
    }
    return switch (code) {
      case "A" -> SpecialUseAirspaceType.ALERT;
      case "D" -> SpecialUseAirspaceType.DANGER;
      case "M" -> SpecialUseAirspaceType.MILITARY_OPERATIONS;
      case "P" -> SpecialUseAirspaceType.PROHIBITED;
      case "R" -> SpecialUseAirspaceType.RESTRICTED;
      case "T" -> SpecialUseAirspaceType.TEMPORARY_RESERVED;
      case "W" -> SpecialUseAirspaceType.WARNING;
      default -> throw new IllegalArgumentException("Unsupported DAFIF special use airspace type code: " + code);
    };
  }
}
