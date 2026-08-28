package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Optional.ofNullable;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import org.mitre.caasd.commons.Course;
import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.Runway;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;

/**
 * Shared runway assembly operations used by both airports and heliports.
 */
final class RunwayAssembly {

  private RunwayAssembly() {
    throw new IllegalStateException("Cannot instantiate utility class.");
  }

  /**
   * Returns each runway end as the origin of a pair, including solitary runway ends.
   */
  static Stream<RunwayPair> directedPairs(Collection<ArincRunway> runways) {
    return runways.isEmpty() ? Stream.empty() : ReciprocalRunwayPairer.INSTANCE.apply(runways).stream()
        .flatMap(pair -> pair.otherEnd() == null
            ? Stream.of(pair)
            : Stream.of(pair, new RunwayPair(pair.otherEnd(), pair.thisRunway())));
  }

  static Runway.Standard standardRunway(
      ArincRunway origin,
      ArincRunway reciprocal,
      MagneticVariation magneticVariation
  ) {
    Optional<Course> trueCourse = origin.runwayMagneticBearing()
        .map(magneticVariation::magneticToTrue)
        .map(Course::ofDegrees)
        .or(() -> ofNullable(reciprocal).map(runway -> courseBetween(origin, runway)));

    Optional<Distance> length = origin.runwayLength()
        .map(value -> value - origin.thresholdDisplacementDistance().orElse(0))
        .map(Distance::ofFeet)
        .or(() -> ofNullable(reciprocal).map(runway ->
            LatLong.of(runway.latitude(), runway.longitude())
                .distanceTo(LatLong.of(origin.latitude(), origin.longitude()))));

    return Runway.builder()
        .runwayIdentifier(origin.runwayIdentifier())
        .origin(LatLong.of(origin.latitude(), origin.longitude()))
        .length(length.orElse(null))
        .course(trueCourse.orElse(null))
        .originElevation(origin.landingThresholdElevation().map(Distance::ofFeet).orElse(null))
        .build();
  }

  private static Course courseBetween(ArincRunway origin, ArincRunway reciprocal) {
    return LatLong.of(origin.latitude(), origin.longitude())
        .courseTo(LatLong.of(reciprocal.latitude(), reciprocal.longitude()));
  }
}
