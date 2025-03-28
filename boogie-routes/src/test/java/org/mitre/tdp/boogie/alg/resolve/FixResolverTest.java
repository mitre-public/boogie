package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mitre.tdp.boogie.MockObjects.fix;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.split.RouteToken;

class FixResolverTest {

  @Test
  void test_Tailored() {

    RouteToken token = RouteToken.faaIfrBuilder("JIMMY", 0.)
        .wildcards("/")
        .build();

    List<ResolvedToken> resolved = resolver(token).resolve(token);
    assertEquals(1, resolved.size(), "Should be on resolved token.");

    ResolvedToken.StandardFix fix = (ResolvedToken.StandardFix) resolved.iterator().next();
    assertAll(
        () -> assertEquals("JIMMY", fix.infrastructure().fixIdentifier(), "Identifier"),
        () -> assertEquals(LatLong.of(0., 0.), fix.infrastructure().latLong(), "LatLong")
    );
  }

  @Test
  void test_Direct() {

    RouteToken token = RouteToken.faaIfrBuilder("JIMMY", 0.)
        .wildcards(" ")
        .build();

    List<ResolvedToken> resolved = resolver(token).resolve(token);
    assertEquals(1, resolved.size(), "Should be on resolved token.");

    ResolvedToken.DirectToFix fix = (ResolvedToken.DirectToFix) resolved.iterator().next();
    assertAll(
        () -> assertEquals("JIMMY", fix.infrastructure().fixIdentifier(), "Identifier"),
        () -> assertEquals(LatLong.of(0., 0.), fix.infrastructure().latLong(), "LatLong")
    );
  }

  private FixResolver resolver(RouteToken token) {
    Fix fix = fix(token.infrastructureName(), 0., 0.);
    return new FixResolver(LookupService.inMemory(singleton(fix), f -> Stream.of(f.fixIdentifier())));
  }
}
