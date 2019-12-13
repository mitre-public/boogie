package org.mitre.tdp.boogie;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.util.Declinations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Class containing helper code for generating mock objects.
 */
public class ObjectMocks {

  public static Fix fix(String name, double lat, double lon) {
    Fix fix = mock(Fix.class);
    when(fix.identifier()).thenReturn(name);
    when(fix.latLong()).thenReturn(LatLong.of(lat, lon));
    when(fix.magneticVariation()).thenReturn(new MagneticVariation() {
      @Override
      public Optional<Float> published() {
        return Optional.empty();
      }

      @Override
      public float modeled() {
        return (float) Declinations.declination(lat, lon, Optional.empty(), Instant.parse("2019-01-01T00:00:00.00Z"));
      }
    });
    when(fix.toString()).thenReturn("Name: " + name);
    return fix;
  }

  public static Leg TF(String name, double lat, double lon) {
    return leg(name, lat, lon, LegType.TF);
  }

  public static Leg IF(String name, double lat, double lon) {
    return leg(name, lat, lon, LegType.IF);
  }

  public static Leg DF(String name, double lat, double lon) {
    return leg(name, lat, lon, LegType.DF);
  }

  public static Leg CF(String name, double lat, double lon) {
    return leg(name, lat, lon, LegType.CF);
  }

  public static Leg FM(String name, double lat, double lon) {
    return leg(name, lat, lon, LegType.FM);
  }

  public static Leg CA() {
    return nonConcreteLeg(LegType.CA);
  }

  public static Leg VA() {
    return nonConcreteLeg(LegType.VA);
  }

  public static Leg VI() {
    return nonConcreteLeg(LegType.VI);
  }

  public static Leg VM() {
    return nonConcreteLeg(LegType.VM);
  }

  public static Leg nonConcreteLeg(LegType type) {
    Leg leg = mock(Leg.class);
    when(leg.type()).thenReturn(type);
    return leg;
  }

  public static Leg leg(String name, double lat, double lon, LegType type) {
    Fix term = fix(name, lat, lon);
    Leg leg = mock(Leg.class);
    when(leg.type()).thenReturn(type);
    when(leg.pathTerminator()).thenReturn(term);

    when(leg.toString()).thenReturn("Path Terminator: " + name);
    return leg;
  }

  public static Transition transition(String pname, TransitionType ttype, ProcedureType ptype, List<Leg> legs) {
    return transition(null, pname, "FOO", ttype, ptype, legs);
  }

  public static Transition transition(String tname, String pname, String aname, TransitionType ttype, ProcedureType ptype, List<Leg> legs) {
    Transition transition = mock(Transition.class);

    when(transition.identifier()).thenReturn(tname);
    when(transition.airport()).thenReturn(aname);
    when(transition.legs()).thenReturn(legs);
    when(transition.procedure()).thenReturn(pname);
    when(transition.procedureType()).thenReturn(ptype);
    when(transition.transitionType()).thenReturn(ttype);
    when(transition.source()).thenReturn(NavigationSource.FUSED);
    when(transition.toString()).thenReturn("Transition: " + (tname == null ? "common" : tname) + " Procedure: " + pname);

    return transition;
  }

  public static MagneticVariation magneticVariation(float published, float modeled) {
    MagneticVariation variation = mock(MagneticVariation.class);
    when(variation.published()).thenReturn(Optional.of(published));
    when(variation.modeled()).thenReturn(modeled);
    return variation;
  }

  public static Airport airport(String name, double lat, double lon) {
    Airport airport = mock(Airport.class);
    when(airport.identifier()).thenReturn(name);
    when(airport.latLong()).thenReturn(LatLong.of(lat, lon));
    return airport;
  }

  public static Airway airway(String name, List<Leg> legs) {
    Airway airway = mock(Airway.class);
    when(airway.identifier()).thenReturn(name);
    when(airway.legs()).thenReturn(legs);
    return airway;
  }
}
