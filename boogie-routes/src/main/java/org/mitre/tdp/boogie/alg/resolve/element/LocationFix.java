package org.mitre.tdp.boogie.alg.resolve.element;

import java.util.Optional;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.NavigationSource;

final class LocationFix implements Fix {

  private final String identifier;
  private final LatLong location;

  LocationFix(String id, LatLong loc) {
    this.identifier = id;
    this.location = loc;
  }

  @Override
  public String identifier() {
    return identifier;
  }

  @Override
  public NavigationSource navigationSource() {
    return () -> "ANONYMOUS";
  }

  @Override
  public LatLong latLong() {
    return location;
  }


  @Override
  public MagneticVariation magneticVariation() {
    return new MagneticVariation() {
      @Override
      public Optional<Double> published() {
        return Optional.empty();
      }

      @Override
      public double modeled() {
        return 0.0d;
      }
    };
  }

  @Override
  public Optional<Double> elevation() {
    return Optional.empty();
  }
}
