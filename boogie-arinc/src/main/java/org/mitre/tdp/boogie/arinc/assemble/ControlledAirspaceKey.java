package org.mitre.tdp.boogie.arinc.assemble;

import org.mitre.tdp.boogie.arinc.model.ArincControlledAirspaceLeg;
import org.mitre.tdp.boogie.arinc.v18.field.AirspaceType;

/**
 * Structural key for records describing one controlled airspace boundary and altitude layer. The long airspace name is
 * intentionally excluded because it may be populated only on the first record.
 */
public record ControlledAirspaceKey(
    String airspaceCenter,
    AirspaceType airspaceType,
    String icaoRegion,
    String multipleCode,
    String airspaceClassification
) {

  public static ControlledAirspaceKey from(ArincControlledAirspaceLeg representative) {
    return new ControlledAirspaceKey(
        representative.airspaceCenter(),
        representative.airspaceType(),
        representative.icaoRegion(),
        representative.multipleCode().orElse(""),
        representative.airspaceClassification().orElse("")
    );
  }
}
