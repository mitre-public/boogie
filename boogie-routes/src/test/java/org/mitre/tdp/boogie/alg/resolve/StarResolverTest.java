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

class StarResolverTest {

  @Test
  void test_MatchingSID() {

    StarResolver resolver = new StarResolver(
        procedureService(CONNR5.INSTANCE)
    );

    ResolvedTokens resolved = resolver.resolve(
        standard("KDEN", 0.),
        standard("CONNR5", 1.),
        standard("DBL", 2.)
    );

    assertEquals(0, resolved.resolvedTokens().size(), "Should not resolve tokens for SID.");
  }

  @Test
  void test_MatchingSTAR_AtAirport() {

    StarResolver resolver = new StarResolver(
        procedureService(HOBTT2.INSTANCE)
    );

    ResolvedTokens resolved = resolver.resolve(
        standard("CONNR", 0.), // not in procedure
        standard("HOBTT2", 1.),
        standard("KATL", 2.)
    );

    assertEquals(1, resolved.resolvedTokens().size(), "Expected match to STAR (via arrival airport).");

    ResolvedToken.StarEnrouteCommon star = (ResolvedToken.StarEnrouteCommon) resolved.resolvedTokens().iterator().next();
    assertAll(
        () -> assertEquals("HOBTT2", star.infrastructure().procedureIdentifier(), "Identifier"),
        () -> assertEquals("KATL", star.infrastructure().airportIdentifier(), "Airport")
    );
  }

  @Test
  void test_MatchingSTAR_EntryFix() {

    StarResolver resolver = new StarResolver(
        procedureService(HOBTT2.INSTANCE)
    );

    ResolvedTokens resolved = resolver.resolve(
        standard("SHYRE", 0.),
        standard("HOBTT2", 1.),
        standard("ENSLL", 2.) // not the serviced airport
    );

    assertEquals(1, resolved.resolvedTokens().size(), "Expected match to STAR (via entry fix).");

    ResolvedToken.StarEnrouteCommon star = (ResolvedToken.StarEnrouteCommon) resolved.resolvedTokens().iterator().next();
    assertAll(
        () -> assertEquals("HOBTT2", star.infrastructure().procedureIdentifier(), "Identifier"),
        () -> assertEquals("KATL", star.infrastructure().airportIdentifier(), "Airport")
    );
  }

  private LookupService<Procedure> procedureService(Procedure... procedures) {
    return inMemory(
        List.of(procedures),
        p -> Stream.of(p.procedureIdentifier())
    );
  }
}
