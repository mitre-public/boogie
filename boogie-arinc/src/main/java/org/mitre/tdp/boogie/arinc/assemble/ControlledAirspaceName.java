package org.mitre.tdp.boogie.arinc.assemble;

import org.mitre.tdp.boogie.arinc.model.ArincControlledAirspaceLeg;

import java.util.function.Function;

/**
 * Fully qualified name for a controlled airspace and classification layer.
 */
public final class ControlledAirspaceName implements Function<ArincControlledAirspaceLeg, String> {
    public static final ControlledAirspaceName INSTANCE = new ControlledAirspaceName();
    private ControlledAirspaceName() {
    }
    @Override
    public String apply(ArincControlledAirspaceLeg representative) {
        return representative.airspaceCenter()
                .concat("-")
                .concat(representative.airspaceType().name())
                .concat("-")
                .concat(representative.icaoRegion())
                .concat("-")
                .concat(representative.controlledAirspaceName().orElse(""))
                .concat("-")
                .concat(representative.multipleCode().orElse(""))
                .concat("-")
                .concat(representative.airspaceClassification().orElse(""));
    }
}
