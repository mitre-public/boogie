package org.mitre.tdp.boogie;

import java.util.List;
import java.util.function.Function;

import org.mitre.tdp.boogie.model.BoogieAirport;
import org.mitre.tdp.boogie.validate.RecordElectorFactory;

/**
 * Serves as the top-level interface for an airport within Boogie.
 * <br>
 * Airports generally represent some position and identification information for facilities where aircraft land and depart from
 * as well as some contained {@link Runway} information.
 * <br>
 * On the off chance multiple airports may have been supplied for some downstream metric they can be de-duped with one of the
 * supplied {@link RecordElectorFactory#newUniqueAirportElector(Function)} functions.
 * <br>
 * See {@link BoogieAirport} for an immutable, buildable implementation of this interface.
 */
public interface Airport extends Fix {

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
   * Collection of all {@link Runway}s available at the airport.
   */
  List<? extends Runway> runways();

  // nice to have the option to flex an airport as a fix... though I'm loathe to do this interface extension - I wish I could make
  // these be non-public
  @Override
  default String fixIdentifier() {
    return airportIdentifier();
  }

  @Override
  default String fixRegion() {
    return airportRegion();
  }
}
