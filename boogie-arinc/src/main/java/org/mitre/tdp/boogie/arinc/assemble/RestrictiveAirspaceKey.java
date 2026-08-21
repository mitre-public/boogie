package org.mitre.tdp.boogie.arinc.assemble;

import org.mitre.tdp.boogie.arinc.model.ArincRestrictiveAirspaceLeg;

import java.util.function.Function;

/**
 * This is like the {@link RestrictiveAirspaceName} but no * processing. The key is still unique but not helpful as is.
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
        .concat("-");
  }
}
