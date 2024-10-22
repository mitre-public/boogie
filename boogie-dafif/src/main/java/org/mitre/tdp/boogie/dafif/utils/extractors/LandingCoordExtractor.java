package org.mitre.tdp.boogie.dafif.utils.extractors;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.dafif.model.DafifAddRunway;
import org.mitre.tdp.boogie.dafif.model.DafifRunway;

/**
 * DAFIF has two confusing parts of runways. First, the primary record contains coordinates for the base of the runway.
 * However, if there is a displacement the landing thresholds are in another file called AddRunway.
 * One must go check for the existance of a record in the add runway, then use that if its there if you want to know
 * where to land.
 *
 * Then DAFIF's data model is a single two sided runway. So you can do both in one shot.
 */
public final class LandingCoordExtractor implements Function<Pair<DafifRunway, DafifAddRunway>, Pair<LatLong, LatLong>> {
  public static final LandingCoordExtractor INSTANCE = new LandingCoordExtractor();

  private static final Function<DafifRunway, Optional<LatLong>> LE = runway -> Optional.ofNullable(runway)
      .filter(r -> r.lowEndDegreesLatitude().isPresent() && r.lowEndDegreesLongitude().isPresent())
      .map(r -> LatLong.of(r.lowEndDegreesLatitude().get(), r.lowEndDegreesLongitude().get()));
  private static final Function<DafifRunway, Optional<LatLong>> HE = runway -> Optional.ofNullable(runway)
      .filter(r -> r.highEndDegreesLatitude().isPresent() && r.highEndDegreesLongitude().isPresent())
      .map(r -> LatLong.of(r.highEndDegreesLatitude().get(), r.highEndDegreesLongitude().get()));

  private LandingCoordExtractor() {
  }

  /**
   * This method returns both landing threshold coordinates of a dafif runway
   * @param pair The DafifRunway and DafifAddRunway
   * @return a pair of the Low End and High End coordinates.
   */
  @Override
  public Pair<LatLong, LatLong> apply(Pair<DafifRunway, DafifAddRunway> pair) {
    LatLong leOrigin = Optional.ofNullable(pair.second())
        .filter(r -> r.lowEndDisplacedThresholdDegreesLatitude().isPresent() && r.lowEndDisplacedThresholdDegreesLongitude().isPresent())
        .map(r -> LatLong.of(r.lowEndDisplacedThresholdDegreesLatitude().get(), r.lowEndDisplacedThresholdDegreesLongitude().get()))
        .orElseGet(() -> LE.apply(pair.first()).orElseThrow(() -> new IllegalArgumentException("Runway without coordinates: ".concat(pair.first().toString()))));

    LatLong heOrigin = Optional.ofNullable(pair.second())
        .filter(r -> r.highEndDisplacedThresholdDegreesLatitude().isPresent() && r.highEndDisplacedThresholdDegreesLongitude().isPresent())
        .map(r -> LatLong.of(r.highEndDisplacedThresholdDegreesLatitude().get(), r.highEndDisplacedThresholdDegreesLongitude().get()))
        .orElseGet(() -> HE.apply(pair.first()).orElseThrow(() -> new IllegalArgumentException("Runway without coordinates: ".concat(pair.first().toString()))));

    return Pair.of(leOrigin, heOrigin);
  }
}
