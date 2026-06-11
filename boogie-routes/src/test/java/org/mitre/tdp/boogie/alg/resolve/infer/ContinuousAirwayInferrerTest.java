package org.mitre.tdp.boogie.alg.resolve.infer;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.alg.RouteContext;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokens;
import org.mitre.tdp.boogie.alg.split.RouteToken;

class ContinuousAirwayInferrerTest {

  /**
   * Exercises a multi-hop chain below the twelve-segment maximum observed for one identifier in {@code A424-22std.dat.gz}.
   */
  @Test
  void infersOrderedContinuationsForSixSegmentChain() {
    List<Airway> chain = linkedChain();
    ResolvedTokens left = multiplyResolvedAirwaySection(chain);
    ResolvedTokens right = exitFixSection();

    List<ResolvedTokens> inferred = new ContinuousAirwayInferrer().inferBetween(left, right);

    assertAll(
        () -> assertEquals(chain.size() - 1, inferred.size(), "Should infer one section per hop after the start segment"),
        () -> IntStream.range(0, chain.size() - 1)
            .forEach(layer -> assertEquals(
                expectedLayer(chain, layer),
                airwaysIn(inferred.get(layer)),
                "Layer " + layer))
    );
  }

  @Test
  void sectionInferrerFactoryDelegatesToContinuousAirwayInferrer() {
    ResolvedTokens left = multiplyResolvedAirwaySection(linkedChain());
    ResolvedTokens right = exitFixSection();

    assertSameInference(left, right, SectionInferrer.continuousAirway());
  }

  @Test
  void standardRouteContextRegistersContinuousAirwayInferrerFirst() {
    ResolvedTokens left = multiplyResolvedAirwaySection(linkedChain());
    ResolvedTokens right = exitFixSection();

    assertSameInference(left, right, RouteContext.standard().build().inferrers().get(0));
  }

  private static void assertSameInference(ResolvedTokens left, ResolvedTokens right, SectionInferrer inferrer) {
    List<ResolvedTokens> expected = new ContinuousAirwayInferrer().inferBetween(left, right);
    List<ResolvedTokens> actual = inferrer.inferBetween(left, right);

    assertEquals(expected.size(), actual.size());
    IntStream.range(0, expected.size())
        .forEach(layer -> assertEquals(airwaysIn(expected.get(layer)), airwaysIn(actual.get(layer)), "Layer " + layer));
  }

  private static ResolvedTokens multiplyResolvedAirwaySection(List<Airway> chain) {
    return new ResolvedTokens(
        RouteToken.standard("W1", 1.),
        chain.stream().<ResolvedToken>map(ResolvedToken::standardAirway).toList()
    );
  }

  private static ResolvedTokens exitFixSection() {
    return new ResolvedTokens(
        RouteToken.standard("FIX7", 2.),
        List.of(ResolvedToken.standardFix(fix("FIX7", 6.0)))
    );
  }

  private static List<Airway> linkedChain() {
    return IntStream.range(0, 6)
        .mapToObj(i -> segment(
            "FIX" + (i + 1), i,
            "FIX" + (i + 2), i + 1))
        .toList();
  }

  private static Set<Airway> expectedLayer(List<Airway> chain, int layer) {
    return new LinkedHashSet<>(chain.subList(layer + 1, chain.size()));
  }

  private static Fix fix(String identifier, double latitude) {
    return Fix.builder()
        .fixIdentifier(identifier)
        .latLong(LatLong.of(latitude, 0.))
        .build();
  }

  private static Airway segment(String from, double fromLatitude, String to, double toLatitude) {
    Leg entry = Leg.tfBuilder(fix(from, fromLatitude), 1).build();
    Leg exit = Leg.tfBuilder(fix(to, toLatitude), 2).build();
    return Airway.builder()
        .airwayIdentifier("W1")
        .legs(List.of(entry, exit))
        .build();
  }

  private static Set<Airway> airwaysIn(ResolvedTokens tokens) {
    return tokens.resolvedTokens().stream()
        .map(ResolvedToken.StandardAirway.class::cast)
        .map(ResolvedToken.StandardAirway::infrastructure)
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }
}
