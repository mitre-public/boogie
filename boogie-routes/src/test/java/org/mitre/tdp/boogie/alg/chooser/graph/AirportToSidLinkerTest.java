package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airports;
import org.mitre.tdp.boogie.CONNR5;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;

class AirportToSidLinkerTest {

  private static final TokenGrapher GRAPHER = TokenGrapher.standard();

  @Test
  void test_LinkToCommonPortion() {

    Linker linker = Linker.airportToSid(anyAirport(Airports.KDEN()), anySid(CONNR5.INSTANCE, ResolvedToken::sidEnrouteCommon));

    Collection<LinkedLegs> links = linker.links();
    assertEquals(1, links.size(), "Should be one link from KDEN->CONNR (the start of the common portion)");

    Set<String> expected = Set.of(
        "CONNR:IF"
    );
    assertEquals(expected, links.stream().map(l -> handleFor(l.target())).collect(toSet()));
  }

  @Test
  void test_LinkToRunwayTransition() {

    Linker linker = Linker.airportToSid(anyAirport(Airports.KDEN()), anySid(CONNR5.INSTANCE, ResolvedToken::sidRunway));

    Collection<LinkedLegs> links = linker.links();
    assertEquals(8, links.size(), "Should be eight links from KDEN->various fixes (the starts of the runway transitions)");

    Set<String> expected = Set.of(
        "NONE:VA",
        "NONE:VI"
    );
    assertEquals(expected, links.stream().map(l -> handleFor(l.target())).collect(toSet()));
  }

  private String handleFor(Leg leg) {
    return String.format("%s:%s", leg.associatedFix().map(Fix::fixIdentifier).orElse("NONE"), leg.pathTerminator().name());
  }

  private AnyAirport anyAirport(Airport airport) {
    return new AnyAirport(airport, GRAPHER.graphRepresentationOf(ResolvedToken.standardAirport(airport)));
  }

  private AnySid anySid(Procedure sid, Function<Procedure, ResolvedToken> tokenizer) {
    ResolvedToken token = tokenizer.apply(sid);
    return new AnySid((Procedure) token.infrastructure(), GRAPHER.graphRepresentationOf(token));

  }
}
