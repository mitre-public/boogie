package org.mitre.tdp.boogie.contract.routes;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableAirport.class)
@JsonDeserialize(as = ImmutableAirport.class)
public interface Airport {
  /**
   * The (typically) ICAO identifier of the airport. This interface doesn't <i>require</i> this to be the case though.
   * <br>
   * Generally speaking it is up to downstream consumers to standardize these identifiers to FAA, ICAO, etc. whatever standard
   * they prefer. Some downstream modules are sensitive to the values in this field - e.g. the RouteExpander cares whether the
   * identifier here is the same that appears in the route string.
   */
  String airportIdentifier();

  /**
   * The geospatial region in which the airport identifier should be unique.
   */
  String airportRegion();

  /**
   * Collection of all {@link org.mitre.tdp.boogie.Runway}s available at the airport.
   */
  List<Runway> runways();

  Fix fix();
}
