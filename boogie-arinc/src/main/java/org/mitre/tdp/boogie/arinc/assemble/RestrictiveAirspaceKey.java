package org.mitre.tdp.boogie.arinc.assemble;

import org.mitre.tdp.boogie.arinc.model.ArincRestrictiveAirspaceLeg;

import java.util.function.Function;

/**
 * Structural key for records describing one restrictive airspace boundary.
 *
 * <p>The raw designation is intentionally retained for names ending in {@code *}, while the multiple code keeps
 * independently defined lateral or vertical subdivisions separate.</p>
 */
public final class RestrictiveAirspaceKey implements Function<ArincRestrictiveAirspaceLeg, String> {
  public static final RestrictiveAirspaceKey INSTANCE = new RestrictiveAirspaceKey();
  private RestrictiveAirspaceKey() {
  }
  @Override
  public String apply(ArincRestrictiveAirspaceLeg representative) {
    String ident = representative.restrictiveAirspaceDesignation();
    return ident.concat("-")
        .concat(representative.restrictiveType())
        .concat("-")
        .concat(representative.icaoRegion())
        .concat("-")
        .concat(representative.multipleCode().orElse(""));
  }
}
