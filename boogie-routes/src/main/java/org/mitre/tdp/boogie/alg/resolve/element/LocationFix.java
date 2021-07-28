package org.mitre.tdp.boogie.alg.resolve.element;

import java.util.Optional;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;

final class LocationFix implements Fix {

  private final String fixIdentifier;
  private final String fixRegion;
  private final LatLong location;

  LocationFix(String fixIdentifier, String fixRegion, LatLong loc) {
    this.fixIdentifier = fixIdentifier;
    this.fixRegion = fixRegion;
    this.location = loc;
  }

  @Override
  public String fixIdentifier() {
    return fixIdentifier;
  }

  @Override
  public String fixRegion() {
    return fixRegion;
  }

  @Override
  public LatLong latLong() {
    return location;
  }

  @Override
  public Optional<Double> publishedVariation() {
    return Optional.empty();
  }

  @Override
  public double modeledVariation() {
    return 0.d;
  }

  @Override
  public Optional<Double> elevation() {
    return Optional.empty();
  }
}
