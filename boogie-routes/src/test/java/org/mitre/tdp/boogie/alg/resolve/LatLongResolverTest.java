package org.mitre.tdp.boogie.alg.resolve;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.alg.split.RouteToken;

class LatLongResolverTest {

  private static final LatLongResolver RESOLVER = new LatLongResolver();

  @Test
  void test_FAA() {

    RouteToken token = RouteToken.faaIfrBuilder("5300N/14000W", 0.)
        .wildcards(" ")
        .build();

    List<ResolvedToken> resolved = RESOLVER.resolve(token);
    assertEquals(1, resolved.size(), "Should resolve to a single element.");

    ResolvedToken.DirectToLatLong latLong = (ResolvedToken.DirectToLatLong) resolved.get(0);
    assertEquals(LatLong.of(53., -140.), latLong.infrastructure());
  }

  @Test
  void test_IcaoD() {

    RouteToken token = RouteToken.icaoBuilder("53N140W", 0.)
        .wildcards("/")
        .build();

    List<ResolvedToken> resolved = RESOLVER.resolve(token);
    assertEquals(1, resolved.size(), "Should resolve to a single element.");

    ResolvedToken.StandardLatLong latLong = (ResolvedToken.StandardLatLong) resolved.get(0);
    assertEquals(LatLong.of(53., -140.), latLong.infrastructure());
  }

  @Test
  void test_IcaoDM() {

    RouteToken token = RouteToken.icaoBuilder("5300N14000W", 0.)
        .wildcards("/")
        .build();

    List<ResolvedToken> resolved = RESOLVER.resolve(token);
    assertEquals(1, resolved.size(), "Should resolve to a single element.");

    ResolvedToken.StandardLatLong latLong = (ResolvedToken.StandardLatLong) resolved.get(0);
    assertEquals(LatLong.of(53., -140.), latLong.infrastructure());
  }
}
