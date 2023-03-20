package org.mitre.tdp.boogie.contract.routes;

import java.util.List;
import java.util.Optional;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableExpandedRoute.class)
@JsonDeserialize(as = ImmutableExpandedRoute.class)
public interface ExpandedRoute {
  Optional<RouteSummary> routeSummary();

  List<ExpandedRouteLeg> legs();
}
