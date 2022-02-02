package org.mitre.tdp.boogie;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.model.BoogieLeg;
import org.mitre.tdp.boogie.util.Declinations;


/**
 * Class containing helper code for generating mock objects.
 */
public final class MockObjects {

  public static Fix fix(String name, double lat, double lon) {
    Fix fix = spy(Fix.class);
    when(fix.fixIdentifier()).thenReturn(name);
    when(fix.fixRegion()).thenReturn("MOCK");
    when(fix.latLong()).thenReturn(LatLong.of(lat, lon));
    when(fix.latitude()).thenCallRealMethod();
    when(fix.longitude()).thenCallRealMethod();
    when(fix.publishedVariation()).thenReturn(Optional.empty());
    when(fix.modeledVariation()).thenReturn(Declinations.declination(lat, lon, null, Instant.parse("2019-01-01T00:00:00.00Z")));
    when(fix.toString()).thenReturn("Name: " + name);
    when(fix.projectOut(any(), any())).thenCallRealMethod();
    return fix;
  }

  public static Leg leg(String name, double lat, double lon, PathTerminator type) {
    Fix term = fix(name, lat, lon);
    BoogieLeg leg = mock(BoogieLeg.class);
    when(leg.pathTerminator()).thenReturn(type);
    when(leg.associatedFix()).thenReturn((Optional) Optional.of(term));
    when(leg.toString()).thenReturn("Associated Fix: " + name + " - " + type.name());
    when(leg.toBuilder()).thenReturn(new BoogieLeg.Builder().associatedFix(term));
    return leg;
  }

  public static Leg TF(String name, double lat, double lon) {
    return leg(name, lat, lon, PathTerminator.TF);
  }

  public static Leg IF(String name, double lat, double lon) {
    return leg(name, lat, lon, PathTerminator.IF);
  }

  public static Leg DF(String name, double lat, double lon) {
    return leg(name, lat, lon, PathTerminator.DF);
  }

  public static Leg CF(String name, double lat, double lon) {
    return leg(name, lat, lon, PathTerminator.CF);
  }

  public static Leg FM(String name, double lat, double lon) {
    return leg(name, lat, lon, PathTerminator.FM);
  }

  public static Leg HM(String name, double lat, double lon) {
    return leg(name, lat, lon, PathTerminator.HM);
  }

  public static Leg CA() {
    return nonConcreteLeg(PathTerminator.CA);
  }

  public static Leg VA() {
    return nonConcreteLeg(PathTerminator.VA);
  }

  public static Leg VI() {
    return nonConcreteLeg(PathTerminator.VI);
  }

  public static Leg VM() {
    return nonConcreteLeg(PathTerminator.VM);
  }

  public static Leg nonConcreteLeg(PathTerminator type) {
    Leg leg = mock(Leg.class);
    when(leg.pathTerminator()).thenReturn(type);
    return leg;
  }

  public static Transition transition(String pname, TransitionType ttype, ProcedureType ptype, List<Leg> legs) {
    return transition(null, pname, "FOO", ttype, ptype, legs);
  }

  public static Transition transition(String pname, String aname, TransitionType ttype, ProcedureType ptype, List<Leg> legs) {
    return transition(null, pname, aname, ttype, ptype, legs);
  }

  public static Transition transition(String tname, String pname, String aname, TransitionType ttype, ProcedureType ptype, List<? extends Leg> legs) {
    Transition transition = mock(Transition.class);

    when(transition.transitionIdentifier()).thenReturn(Optional.ofNullable(tname));
    when(transition.airportIdentifier()).thenReturn(aname);
    when(transition.airportRegion()).thenReturn("MOCK");
    when(transition.legs()).thenReturn((List) legs);
    when(transition.procedureIdentifier()).thenReturn(pname);
    when(transition.procedureType()).thenReturn(ptype);
    when(transition.transitionType()).thenReturn(ttype);
    when(transition.toString()).thenReturn("Transition: " + (tname == null ? "common" : tname) + " Procedure: " + pname);

    return transition;
  }

  public static Airport airport(String name, double lat, double lon) {
    Airport airport = spy(Airport.class);
    when(airport.airportIdentifier()).thenReturn(name);
    when(airport.latLong()).thenReturn(LatLong.of(lat, lon));
    return airport;
  }

  public static Airway airway(String name, List<? extends Leg> legs) {
    Airway airway = mock(Airway.class);
    when(airway.airwayIdentifier()).thenReturn(name);
    when(airway.legs()).thenReturn((List) legs);
    return airway;
  }
}
