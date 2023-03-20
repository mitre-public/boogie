package org.mitre.tdp.boogie.convert.routes;

import java.util.function.Function;

import org.mitre.tdp.boogie.contract.routes.ImmutableRouteSummary;
import org.mitre.tdp.boogie.contract.routes.RouteSummary;

public class ConvertRouteSummary implements Function<org.mitre.tdp.boogie.alg.RouteSummary, RouteSummary> {
  public static final ConvertRouteSummary INSTANCE = new ConvertRouteSummary();

  public ConvertRouteSummary() {

  }

  @Override
  public RouteSummary apply(org.mitre.tdp.boogie.alg.RouteSummary routeSummary) {
    return ImmutableRouteSummary.builder()
        .route(routeSummary.route())
        .arrivalAirport(routeSummary.arrivalAirport())
        .arrivalRunway(routeSummary.arrivalRunway())
        .arrivalFix(routeSummary.arrivalFix())
        .departureAirport(routeSummary.departureAirport())
        .departureRunway(routeSummary.departureRunway())
        .departureFix(routeSummary.departureFix())
        .star(routeSummary.star())
        .starEntryFix(routeSummary.starEntryFix())
        .requiredStarEquipage(routeSummary.requiredStarEquipage())
        .sid(routeSummary.sid())
        .sidExitFix(routeSummary.sidExitFix())
        .requiredSidEquipage(routeSummary.requiredSidEquipage())
        .approach(routeSummary.approach())
        .approachEntryFix(routeSummary.approachEntryFix())
        .requiredApproachEquipage(routeSummary.requiredApproachEquipage())
        .build();
  }
}
