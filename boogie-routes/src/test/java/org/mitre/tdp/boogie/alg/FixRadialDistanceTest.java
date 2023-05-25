package org.mitre.tdp.boogie.alg;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Course;
import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.model.BoogieFix;

class FixRadialDistanceTest {

  @Test
  void testFormatting() {
    assertEquals("SHERL000011", dummy().formattedIdentifier());
  }

  @Test
  void testProjection() {

    LatLong projected = dummy().projectedLocation();

    assertAll(
        () -> assertNotEquals(0., projected.latitude(), "Latitude"),
        () -> assertNotEquals(0., projected.longitude(), "Longitude")
    );
  }

  private FixRadialDistance dummy() {

    BoogieFix fix = new BoogieFix.Builder()
        .fixIdentifier("SHERL")
        .fixRegion("K2")
        .latitude(0.)
        .longitude(0.)
        .elevation(0.)
        .modeledVariation(10.)
        .build();

    return FixRadialDistance.create(
        fix,
        Course.ofDegrees(0.),
        Distance.ofNauticalMiles(11.)
    );
  }
}
