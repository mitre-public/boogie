package org.mitre.tdp.boogie.alg.resolve.element;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.LegType;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.NavigationSource;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.graph.ProcedureGraph;
import org.mitre.tdp.boogie.models.LinkedLegs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestElements {

  @Test
  public void testAirportElement() {
    Airport airport = mock(Airport.class);

    LatLong airportLocation = LatLong.of(0.0, 0.0);
    String airportId = "KATL";

    when(airport.latLong()).thenReturn(airportLocation);
    when(airport.identifier()).thenReturn(airportId);
    when(airport.source()).thenReturn(NavigationSource.CIFP);

    AirportElement element = new AirportElement(airport);

    assertEquals(element.legs().size(), 1);

    LinkedLegs linked = element.legs().get(0);

    assertEquals(linked.source().leg().pathTerminator().latLong(), airportLocation);
    assertEquals(linked.target().leg().pathTerminator().latLong(), airportLocation);

    assertEquals(linked.source().leg().pathTerminator().identifier(), "KATL");
    assertEquals(linked.target().leg().pathTerminator().identifier(), "KATL");

    assertEquals(linked.source().leg().type(), LegType.TF);
    assertEquals(linked.source().leg().type(), LegType.TF);
  }

  private Leg TF(String name, double lat, double lon) {
    return leg(name, lat, lon, LegType.TF);
  }

  private Leg IF(String name, double lat, double lon) {
    return leg(name, lat, lon, LegType.IF);
  }

  private Leg leg(String name, double lat, double lon, LegType type) {
    Fix term = mock(Fix.class);
    when(term.identifier()).thenReturn(name);
    when(term.latLong()).thenReturn(LatLong.of(lat, lon));

    Leg leg = mock(Leg.class);
    when(leg.type()).thenReturn(type);
    when(leg.pathTerminator()).thenReturn(term);

    return leg;
  }

  @Test
  public void testAirwayElement() {
    Leg l1 = TF("YYT", 0.0, 0.0);
    Leg l2 = TF("YYZ", 0.0, 1.0);
    Leg l3 = TF("ZZT", 0.0, 2.0);
    Leg l4 = TF("ZZY", 0.0, 3.0);

    Airway airway = mock(Airway.class);

    String airwayId = "J121";

    when(airway.legs()).thenReturn(Arrays.asList(l1, l2, l3, l4));
    when(airway.identifier()).thenReturn(airwayId);

    AirwayElement element = new AirwayElement(airway);

    assertEquals(element.legs().size(), 6);

    assertTrue(element.legs().stream().allMatch(l ->
        l.source().leg().type().equals(LegType.TF)
            && l.target().leg().type().equals(LegType.TF)));

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
  public void testFixElement() {
    Fix fix = mock(Fix.class);

    LatLong fixLocation = LatLong.of(0.0, 0.0);
    String fixId = "SHERL";

    when(fix.latLong()).thenReturn(fixLocation);
    when(fix.identifier()).thenReturn(fixId);
    when(fix.source()).thenReturn(NavigationSource.CIFP);

    FixElement element = new FixElement(fix);

    assertEquals(element.legs().size(), 1);

    LinkedLegs linked = element.legs().get(0);

    assertEquals(linked.source().leg().pathTerminator().latLong(), fixLocation);
    assertEquals(linked.target().leg().pathTerminator().latLong(), fixLocation);

    assertEquals(linked.source().leg().pathTerminator().identifier(), "SHERL");
    assertEquals(linked.target().leg().pathTerminator().identifier(), "SHERL");

    assertEquals(linked.source().leg().type(), LegType.TF);
    assertEquals(linked.source().leg().type(), LegType.TF);
  }

  @Test
  public void testLatLonElement() {
    String latLonId = "5300N/14000W";
    LatLonElement element = LatLonElement.from(latLonId);

    assertEquals(element.legs().size(), 1);

    LinkedLegs linked = element.legs().get(0);

    assertEquals(linked.source().leg().pathTerminator().latLong(), LatLong.of(53.0, -140.0));
    assertEquals(linked.target().leg().pathTerminator().latLong(), LatLong.of(53.0, -140.0));

    assertEquals(linked.source().leg().pathTerminator().identifier(), "5300N/14000W");
    assertEquals(linked.target().leg().pathTerminator().identifier(), "5300N/14000W");

    assertEquals(linked.source().leg().type(), LegType.TF);
    assertEquals(linked.source().leg().type(), LegType.TF);
  }

  @Test
  public void testProcedureElementSingleTransition() {
    Leg l1 = IF("ZZV", 0.0, 0.0);
    Leg l2 = TF("ZZY", 0.0, 1.0);
    Leg l3 = TF("ZZZ", 0.0, 2.0);

    Transition transition = mock(Transition.class);
    when(transition.transitionType()).thenReturn(TransitionType.COMMON);
    when(transition.legs()).thenReturn(Arrays.asList(l1, l2, l3));
    when(transition.procedure()).thenReturn("GNDLF1");
    when(transition.procedureType()).thenReturn(ProcedureType.STAR);

    ProcedureGraph pg = ProcedureGraph.from(Collections.singletonList(transition));

    ProcedureElement element = new ProcedureElement(pg);

    assertEquals(2, element.legs().size());

    List<LinkedLegs> linked = element.legs();

    LinkedLegs ll1 = linked.get(0);
    LinkedLegs ll2 = linked.get(1);

    assertEquals(ll1.source().leg().pathTerminator().identifier(), "ZZV");
    assertEquals(ll1.target().leg().pathTerminator().identifier(), "ZZY");
    assertEquals(ll2.source().leg(), ll1.target().leg());
    assertEquals(ll2.target().leg().pathTerminator().identifier(), "ZZZ");
  }

  private Transition transition(String pname, ProcedureType ptype, List<Leg> legs) {
    Transition transition = mock(Transition.class);

    when(transition.legs()).thenReturn(legs);
    when(transition.procedure()).thenReturn(pname);
    when(transition.procedureType()).thenReturn(ptype);
    when(transition.transitionType()).thenReturn(TransitionType.COMMON);

    return transition;
  }

  // This is really about checking the linking of the various transitions to each-other in a sane way
  @Test
  public void testProcedureElementMultiTransition() {
    Leg l1_1 = IF("AAA", 0.0, 0.0);
    Leg l1_2 = TF("BBB", 0.0, 0.1);
    Transition ab = transition("ALPHA1", ProcedureType.STAR, Arrays.asList(l1_1, l1_2));

    Leg l2_1 = IF("BBB", 0.0, 0.2);
    Leg l2_2 = TF("CCC", 0.0, 0.3);
    Transition bc = transition("ALPHA1", ProcedureType.STAR, Arrays.asList(l2_1, l2_2));

    Leg l3_1 = IF("CCC", 0.0, 0.4);
    Leg l3_2 = TF("DDD", 0.0, 0.5);
    Transition cd = transition("ALPHA1", ProcedureType.STAR, Arrays.asList(l3_1, l3_2));

    Leg l4_1 = IF("CCC", 0.0, 0.4);
    Leg l4_2 = TF("EEE", 0.0, 0.5);
    Transition ce = transition("ALPHA1", ProcedureType.STAR, Arrays.asList(l4_1, l4_2));

    ProcedureGraph pg = ProcedureGraph.from(Arrays.asList(ab, bc, cd, ce));

    ProcedureElement element = new ProcedureElement(pg);

    List<LinkedLegs> linked = element.legs();

    assertEquals(7, linked.size());
  }

  @Test
  public void testProcedureElementDisconnected() {

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
    when(fix.magneticVariation()).thenReturn(new MagneticVariation() {
      @Override
      public Optional<Float> published() {
        return Optional.of(-10.0f);
      }

      @Override
      public float modeled() {
        return -9.0f;
      }
    });

    TailoredElement element = new TailoredElement(tailoredId, fix);

    assertEquals(element.legs().size(), 1);

    LinkedLegs linked = element.legs().get(0);

    double projLat = 0.288345;
    double projLon = -0.082682;
    assertEquals(linked.source().leg().pathTerminator().latLong().latitude(), projLat, 0.0001);
    assertEquals(linked.source().leg().pathTerminator().latLong().longitude(), projLon, 0.0001);
    assertEquals(linked.source().leg().pathTerminator().latLong(), linked.target().leg().pathTerminator().latLong());

    // do we want the first tailored string
    assertEquals(linked.source().leg().pathTerminator().identifier(), "HTO");
    assertEquals(linked.target().leg().pathTerminator().identifier(), "HTO");

    assertEquals(linked.source().leg().type(), LegType.TF);
    assertEquals(linked.source().leg().type(), LegType.TF);
  }
}
