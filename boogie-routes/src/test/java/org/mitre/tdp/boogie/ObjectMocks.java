package org.mitre.tdp.boogie;

import java.util.List;
import java.util.Optional;

import org.mitre.caasd.commons.LatLong;

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
    return fix;
  }

  public static Leg TF(String name, double lat, double lon) {
    return leg(name, lat, lon, LegType.TF);
  }

  public static Leg IF(String name, double lat, double lon) {
    return leg(name, lat, lon, LegType.IF);
  }


  public static Leg leg(String name, double lat, double lon, LegType type) {
    Fix term = fix(name, lat, lon);
    Leg leg = mock(Leg.class);
    when(leg.type()).thenReturn(type);
    when(leg.pathTerminator()).thenReturn(term);
    return leg;
  }

  public static Transition transition(String pname, TransitionType ttype, ProcedureType ptype, List<Leg> legs) {
    Transition transition = mock(Transition.class);

    when(transition.legs()).thenReturn(legs);
    when(transition.procedure()).thenReturn(pname);
    when(transition.procedureType()).thenReturn(ptype);
    when(transition.transitionType()).thenReturn(ttype);
    when(transition.source()).thenReturn(NavigationSource.FUSED);
    when(transition.airport()).thenReturn("FOO");

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
