package org.mitre.tdp.boogie.conformance.alg.assign.combine;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.annotation.Nonnull;
import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;

class TestPathTerminatorBasedLegHasher {

  @Test
  void testHashingWithNearbyLatLon() {
    PathTerminatorBasedLegHasher hasher = PathTerminatorBasedLegHasher.newInstance();

    // from 4Bu1Nttu_DAACIz, RW21R at KPSC
    LatLong cifpLatLong = LatLong.of(46.26840303939609, -119.11504438450798);
    FlyableLeg flyableLeg1 = mockFlyableLeg(PathTerm.TF, cifpLatLong);
    Integer hash1 = hasher.apply(flyableLeg1);

    // from 4oPjcWRT84AAchP, RW21R at KPSC
    LatLong jeppLatLong = LatLong.of(46.268417341819735, -119.11506482388177);
    FlyableLeg flyableLeg2 = mockFlyableLeg(PathTerm.TF, jeppLatLong);
    Integer hash2 = hasher.apply(flyableLeg2);

    assertEquals(hash1, hash2, "Legs with similar path terminating fixes should hash together");
  }

  @Nonnull
  private FlyableLeg mockFlyableLeg(PathTerm legType, LatLong latLong) {
    FlyableLeg flyableLeg = mock(FlyableLeg.class, withSettings().verboseLogging());
    Leg leg = mock(Leg.class, withSettings().verboseLogging());
    Fix fix = mock(Fix.class, withSettings().verboseLogging());
    when(flyableLeg.current()).thenReturn(leg);
    when(leg.type()).thenReturn(legType);
    when(leg.pathTerminator()).thenReturn(fix);
    when(fix.latitude()).thenReturn(latLong.latitude());
    when(fix.longitude()).thenReturn(latLong.longitude());
    return flyableLeg;
  }

}