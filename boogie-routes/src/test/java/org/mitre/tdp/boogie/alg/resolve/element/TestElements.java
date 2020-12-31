package org.mitre.tdp.boogie.alg.resolve.element;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.test.MockObjects.IF;
import static org.mitre.tdp.boogie.test.MockObjects.TF;
import static org.mitre.tdp.boogie.test.MockObjects.magneticVariation;
import static org.mitre.tdp.boogie.utils.Collections.allMatch;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.graph.ProcedureGraph;
import org.mitre.tdp.boogie.alg.graph.TestProcedureGraph;
import org.mitre.tdp.boogie.models.LinkedLegs;
import org.mitre.tdp.boogie.utils.Iterators;

public class TestElements {

  private static Airway singleAirway() {
    Leg l1 = TF("YYT", 0.0, 0.0);
    Leg l2 = TF("YYZ", 0.0, 1.0);
    Leg l3 = TF("ZZT", 0.0, 2.0);
    Leg l4 = TF("ZZY", 0.0, 3.0);

    Airway airway = mock(Airway.class);

    String airwayId = "J121";

    when(airway.legs()).thenReturn((List) Arrays.asList(l1, l2, l3, l4));
    when(airway.identifier()).thenReturn(airwayId);
    return airway;
  }

  private static ProcedureGraph singleTransitionProcedureGraph() {
    Leg l1 = IF("ZZV", 0.0, 0.0);
    Leg l2 = TF("ZZY", 0.0, 1.0);
    Leg l3 = TF("ZZZ", 0.0, 2.0);

    Transition transition = mock(Transition.class);
    when(transition.transitionType()).thenReturn(TransitionType.COMMON);
    when(transition.legs()).thenReturn((List) Arrays.asList(l1, l2, l3));
    when(transition.procedure()).thenReturn("GNDLF1");
    when(transition.procedureType()).thenReturn(ProcedureType.STAR);
    when(transition.navigationSource()).thenReturn(() -> "CIFP");

    return ProcedureGraph.from(Collections.singletonList(transition));
  }

  @Test
  public void testAirportElement() {
    Airport airport = mock(Airport.class);

    LatLong airportLocation = LatLong.of(0.0, 0.0);
    String airportId = "KATL";

    when(airport.latLong()).thenReturn(airportLocation);
    when(airport.identifier()).thenReturn(airportId);
    when(airport.navigationSource()).thenReturn(() -> "CIFP");

    AirportElement element = new AirportElement(airport);

    assertEquals(element.legs().size(), 1);

    LinkedLegs linked = element.legs().get(0);

    assertEquals(linked.source().leg().pathTerminator().latLong(), airportLocation);
    assertEquals(linked.target().leg().pathTerminator().latLong(), airportLocation);

    assertEquals(linked.source().leg().pathTerminator().identifier(), "KATL");
    assertEquals(linked.target().leg().pathTerminator().identifier(), "KATL");

    assertEquals(linked.source().leg().type(), PathTerm.IF);
    assertEquals(linked.source().leg().type(), PathTerm.IF);
  }

  // no hard exception
  @Test
  public void testAirwayElementSafeWithAirwaysOfLessThanTwoLegs() {
    Leg leg = mock(Leg.class);

    Airway airway = mock(Airway.class);
    when(airway.legs()).thenReturn((List) Collections.singletonList(leg));

    AirwayElement element = new AirwayElement(airway);
    assertEquals(0, element.buildLegs().size());
  }

  @Test
  public void testAirwayElement() {
    Airway airway = singleAirway();

    AirwayElement element = new AirwayElement(airway);

    assertEquals(element.legs().size(), 6);

    assertTrue(element.legs().stream().allMatch(l ->
        l.source().leg().type().equals(PathTerm.TF)
            && l.target().leg().type().equals(PathTerm.TF)));

    List<LinkedLegs> linked = element.legs();

    LinkedLegs ll1_1 = linked.get(0);
    LinkedLegs ll1_2 = linked.get(1);

    assertEquals(ll1_1.source(), ll1_2.target());
    assertEquals(ll1_1.target(), ll1_2.source());
    assertEquals(ll1_1.source().leg().pathTerminator().identifier(), "YYT");
    assertEquals(ll1_1.target().leg().pathTerminator().identifier(), "YYZ");

    LinkedLegs ll2_1 = linked.get(2);
    LinkedLegs ll2_2 = linked.get(3);

    assertEquals(ll2_1.source(), ll2_2.target());
    assertEquals(ll2_1.target(), ll2_2.source());
    assertEquals(ll2_1.source().leg().pathTerminator().identifier(), "YYZ");
    assertEquals(ll2_1.target().leg().pathTerminator().identifier(), "ZZT");

    LinkedLegs ll3_1 = linked.get(4);
    LinkedLegs ll3_2 = linked.get(5);

    assertEquals(ll3_1.source(), ll3_2.target());
    assertEquals(ll3_1.target(), ll3_2.source());
    assertEquals(ll3_1.source().leg().pathTerminator().identifier(), "ZZT");
    assertEquals(ll3_1.target().leg().pathTerminator().identifier(), "ZZY");
  }

  @Test
  public void testAirwayElementSubsequentLegLinkReferences() {
    Airway airway = singleAirway();

    AirwayElement element = new AirwayElement(airway);

    List<LinkedLegs> linked = element.legs();

    List<LinkedLegs> forward = IntStream.range(0, linked.size() / 2)
        .map(i -> 2 * i)
        .mapToObj(linked::get)
        .collect(Collectors.toList());

    Iterators.pairwise(forward, (ll1, ll2) -> {
      assertEquals(ll1.target(), ll2.source(), "The target of the previous leg should be identical to the source of the next.");
      assertEquals(ll1.target().hashCode(), ll2.source().hashCode(), "The target of the previous leg should have the same hashCode as the source of the next");
    });

    List<LinkedLegs> backwards = IntStream.range(0, linked.size() / 2)
        .map(i -> (2 * i) + 1)
        .mapToObj(linked::get)
        .collect(Collectors.toList());

    Iterators.pairwise(backwards, (ll1, ll2) -> {
      assertEquals(ll1.source(), ll2.target(), "The target of the previous leg should be identical to the source of the next.");
      assertEquals(ll1.source().hashCode(), ll2.target().hashCode(), "The target of the previous leg should have the same hashCode as the source of the next");
    });
  }

  @Test
  public void testFixElement() {
    Fix fix = mock(Fix.class);

    LatLong fixLocation = LatLong.of(0.0, 0.0);
    String fixId = "SHERL";

    when(fix.latLong()).thenReturn(fixLocation);
    when(fix.identifier()).thenReturn(fixId);
    when(fix.navigationSource()).thenReturn(() -> "CIFP");

    FixElement element = new FixElement(fix, "");

    assertEquals(element.legs().size(), 1);

    LinkedLegs linked = element.legs().get(0);

    assertAll(
        () -> assertEquals(fixLocation, linked.source().leg().pathTerminator().latLong()),
        () -> assertEquals(fixLocation, linked.target().leg().pathTerminator().latLong()),

        () -> assertEquals("SHERL", linked.source().leg().pathTerminator().identifier()),
        () -> assertEquals("SHERL", linked.target().leg().pathTerminator().identifier()),

        () -> assertEquals(PathTerm.DF, linked.source().leg().type()),
        () -> assertEquals(PathTerm.DF, linked.source().leg().type())
    );

    element = new FixElement(fix, "/");
    LinkedLegs linked2 = element.legs().iterator().next();

    assertAll(
        () -> assertEquals(PathTerm.IF, linked2.source().leg().type()),
        () -> assertEquals(PathTerm.IF, linked2.target().leg().type())
    );
  }

  @Test
  public void testLatLonElement() {
    String latLonId = "5300N/14000W";
    LatLonElement element = LatLonElement.from(latLonId, "");

    assertEquals(element.legs().size(), 1);

    LinkedLegs linked = element.legs().get(0);

    assertAll(
        () -> assertEquals(linked.source().leg().pathTerminator().latLong(), LatLong.of(53.0, -140.0)),
        () -> assertEquals(linked.target().leg().pathTerminator().latLong(), LatLong.of(53.0, -140.0)),

        () -> assertEquals(linked.source().leg().pathTerminator().identifier(), "5300N/14000W"),
        () -> assertEquals(linked.target().leg().pathTerminator().identifier(), "5300N/14000W"),

        () -> assertEquals(linked.source().leg().type(), PathTerm.DF),
        () -> assertEquals(linked.source().leg().type(), PathTerm.DF)
    );

    element = LatLonElement.from(latLonId, "/");
    LinkedLegs linked2 = element.legs().iterator().next();

    assertAll(
        () -> assertEquals(PathTerm.IF, linked2.source().leg().type()),
        () -> assertEquals(PathTerm.IF, linked2.target().leg().type())
    );
  }

  @Test
  public void testProcedureElementFilteredReturnsNoLegsIfNoTransitionsPass() {
    ProcedureElement element = new ProcedureElement(singleTransitionProcedureGraph()).setTransitionFilter(transition -> false);
    assertEquals(Collections.emptyList(), element.buildLegs());
  }

  @Test
  public void testProcedureElementSingleTransitionLegIdentifiersAndWeights() {
    ProcedureGraph pg = singleTransitionProcedureGraph();

    ProcedureElement element = new ProcedureElement(pg);

    assertEquals(2, element.legs().size());

    List<LinkedLegs> linked = element.legs();

    assertTrue(allMatch(linked, leg -> leg.linkWeight() == 0.0d));

    LinkedLegs ll1 = linked.get(0);
    LinkedLegs ll2 = linked.get(1);

    assertEquals(ll1.source().leg().pathTerminator().identifier(), "ZZV");
    assertEquals(ll1.target().leg().pathTerminator().identifier(), "ZZY");
    assertEquals(ll2.source().leg(), ll1.target().leg());
    assertEquals(ll2.target().leg().pathTerminator().identifier(), "ZZZ");
  }

  @Test
  public void testProcedureElementSubsequentLegLinkReferences() {
    ProcedureGraph pg = singleTransitionProcedureGraph();

    ProcedureElement element = new ProcedureElement(pg);

    List<LinkedLegs> linked = element.legs();

    Iterators.pairwise(linked, (ll1, ll2) -> {
      assertEquals(ll1.target(), ll2.source(), "The target of the previous leg should be identical to the source of the next.");
      assertEquals(ll1.target().hashCode(), ll2.source().hashCode(), "The target of the previous leg should have the same hashCode as the source of the next");
    });
  }

  private boolean legMatches(List<LinkedLegs> legs, String sourceName, PathTerm sourceType, String targetName, PathTerm targetType) {
    return legs.stream()
        .filter(ll -> ll.source().leg().pathTerminator() != null)
        .filter(ll -> ll.target().leg().pathTerminator() != null)
        .filter(ll -> ll.source().leg().pathTerminator().identifier().equals(sourceName) && ll.target().leg().pathTerminator().identifier().equals(targetName))
        .filter(ll -> ll.source().leg().type().equals(sourceType) && ll.target().leg().type().equals(targetType))
        .findFirst()
        .isPresent();
  }

  // This is really about checking the linking of the various transitions to each-other in a sane way
  @Test
  public void testProcedureElementMultiTransition() {
    ProcedureGraph pg = TestProcedureGraph.nominalGraph();

    ProcedureElement element = new ProcedureElement(pg).setTransitionFilter(transition -> true);

    List<LinkedLegs> linked = element.legs();

    assertAll(
        () -> assertEquals(pg.edgeSet().size(), linked.size()),

        () -> assertTrue(legMatches(linked, "AAA", PathTerm.IF, "BBB", PathTerm.TF)),
        () -> assertTrue(legMatches(linked, "BBB", PathTerm.IF, "CCC", PathTerm.TF)),
        () -> assertTrue(legMatches(linked, "CCC", PathTerm.TF, "CCC", PathTerm.IF)),
        () -> assertTrue(legMatches(linked, "CCC", PathTerm.IF, "DDD", PathTerm.TF)),
        () -> assertTrue(legMatches(linked, "CCC", PathTerm.IF, "EEE", PathTerm.TF))
    );
  }

  @Test
  public void testBearingDistanceTailored() {
    String tailoredId = "HTO354018";
    Pair<Double, Double> bearingAndDistance = TailoredElement.bearingDistance(tailoredId);

    assertEquals(bearingAndDistance.first(), 354.0, 0.01);
    assertEquals(bearingAndDistance.second(), 18.0, 0.01);
  }

  @Test
  public void testTailoredElement() {
    String tailoredId = "HTO354018";
    Fix fix = mock(Fix.class);

    when(fix.identifier()).thenReturn("HTO");
    when(fix.latLong()).thenReturn(LatLong.of(0.0, 0.0));

    MagneticVariation magneticVariation = magneticVariation(-10.0f, -9.0f);
    when(fix.magneticVariation()).thenReturn(magneticVariation);

    TailoredElement element = new TailoredElement(tailoredId, "/", fix);

    assertEquals(element.legs().size(), 1);

    LinkedLegs linked = element.legs().get(0);

    double projLat = 0.288345;
    double projLon = -0.082682;

    assertAll(
        () -> assertEquals(projLat, linked.source().leg().pathTerminator().latLong().latitude(), 0.0001),
        () -> assertEquals(projLon, linked.source().leg().pathTerminator().latLong().longitude(), 0.0001),
        () -> assertEquals(linked.source().leg().pathTerminator().latLong(), linked.target().leg().pathTerminator().latLong()),

        // do we want the first tailored string
        () -> assertEquals("HTO354018", linked.source().leg().pathTerminator().identifier()),
        () -> assertEquals("HTO354018", linked.target().leg().pathTerminator().identifier()),

        () -> assertEquals(PathTerm.IF, linked.source().leg().type()),
        () -> assertEquals(PathTerm.IF, linked.source().leg().type())
    );

    element = new TailoredElement(tailoredId, "", fix);
    LinkedLegs linked2 = element.legs().iterator().next();

    assertAll(
        () -> assertEquals(PathTerm.DF, linked2.source().leg().type()),
        () -> assertEquals(PathTerm.DF, linked2.target().leg().type())
    );
  }
}
