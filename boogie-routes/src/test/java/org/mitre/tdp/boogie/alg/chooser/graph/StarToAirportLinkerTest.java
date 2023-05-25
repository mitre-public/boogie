package org.mitre.tdp.boogie.alg.chooser.graph;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airports;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.HOBTT2;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.alg.resolve.LinkedLegs;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;

import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StarToAirportLinkerTest {

  private static final TokenGrapher GRAPHER = TokenGrapher.standard();

  @Test
  void test_LinkToCommonPortion() {

    Linker linker = Linker.starToAirport(anyStar(HOBTT2.INSTANCE, ResolvedToken::starEnrouteCommon), anyAirport(Airports.KATL()));

    Collection<LinkedLegs> links = linker.links();
    assertEquals(1, links.size(), "Should be only one link from ENSLL->KATL (the end of the common portion)");

    Set<String> expected = Set.of(
        "ENSLL:TF"
    );
    assertEquals(expected, links.stream().map(l -> handleFor(l.source())).collect(toSet()));
  }

  @Test
  void test_LinkToRunwayTransition() {

    Linker linker = Linker.starToAirport(anyStar(HOBTT2.INSTANCE, ResolvedToken::starRunway), anyAirport(Airports.KATL()));

    Collection<LinkedLegs> links = linker.links();
    assertEquals(3, links.size(), "Should be only one link from ENSLL->KATL (the end of the common portion)");

    Set<String> expected = Set.of(
        "YURII:FM",
        "KEAVY:FM"
    );
    assertEquals(expected, links.stream().map(l -> handleFor(l.source())).collect(toSet()));
  }

  private String handleFor(Leg leg) {
    return String.format("%s:%s", leg.associatedFix().map(Fix::fixIdentifier).orElse("NONE"), leg.pathTerminator().name());
  }

  private AnyAirport anyAirport(Airport airport) {
    return new AnyAirport(airport, GRAPHER.graphRepresentationOf(ResolvedToken.standardAirport(airport)));
  }

  private AnyStar anyStar(Procedure sid, Function<Procedure, ResolvedToken<Procedure>> tokenizer) {
    ResolvedToken<Procedure> token = tokenizer.apply(sid);
    return new AnyStar(token.infrastructure(), GRAPHER.graphRepresentationOf(token));
  }
}
