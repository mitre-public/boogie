package org.mitre.tdp.boogie.alg.resolve;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mitre.tdp.boogie.alg.LookupService.inMemory;
import static org.mitre.tdp.boogie.alg.split.RouteToken.standard;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.CONNR5;
import org.mitre.tdp.boogie.HOBTT2;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.alg.LookupService;

class SidResolverTest {

  @Test
  void test_MatchingSTAR() {

    SidResolver resolver = new SidResolver(
        procedureService(HOBTT2.INSTANCE)
    );

    ResolvedTokens resolved = resolver.resolve(
        standard("SHYRE", 0.),
        standard("HOBTT2", 1.),
        standard("KATL", 2.)
    );

    assertEquals(0, resolved.resolvedTokens().size(), "Should not resolve tokens for STAR.");
  }

  @Test
  void test_MatchingSID_AtAirport() {

    SidResolver resolver = new SidResolver(
        procedureService(CONNR5.INSTANCE)
    );

    ResolvedTokens resolved = resolver.resolve(
        standard("KDEN", 0.),
        standard("CONNR5", 1.),
        standard("SHYRE", 2.) // not in procedure
    );

    assertEquals(1, resolved.resolvedTokens().size(), "Expected match to SID (via arrival airport).");

    ResolvedToken.SidEnrouteCommon star = (ResolvedToken.SidEnrouteCommon) resolved.resolvedTokens().iterator().next();
    assertAll(
        () -> assertEquals("CONNR5", star.infrastructure().procedureIdentifier(), "Identifier"),
        () -> assertEquals("KDEN", star.infrastructure().airportIdentifier(), "Airport")
    );
  }

  @Test
  void test_MatchingSID_ExitFix() {

    SidResolver resolver = new SidResolver(
        procedureService(CONNR5.INSTANCE)
    );

    ResolvedTokens resolved = resolver.resolve(
        standard("SHYRE", 0.), // not the serviced airport
        standard("CONNR5", 1.),
        standard("DBL", 2.)
    );

    assertEquals(1, resolved.resolvedTokens().size(), "Expected match to SID (via exit fix).");

    ResolvedToken.SidEnrouteCommon star = (ResolvedToken.SidEnrouteCommon) resolved.resolvedTokens().iterator().next();
    assertAll(
        () -> assertEquals("CONNR5", star.infrastructure().procedureIdentifier(), "Identifier"),
        () -> assertEquals("KDEN", star.infrastructure().airportIdentifier(), "Airport")
    );
  }

  private LookupService<Procedure> procedureService(Procedure... procedures) {
    return inMemory(
        List.of(procedures),
        p -> Stream.of(p.procedureIdentifier())
    );
  }
}
