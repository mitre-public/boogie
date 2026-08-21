package org.mitre.tdp.boogie.arinc.assemble;

import org.mitre.tdp.boogie.arinc.model.ArincRestrictiveAirspaceLeg;

import java.util.Optional;
import java.util.function.Function;

public final class RestrictiveAirspaceName implements Function<ArincRestrictiveAirspaceLeg, String> {
    public static final RestrictiveAirspaceName INSTANCE = new RestrictiveAirspaceName();
    private  RestrictiveAirspaceName() {

    }
    @Override
    public String apply(ArincRestrictiveAirspaceLeg representative) {
        String ident = Optional.of(representative.restrictiveAirspaceDesignation()).filter(s -> !s.endsWith("*"))
                .or(representative::restrictiveAirspaceName)
                .orElseThrow(() -> new IllegalArgumentException("No valid airspace designation found: " + representative));
        return ident.concat("-")
                .concat(representative.restrictiveType())
                .concat("-")
                .concat(representative.icaoRegion())
                .concat("-")
                .concat(representative.multipleCode().orElse(""));
    }
}
