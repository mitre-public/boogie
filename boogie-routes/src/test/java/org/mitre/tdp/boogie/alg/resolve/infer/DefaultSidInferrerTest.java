package org.mitre.tdp.boogie.alg.resolve.infer;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airports;
import org.mitre.tdp.boogie.CONNR5;
import org.mitre.tdp.boogie.COSTR3;
import org.mitre.tdp.boogie.CategoryAndType;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokens;
import org.mitre.tdp.boogie.alg.split.RouteToken;

class DefaultSidInferrerTest {

  @Test
  void testAirportToFix_NoMatch() {

    List<ResolvedTokens> inferred = inferSidDenverDbl(List.of(COSTR3.INSTANCE));
    assertEquals(0, inferred.size(), "Should infer no procedure (no available matches)");
  }

  @Test
  void testAirportToFix_WithMatch() {

    List<ResolvedTokens> inferred = inferSidDenverDbl(List.of(CONNR5.INSTANCE, COSTR3.INSTANCE));
    assertEquals(1, inferred.size(), "Should infer one procedure (CONNR5)");

    ResolvedTokens tokens = inferred.get(0);
    assertEquals(1, tokens.resolvedTokens().size(), "Should be one underlying resolved token.");

    ResolvedToken token = tokens.resolvedTokens().iterator().next();
    assertAll(
        () -> assertEquals("CONNR5", tokens.routeToken().infrastructureName(), "RouteToken Infrastructure Name"),
        () -> assertEquals(.5, tokens.routeToken().index(), "RouteToken Index"),

        () -> assertEquals(ResolvedToken.SidEnrouteCommon.class, token.getClass(), "ResolvedToken Class")
    );
  }

  private List<ResolvedTokens> inferSidDenverDbl(List<Procedure> available) {
    ResolvedTokens left = new ResolvedTokens(
        RouteToken.standard("KDEN", 0.),
        List.of(ResolvedToken.standardAirport(Airports.KDEN()))
    );

    Fix dbl = Fix.builder()
        .fixIdentifier("DBL")
        .latLong(LatLong.of(39.439344444444444, -106.89468055555557))
        .build();

    ResolvedTokens right = new ResolvedTokens(
        RouteToken.standard("DBL", 1.),
        List.of(ResolvedToken.standardFix(dbl))
    );

    LookupService<Procedure> lookup = LookupService.inMemory(
        available,
        p -> Stream.of(p.procedureIdentifier())
    );

    return new DefaultSidInferrer(lookup, "CONNR5", CategoryAndType.NULL).inferBetween(left, right);
  }
}
