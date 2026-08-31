package org.mitre.tdp.boogie.arinc.assemble;

import org.mitre.tdp.boogie.arinc.model.ArincRestrictiveAirspaceLeg;

/**
 * Structural key for records describing one restrictive airspace boundary.
 *
 * <p>The raw designation is intentionally retained for names ending in {@code *}, while the multiple code keeps
 * independently defined lateral or vertical subdivisions separate.</p>
 */
public record RestrictiveAirspaceKey(
    String restrictiveAirspaceDesignation,
    String restrictiveType,
    String icaoRegion,
    String multipleCode
) {

  public static RestrictiveAirspaceKey from(ArincRestrictiveAirspaceLeg representative) {
    return new RestrictiveAirspaceKey(
        representative.restrictiveAirspaceDesignation(),
        representative.restrictiveType(),
        representative.icaoRegion(),
        representative.multipleCode().orElse("")
    );
  }
}
