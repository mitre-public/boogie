package org.mitre.tdp.boogie.arinc.database;

import java.util.Collection;
import java.util.Optional;

import org.mitre.tdp.boogie.Helipad;
import org.mitre.tdp.boogie.arinc.model.ArincGnssLandingSystem;
import org.mitre.tdp.boogie.arinc.model.ArincHelipad;
import org.mitre.tdp.boogie.arinc.model.ArincLocalizerGlideSlope;

public final class HelipadPage {
  private final ArincHelipad helipad;
  private final ArincLocalizerGlideSlope localizerGlideSlope;
  private final ArincLocalizerGlideSlope secondaryLocalizerGlideSlope;
  private final Collection<ArincGnssLandingSystem> gnssLandingSystem;

  public HelipadPage(ArincHelipad helipad, ArincLocalizerGlideSlope localizerGlideSlope, ArincLocalizerGlideSlope secondaryLocalizerGlideSlope, Collection<ArincGnssLandingSystem> gnssLandingSystem) {
    this.helipad = helipad;
    this.localizerGlideSlope = localizerGlideSlope;
    this.secondaryLocalizerGlideSlope = secondaryLocalizerGlideSlope;
    this.gnssLandingSystem = gnssLandingSystem;
  }

  public ArincHelipad helipad() {
    return helipad;
  }

  public Optional<ArincLocalizerGlideSlope> localizerGlideSlope() {
    return Optional.ofNullable(localizerGlideSlope);
  }

  public Optional<ArincLocalizerGlideSlope> secondaryLocalizerGlideSlope() {
    return Optional.ofNullable(secondaryLocalizerGlideSlope);
  }

  public Collection<ArincGnssLandingSystem> gnssLandingSystem() {
    return gnssLandingSystem;
  }
}
