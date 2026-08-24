package org.mitre.tdp.boogie.conformance.alg.assign.combine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.Collections;
import java.util.Optional;
import javax.annotation.Nonnull;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.Route;

import com.google.common.collect.Range;

class TestPathTerminatorBasedLegHasher {

  @Test
  void testHashingWithNearbyLatLon() {
    PathTerminatorBasedLegHasher hasher = PathTerminatorBasedLegHasher.newInstance();

    // from 4Bu1Nttu_DAACIz, RW21R at KPSC
    LatLong cifpLatLong = LatLong.of(46.26840303939609, -119.11504438450798);
    FlyableLeg flyableLeg1 = mockFlyableLeg(PathTerminator.TF, cifpLatLong);
    Integer hash1 = hasher.apply(flyableLeg1);

    // from 4oPjcWRT84AAchP, RW21R at KPSC
    LatLong latLongJ = LatLong.of(46.268417341819735, -119.11506482388177);
    FlyableLeg flyableLeg2 = mockFlyableLeg(PathTerminator.TF, latLongJ);
    Integer hash2 = hasher.apply(flyableLeg2);

    assertEquals(hash1, hash2, "Legs with similar path terminating fixes should hash together");
  }

  @Test
  void testHashIncludesCourseReference() {
    PathTerminatorBasedLegHasher hasher = PathTerminatorBasedLegHasher.newInstance();
    Leg magnetic = Leg.builder(PathTerminator.CA, 0)
        .outboundMagneticCourse(125.)
        .altitudeConstraint(Range.atLeast(1000.))
        .build();
    Leg truth = Leg.builder(PathTerminator.CA, 0)
        .outboundTrueCourse(125.)
        .altitudeConstraint(Range.atLeast(1000.))
        .build();

    assertNotEquals(hasher.apply(flyable(magnetic)), hasher.apply(flyable(truth)));
  }

  private FlyableLeg flyable(Leg leg) {
    return new FlyableLeg(null, leg, null, Route.newRoute(Collections.emptyList(), new Object()));
  }

  @Nonnull
  private FlyableLeg mockFlyableLeg(PathTerminator legType, LatLong latLong) {
    FlyableLeg flyableLeg = mock(FlyableLeg.class, withSettings().verboseLogging());
    Leg leg = mock(Leg.class, withSettings().verboseLogging());
    Fix fix = mock(Fix.class, withSettings().verboseLogging());
    when(flyableLeg.current()).thenReturn(leg);
    when(leg.pathTerminator()).thenReturn(legType);
    when(leg.associatedFix()).thenReturn((Optional) Optional.of(fix));
    when(fix.latitude()).thenReturn(latLong.latitude());
    when(fix.longitude()).thenReturn(latLong.longitude());
    return flyableLeg;
  }
}
