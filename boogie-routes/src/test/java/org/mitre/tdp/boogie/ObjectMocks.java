package org.mitre.tdp.boogie;

import java.util.List;

import org.mitre.caasd.commons.LatLong;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Class containing helper code for generating mock objects.
 */
public class ObjectMocks {

  public static Leg TF(String name, double lat, double lon) {
    return leg(name, lat, lon, LegType.TF);
  }

  public static Leg IF(String name, double lat, double lon) {
    return leg(name, lat, lon, LegType.IF);
  }

  public static  Leg leg(String name, double lat, double lon, LegType type) {
    Fix term = mock(Fix.class);
    when(term.identifier()).thenReturn(name);
    when(term.latLong()).thenReturn(LatLong.of(lat, lon));

    Leg leg = mock(Leg.class);
    when(leg.type()).thenReturn(type);
    when(leg.pathTerminator()).thenReturn(term);

    return leg;
  }

  public static  Transition transition(String pname, TransitionType ttype, ProcedureType ptype, List<Leg> legs) {
    Transition transition = mock(Transition.class);

    when(transition.legs()).thenReturn(legs);
    when(transition.procedure()).thenReturn(pname);
    when(transition.procedureType()).thenReturn(ptype);
    when(transition.transitionType()).thenReturn(ttype);

    return transition;
  }
}
