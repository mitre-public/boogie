package org.mitre.tdp.boogie.conformance;

import java.time.Instant;
import java.util.Optional;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.util.Declinations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockObjects {

  public static Fix fix(String name, double lat, double lon) {
    Fix fix = mock(Fix.class);
    when(fix.identifier()).thenReturn(name);
    when(fix.latLong()).thenReturn(LatLong.of(lat, lon));
    when(fix.magneticVariation()).thenReturn(new MagneticVariation() {
      @Override
      public Optional<Float> published() {
        return Optional.empty();
      }

      @Override
      public float modeled() {
        return (float) Declinations.declination(lat, lon, Optional.empty(), Instant.parse("2019-01-01T00:00:00.00Z"));
      }
    });
    when(fix.toString()).thenReturn("Name: " + name);
    return fix;
  }

  public static MagneticVariation magneticVariation(float published, float modeled) {
    MagneticVariation variation = mock(MagneticVariation.class);
    when(variation.published()).thenReturn(Optional.of(published));
    when(variation.modeled()).thenReturn(modeled);
    return variation;
  }
}
