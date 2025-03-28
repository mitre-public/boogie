package org.mitre.tdp.boogie;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.mitre.caasd.commons.LatLong;

import com.google.common.collect.Range;

public class CHA1C_WSSS_PARTIAL implements Procedure{
  private static final List<Transition> transitions = List.of(RW02(), RW03(), ALL(), ANITO());
  public static final CHA1C_WSSS_PARTIAL INSTANCE = new CHA1C_WSSS_PARTIAL();
  private CHA1C_WSSS_PARTIAL() {}
  @Override
  public String procedureIdentifier() {
    return "CHA1C";
  }

  @Override
  public String airportIdentifier() {
    return "WSSS";
  }

  @Override
  public ProcedureType procedureType() {
    return ProcedureType.SID;
  }

  @Override
  public RequiredNavigationEquipage requiredNavigationEquipage() {
    return RequiredNavigationEquipage.RNAV;
  }

  @Override
  public Collection<? extends Transition> transitions() {
    return transitions;
  }

  @Override
  public void accept(Visitor visitor) {
    throw new UnsupportedOperationException("Not supported in test.");
  }

  private static Transition ANITO() {
    Leg IF = MockObjects.IF("WSSS", 1.35019, 103.994003);
    Leg TF = MockObjects.TF("DOSNO", 0.7992, 104.2358);
    Leg TF2 = MockObjects.TF("ANITO", -0.2833, 104.8667);
    List<Leg> legs = List.of(IF, TF, TF2);
    return Transition.builder()
        .transitionType(TransitionType.ENROUTE)
        .categoryOrTypes(Set.of())
        .transitionIdentifier("ANITO")
        .legs(legs)
        .build();
  }

  private static Transition ALL() {
    Leg IF = MockObjects.IF("WSSS", 1.35019, 103.994003);
    List<Leg> legs = List.of(IF);
    return Transition.builder()
        .legs(legs)
        .transitionType(TransitionType.COMMON)
        .categoryOrTypes(Set.of())
        .transitionIdentifier("ALL")
        .build();
  }

  private static Transition RW02() {
    Range<Double> alt = Range.atLeast(422.0);
    Leg VA = Leg.builder(PathTerminator.VA, 0).outboundMagneticCourse(230.0).altitudeConstraint(alt).build();
    Leg VM = Leg.builder(PathTerminator.VM, 1).outboundMagneticCourse(230.0).build();
    List<Leg> legs = List.of(VA, VM);
    return Transition.builder()
        .legs(legs)
        .categoryOrTypes(Set.of())
        .transitionIdentifier("RW02")
        .transitionType(TransitionType.RUNWAY)
        .build();
  }

  private static Transition RW03() {
    Range<Double> alt = Range.atLeast(422.0);
    Fix wsss = Fix.builder().fixIdentifier("WSSS").latLong(LatLong.of(1.35019, 103.994003)).build();//not realistic but ok for expansion
    Leg VA = Leg.builder(PathTerminator.VA, 0).outboundMagneticCourse(230.0).altitudeConstraint(alt).build();
    Leg FM = Leg.builder(PathTerminator.FM, 1)
        .outboundMagneticCourse(230.0)
        .associatedFix(wsss)
        .build();
    List<Leg> legs = List.of(VA, FM);
    return Transition.builder()
        .legs(legs)
        .categoryOrTypes(Set.of())
        .transitionIdentifier("RW03")
        .transitionType(TransitionType.RUNWAY)
        .build();
  }
}
