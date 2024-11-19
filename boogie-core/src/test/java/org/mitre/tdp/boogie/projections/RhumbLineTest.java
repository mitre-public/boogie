package org.mitre.tdp.boogie.projections;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.Rhumb;
import org.mitre.tdp.boogie.util.Streams;

class RhumbLineTest {

  static LatLong one = LatLong.of(45D, -60D);
  static LatLong two = LatLong.of(45D, -47D);
  static double azimuth = Rhumb.rhumbAzimuth(one, two);

  @Test
  void testSimple() {
    List<LatLong> latLongs = RhumbLine.project10NM(one, two);
    List<Double> azimuths = Streams.pairwise(latLongs)
        .map(i -> Rhumb.rhumbAzimuth(i.first(), i.second()))
        .filter(i -> Math.abs(i - azimuth) < .0001)
        .toList();
    assertAll(
        () -> assertEquals(56, latLongs.size(), "55 plus the start"),
        () -> assertEquals(55, azimuths.size(), "56 items into pairs")
    );
  }

}