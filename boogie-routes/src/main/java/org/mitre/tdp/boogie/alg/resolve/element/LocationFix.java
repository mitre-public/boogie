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
  public NavigationSource source() {
    return NavigationSource.FUSED;
  }

  @Override
  public LatLong latLong() {
    return location;
  }


  @Override
  public MagneticVariation magneticVariation() {
    return new MagneticVariation() {
      @Override
      public Optional<Float> published() {
        return Optional.empty();
      }

      @Override
      public float modeled() {
        return 0.0f;
      }
    };
  }

  @Override
  public float elevation() {
    return 0.0f;
  }
}
