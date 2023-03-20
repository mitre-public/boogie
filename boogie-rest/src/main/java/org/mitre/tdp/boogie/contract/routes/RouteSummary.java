package org.mitre.tdp.boogie.contract.routes;

import java.util.Optional;

import org.immutables.value.Value;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableRouteSummary.class)
@JsonDeserialize(as = ImmutableRouteSummary.class)
public interface RouteSummary {
  String route();

  String arrivalAirport();

  Optional<String> arrivalRunway();

  Optional<String> arrivalFix();

  String departureAirport();

  Optional<String> departureRunway();

  Optional<String> departureFix();

  Optional<String> star();

  Optional<String> starEntryFix();

  Optional<RequiredNavigationEquipage> requiredStarEquipage();

  Optional<String> sid();

  Optional<String> sidExitFix();

  Optional<RequiredNavigationEquipage> requiredSidEquipage();

  Optional<String> approach();

  Optional<String> approachEntryFix();

  Optional<RequiredNavigationEquipage> requiredApproachEquipage();
}
