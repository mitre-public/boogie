package org.mitre.tdp.boogie.arinc.assemble;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;

/**
 * This takes the section and subsection and makes it into an Airport-Style ArincRouteType (i.e., heliport records look liek airprt)
 */
final class RouteTypeExtractor implements Function<ArincProcedureLeg, ArincRouteType> {
  static final RouteTypeExtractor INSTANCE = new RouteTypeExtractor();
  private RouteTypeExtractor() {}
  @Override
  public ArincRouteType apply(ArincProcedureLeg arincProcedureLeg) {
    requireNonNull(arincProcedureLeg);
    String sectionCode = arincProcedureLeg.sectionCode().name();
    checkArgument(sectionCode.equals("P") || sectionCode.equals("H"), "Illegal section code for procedure leg: ".concat(arincProcedureLeg.toString()));

    String candidate = "P" //this is to simplify the logic to determine the procedure is a certain route type H/P its the same but we have all the logic for P.
        .concat(arincProcedureLeg.subSectionCode().orElseThrow(IllegalStateException::new))
        .concat("_")
        .concat(arincProcedureLeg.routeType());

    return Optional.of(candidate).filter(ArincRouteType.VALID::contains).map(ArincRouteType::valueOf).orElse(null);
  }
}
