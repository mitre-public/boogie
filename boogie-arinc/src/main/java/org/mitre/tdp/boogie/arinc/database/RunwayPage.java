package org.mitre.tdp.boogie.arinc.database;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.model.ArincGnssLandingSystem;
import org.mitre.tdp.boogie.arinc.model.ArincLocalizerGlideSlope;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;

/**
 * Represents a page of structured ARINC 424 data related to an {@link ArincRunway}.
 */
final class RunwayPage {

  private final ArincRunway runway;
  private final ArincLocalizerGlideSlope localizerGlideSlope;
  private final ArincLocalizerGlideSlope secondaryLocalizerGlideSlope;

  private final ArincGnssLandingSystem gnssLandingSystem;

  RunwayPage(
      ArincRunway runway,
      ArincLocalizerGlideSlope localizerGlideSlope,
      ArincLocalizerGlideSlope secondaryLocalizerGlideSlope,
      ArincGnssLandingSystem gnssLandingSystem
  ) {
    this.runway = requireNonNull(runway);
    this.localizerGlideSlope = localizerGlideSlope;
    this.secondaryLocalizerGlideSlope = secondaryLocalizerGlideSlope;
    this.gnssLandingSystem = gnssLandingSystem;
  }

  public ArincRunway runway() {
    return runway;
  }

  public Optional<ArincLocalizerGlideSlope> primaryLocalizerGlideSlope() {
    return Optional.ofNullable(localizerGlideSlope);
  }

  public Optional<ArincLocalizerGlideSlope> secondaryLocalizerGlideSlope() {
    return Optional.ofNullable(secondaryLocalizerGlideSlope);
  }

  public Optional<ArincGnssLandingSystem> gnssLandingSystem() {
    return Optional.ofNullable(gnssLandingSystem);
  }
}
