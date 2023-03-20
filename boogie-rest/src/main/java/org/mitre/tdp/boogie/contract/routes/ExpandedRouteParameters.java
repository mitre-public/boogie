package org.mitre.tdp.boogie.contract.routes;

import java.util.Optional;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableExpandedRouteParameters.class)
@JsonDeserialize(as = ImmutableExpandedRouteParameters.class)
public interface ExpandedRouteParameters {
  String route();

  Optional<String> departureRunway();

  Optional<String> arrivalRunway();

  Optional<String> approachEquipage();
}
