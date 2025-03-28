package org.mitre.tdp.boogie;

import static org.mitre.tdp.boogie.MockObjects.FM;
import static org.mitre.tdp.boogie.MockObjects.IF;
import static org.mitre.tdp.boogie.MockObjects.TF;
import static org.mitre.tdp.boogie.MockObjects.transition;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class COSTR3_DUP implements Procedure {
  public static final COSTR3_DUP INSTANCE = new COSTR3_DUP();
  private final List<Transition> transitions;

  private COSTR3_DUP() {
    this.transitions = List.of(ALL(), LBV(), LBV2(), SIMMR(), RSW(), BOXKR(), PIE());
  }

  @Override
  public String procedureIdentifier() {
    return "COSTR3";
  }

  @Override
  public String airportIdentifier() {
    return "KMCO";
  }

  @Override
  public ProcedureType procedureType() {
    return ProcedureType.STAR;
  }

  @Override
  public RequiredNavigationEquipage requiredNavigationEquipage() {
    return RequiredNavigationEquipage.RNAV;
  }

  @Override
  public Collection<Transition> transitions() {
    return transitions;
  }

  @Override
  public void accept(Visitor visitor) {
  }

  private static Transition ALL() {
    Leg COSTR = IF("COSTR", 28.111158333333336, -81.75830555555555);
    Leg BIGGR = TF("BIGGR", 28.241666666666667, -81.54944444444445);
    Leg TINKR = TF("TINKR", 28.323055555555555, -81.42416666666666);
    Leg KRAKN = TF("KRAKN", 28.393055555555556, -81.42416666666666);
    Leg TWONA = TF("TWONA", 28.603611111111114, -81.42416666666666);
    Leg KNUKL = TF("KNUKL", 28.747777777777777, -81.42416666666666);
    Leg KNUKL2 = FM("KNUKL", 28.747777777777777, -81.42416666666666);
    return Transition.builder()
        .transitionIdentifier("ALL")
        .transitionType(TransitionType.COMMON)
        .categoryOrTypes(Set.of(CategoryOrType.NOT_SPECIFIED))
        .legs(Arrays.asList(COSTR, BIGGR, TINKR, KRAKN, TWONA, KNUKL, KNUKL2))
        .build();
  }

  private static Transition LBV() {
    Leg LBV = IF("LBV", 26.828186111111112, -81.3914388888889);
    Leg DOWNN = TF("DOWNN", 27.510766666666665, -81.75657777777778);
    Leg MOANS = TF("MOANS", 27.913880555555554, -81.74858055555556);
    Leg COSTR = TF("COSTR", 28.111158333333336, -81.75830555555555);
    List<Leg> legs = List.of(LBV, DOWNN, MOANS, COSTR);
    Set<CategoryOrType> types = Set.of(CategoryOrType.JET, CategoryOrType.TURBOJET);
    return Transition.builder()
        .transitionIdentifier("LBV")
        .transitionType(TransitionType.ENROUTE)
        .categoryOrTypes(types)
        .legs(legs)
        .build();
  }

  //yes its the same path at least from our perspective here (one is lower, who cares)
  private static Transition LBV2() {
    Leg LBV = IF("LBV", 26.828186111111112, -81.3914388888889);
    Leg DOWNN = TF("DOWNN", 27.510766666666665, -81.75657777777778);
    Leg MOANS = TF("MOANS", 27.913880555555554, -81.74858055555556);
    Leg COSTR = TF("COSTR", 28.111158333333336, -81.75830555555555);
    List<Leg> legs = List.of(LBV, DOWNN, MOANS, COSTR);
    Set<CategoryOrType> types = Set.of(CategoryOrType.PROP, CategoryOrType.PISTON);
    return Transition.builder()
        .transitionIdentifier("LBV")
        .transitionType(TransitionType.ENROUTE)
        .categoryOrTypes(types)
        .legs(legs)
        .build();
  }

  private static Transition SIMMR() {
    Leg SIMMR = IF("SIMMR", 27.78391666666667, -84.20438888888889);
    Leg SYKES = TF("SYKES", 27.66471111111111, -83.04978333333334);
    Leg GUMMY = TF("GUMMY", 27.826502777777776, -82.53235277777777);
    Leg LAL = TF("LAL", 27.986197222222224, -82.01390555555555);
    Leg ANDRO = TF("ANDRO", 28.04918055555556, -81.88568611111111);
    Leg SETME = TF("SETME", 28.07111111111111, -81.84055555555555);
    Leg COSTR = TF("COSTR", 28.111158333333336, -81.75830555555555);
    return transition("SIMMR", "COSTR3", "KMCO", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(SIMMR, SYKES, GUMMY, LAL, ANDRO, SETME, COSTR));
  }

  private static Transition RSW() {
    Leg RSW = IF("RSW", 26.529875, -81.77576666666667);
    Leg DOWNN = TF("DOWNN", 27.510766666666665, -81.75657777777778);
    Leg MOANS = TF("MOANS", 27.913880555555554, -81.74858055555556);
    Leg COSTR = TF("COSTR", 28.111158333333336, -81.75830555555555);
    return transition("RSW", "COSTR3", "KMCO", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(RSW, DOWNN, MOANS, COSTR));
  }

  private static Transition BOXKR() {
    Leg BOXKR = IF("BOXKR", 27.585991666666665, -83.2987888888889);
    Leg SYKES = TF("SYKES", 27.66471111111111, -83.04978333333334);
    Leg GUMMY = TF("GUMMY", 27.826502777777776, -82.53235277777777);
    Leg LAL = TF("LAL", 27.986197222222224, -82.01390555555555);
    Leg ANDRO = TF("ANDRO", 28.04918055555556, -81.88568611111111);
    Leg SETME = TF("SETME", 28.07111111111111, -81.84055555555555);
    Leg COSTR = TF("COSTR", 28.111158333333336, -81.75830555555555);
    return transition("BOXKR", "COSTR3", "KMCO", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(BOXKR, SYKES, GUMMY, LAL, ANDRO, SETME, COSTR));
  }

  private static Transition PIE() {
    Leg PIE = IF("PIE", 27.907763888888887, -82.68430833333333);
    Leg GUMMY = TF("GUMMY", 27.826502777777776, -82.53235277777777);
    Leg LAL = TF("LAL", 27.986197222222224, -82.01390555555555);
    Leg ANDRO = TF("ANDRO", 28.04918055555556, -81.88568611111111);
    Leg SETME = TF("SETME", 28.07111111111111, -81.84055555555555);
    Leg COSTR = TF("COSTR", 28.111158333333336, -81.75830555555555);
    return transition("PIE", "COSTR3", "KMCO", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(PIE, GUMMY, LAL, ANDRO, SETME, COSTR));
  }
}
