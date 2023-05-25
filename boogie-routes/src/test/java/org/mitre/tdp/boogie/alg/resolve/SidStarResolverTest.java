package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mitre.tdp.boogie.MockObjects.IF;
import static org.mitre.tdp.boogie.MockObjects.transition;
import static org.mitre.tdp.boogie.alg.LookupService.inMemory;
import static org.mitre.tdp.boogie.model.ProcedureFactory.newProcedures;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.split.RouteToken;
import org.mitre.tdp.boogie.model.BoogieTransition;

class SidStarResolverTest {

  @Test
  void test_NoAirportContext() {

    ResolvedTokens resolved = resolver(ProcedureType.SID)
        .resolve(null, RouteToken.standard("JIMMY", 0.), null);

    assertEquals(2, resolved.resolvedTokens().size(), "Expected two resolved tokens");

    ResolvedToken.SidEnrouteCommon sid = (ResolvedToken.SidEnrouteCommon) resolved.resolvedTokens().iterator().next();
    assertAll(
        () -> assertEquals("JIMMY", sid.infrastructure().procedureIdentifier(), "Identifier"),
        () -> assertEquals(ProcedureType.SID, sid.infrastructure().procedureType(), "Type")
    );
  }

  @Test
  void test_SIDWithAirport() {

    ResolvedTokens resolved = resolver(ProcedureType.SID)
        .resolve(RouteToken.standard("KATL", 0.), RouteToken.standard("JIMMY", 1.), null);

    assertEquals(1, resolved.resolvedTokens().size(), "Expected two resolved tokens");

    ResolvedToken.SidEnrouteCommon sid = (ResolvedToken.SidEnrouteCommon) resolved.resolvedTokens().iterator().next();
    assertAll(
        () -> assertEquals("JIMMY", sid.infrastructure().procedureIdentifier(), "Identifier"),
        () -> assertEquals("KATL", sid.infrastructure().airportIdentifier(), "Airport")
    );
  }

  @Test
  void test_STARWithAirport() {

    ResolvedTokens resolved = resolver(ProcedureType.STAR)
        .resolve(null, RouteToken.standard("JIMMY", 1.), RouteToken.standard("KATL", 2.));

    assertEquals(1, resolved.resolvedTokens().size(), "Expected two resolved tokens");

    ResolvedToken.StarEnrouteCommon star = (ResolvedToken.StarEnrouteCommon) resolved.resolvedTokens().iterator().next();
    assertAll(
        () -> assertEquals("JIMMY", star.infrastructure().procedureIdentifier(), "Identifier"),
        () -> assertEquals("KATL", star.infrastructure().airportIdentifier(), "Airport")
    );
  }

  private SidStarResolver resolver(ProcedureType procedureType) {
    Leg l = IF("FOO", 0.0, 0.0);

    BoogieTransition atlJimmy = transition("JIMMY", "KATL", TransitionType.COMMON, procedureType, singletonList(l));

    BoogieTransition bnyJimmy = transition("JIMMY", "KBNY", TransitionType.COMMON, procedureType, singletonList(l));

    return new SidStarResolver(
        inMemory(newProcedures(Arrays.asList(atlJimmy, bnyJimmy)), p -> Stream.of(p.procedureIdentifier()))
    );
  }
}
