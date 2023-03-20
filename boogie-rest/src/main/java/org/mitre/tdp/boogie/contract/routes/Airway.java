package org.mitre.tdp.boogie.contract.routes;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableAirway.class)
@JsonDeserialize(as = ImmutableAirway.class)
public interface Airway {
  /**
   * The string identifier of the enroute airway.
   * <br>
   * Generally speaking this is up to 5 characters in length but may be fewer.
   * <br>
   * e.g. Y141, J153, J8, V12, etc.
   */
  String airwayIdentifier();

  /**
   * Generally speaking airways are defined regionally
   */
  String airwayRegion();

  /**
   * The ordered sequence of {@link org.mitre.tdp.boogie.Leg}s contained within the airway. These legs are generally flyable bi-directionally unless
   * explicitly restricted otherwise.
   */
  List<Leg> legs();
}
