package org.mitre.tdp.boogie.contract.routes;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableLatLong.class)
@JsonDeserialize(as = ImmutableLatLong.class)
public interface LatLong {
  double latitude();

  double longitude();

  default org.mitre.caasd.commons.LatLong toCommons() {
    return org.mitre.caasd.commons.LatLong.of(latitude(), longitude());
  }
}
