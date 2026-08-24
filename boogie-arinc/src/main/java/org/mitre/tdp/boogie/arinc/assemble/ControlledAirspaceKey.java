package org.mitre.tdp.boogie.arinc.assemble;

import java.util.function.Function;

import org.mitre.tdp.boogie.arinc.model.ArincControlledAirspaceLeg;

/**
 * Structural key for records describing one controlled airspace boundary and altitude layer. The long airspace name is
 * intentionally excluded because it may be populated only on the first record.
 */
public final class ControlledAirspaceKey implements Function<ArincControlledAirspaceLeg, String> {
  public static final ControlledAirspaceKey INSTANCE = new ControlledAirspaceKey();

  private ControlledAirspaceKey() {
  }

  @Override
  public String apply(ArincControlledAirspaceLeg representative) {
    return representative.airspaceCenter()
        .concat("-")
        .concat(representative.airspaceType().name())
        .concat("-")
        .concat(representative.icaoRegion())
        .concat("-")
        .concat(representative.multipleCode().orElse(""))
        .concat("-")
        .concat(representative.airspaceClassification().orElse(""));
  }
}
