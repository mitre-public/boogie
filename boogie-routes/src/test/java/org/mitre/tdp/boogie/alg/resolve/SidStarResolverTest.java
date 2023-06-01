package org.mitre.tdp.boogie.alg.resolve;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mitre.tdp.boogie.MockObjects.IF;
import static org.mitre.tdp.boogie.alg.LookupService.inMemory;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.split.RouteToken;

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

    Procedure atlJimmy = Procedure.builder()
        .procedureIdentifier("JIMMY")
        .airportIdentifier("KATL")
        .procedureType(procedureType)
        .requiredNavigationEquipage(RequiredNavigationEquipage.UNKNOWN)
        .transitions(List.of(Transition.builder()
            .transitionIdentifier("NONE")
            .transitionType(TransitionType.COMMON)
            .legs(List.of(l))
            .build()))
        .build();

    Procedure bnyJimmy = Procedure.builder()
        .procedureIdentifier("JIMMY")
        .airportIdentifier("KBNY")
        .procedureType(procedureType)
        .requiredNavigationEquipage(RequiredNavigationEquipage.UNKNOWN)
        .transitions(List.of(Transition.builder()
            .transitionIdentifier("NONE")
            .transitionType(TransitionType.COMMON)
            .legs(List.of(l))
            .build()))
        .build();

    LookupService<Procedure> service = inMemory(List.of(atlJimmy, bnyJimmy), p -> Stream.of(p.procedureIdentifier()));
    return new SidStarResolver(service);
  }
}
