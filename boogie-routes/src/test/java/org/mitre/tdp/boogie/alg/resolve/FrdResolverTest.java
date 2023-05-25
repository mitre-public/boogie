package org.mitre.tdp.boogie.alg.resolve;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mitre.tdp.boogie.MockObjects.fix;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Course;
import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.split.RouteToken;

class FrdResolverTest {

  @Test
  void testBearingDistanceTailored() {

    Pair<Course, Distance> bearingAndDistance = FrdResolver.bearingDistance("HTO354018");

    assertAll(
        () -> assertEquals(354., bearingAndDistance.first().inDegrees(), 0.01),
        () -> assertEquals(18., bearingAndDistance.second().inNauticalMiles(), 0.01)
    );
  }

  @Test
  void test_Tailored() {

    RouteToken token = RouteToken.faaIfrBuilder("JIMMY354018", 0.)
        .wildcards("/")
        .build();

    List<ResolvedToken> resolved = resolver(token).resolve(token);
    assertEquals(1, resolved.size(), "Should be on resolved token.");

    ResolvedToken.StandardFrd frd = (ResolvedToken.StandardFrd) resolved.iterator().next();
    assertAll(
        () -> assertEquals("JIMMY", frd.infrastructure().fix().fixIdentifier(), "Identifier"),
        () -> assertEquals(LatLong.of(0., 0.), frd.infrastructure().fix().latLong(), "LatLong"),
        () -> assertEquals(Course.ofDegrees(354.), frd.infrastructure().radial(), "Radial"),
        () -> assertEquals(Distance.ofNauticalMiles(18.), frd.infrastructure().distance(), "Distance")
    );
  }

  @Test
  void test_Direct() {

    RouteToken token = RouteToken.faaIfrBuilder("JIMMY354018", 0.).build();

    List<ResolvedToken> resolved = resolver(token).resolve(token);
    assertEquals(1, resolved.size(), "Should be on resolved token.");

    ResolvedToken.DirectToFrd frd = (ResolvedToken.DirectToFrd) resolved.iterator().next();
    assertAll(
        () -> assertEquals("JIMMY", frd.infrastructure().fix().fixIdentifier(), "Identifier"),
        () -> assertEquals(LatLong.of(0., 0.), frd.infrastructure().fix().latLong(), "LatLong"),
        () -> assertEquals(Course.ofDegrees(354.), frd.infrastructure().radial(), "Radial"),
        () -> assertEquals(Distance.ofNauticalMiles(18.), frd.infrastructure().distance(), "Distance")
    );
  }

  private FrdResolver resolver(RouteToken token) {
    String name = token.infrastructureName();

    Fix fix = fix(name.substring(0, name.length() - 6), 0., 0.);
    return new FrdResolver(LookupService.inMemory(List.of(fix), f -> Stream.of(f.fixIdentifier())));
  }
}
