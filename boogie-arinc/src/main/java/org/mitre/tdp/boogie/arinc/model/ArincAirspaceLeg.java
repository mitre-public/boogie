package org.mitre.tdp.boogie.arinc.model;

import java.util.Optional;

/**
 * The sequence and coordinate fields shared by controlled, restrictive, and FIR/UIR boundary legs.
 */
public interface ArincAirspaceLeg extends ArincModel {
  Integer sequenceNumber();

  Optional<Double> latitude();

  Optional<Double> longitude();

  Optional<Double> arcOriginLatitude();

  Optional<Double> arcOriginLongitude();

  Optional<Double> arcDistance();

  Optional<Double> arcBearing();
}
