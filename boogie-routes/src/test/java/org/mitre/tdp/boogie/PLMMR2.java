package org.mitre.tdp.boogie;

import static org.mitre.tdp.boogie.MockObjects.CF;
import static org.mitre.tdp.boogie.MockObjects.DF;
import static org.mitre.tdp.boogie.MockObjects.IF;
import static org.mitre.tdp.boogie.MockObjects.TF;
import static org.mitre.tdp.boogie.MockObjects.VA;
import static org.mitre.tdp.boogie.MockObjects.VI;
import static org.mitre.tdp.boogie.MockObjects.transition;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableMap;

public final class PLMMR2 implements Procedure {

  public static final PLMMR2 INSTANCE = new PLMMR2();

  private final Map<String, Transition> transitions;

  private PLMMR2() {
    Map<String, Transition> map = Stream.of(RW08L(), RW08R(), RW09B(), RW10(), RW26L(), RW26R(), RW27L(), RW27R(), RW28(), SPA())
        .collect(Collectors.toMap(t -> t.transitionIdentifier().orElse(null), Function.identity()));
    this.transitions = ImmutableMap.copyOf(map);
  }

  @Override
  public String procedureIdentifier() {
    return "PLMMR2";
  }

  @Override
  public String airportIdentifier() {
    return "KATL";
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
  public Collection<Transition> transitions() {
    return transitions.values();
  }

  public Leg get(String fname, String tname) {
    Transition transition = get(tname);
    return transition.legs().stream().filter(l -> l.associatedFix().isPresent()).filter(l -> fname.equals(l.associatedFix().get().fixIdentifier())).findFirst().orElse(null);
  }

  public Transition get(String tname) {
    return this.transitions.get(tname);
  }

  private static Transition RW28() {
    Leg VI = VI();
    Leg WLSON = CF("WLSON", 33.573975000000004, -84.58462499999999);
    Leg ZALLE = TF("ZALLE", 33.45709166666667, -84.58449444444445);
    Leg GGOLF = TF("GGOLF", 33.45582777777778, -84.33613333333332);
    Leg PLMMR = TF("PLMMR", 33.952822222222224, -83.72700555555556);
    return transition("RW28", "PLMMR2", "KATL", TransitionType.RUNWAY, ProcedureType.SID,
        Arrays.asList(VI, WLSON, ZALLE, GGOLF, PLMMR));
  }

  private static Transition RW10() {
    Leg VI = VI();
    Leg GRITZ = CF("GRITZ", 33.60214444444445, -84.2763361111111);
    Leg DDUBB = TF("DDUBB", 33.567025, -84.08201388888888);
    Leg PLMMR = TF("PLMMR", 33.952822222222224, -83.72700555555556);
    return transition("RW10", "PLMMR2", "KATL", TransitionType.RUNWAY, ProcedureType.SID,
        Arrays.asList(VI, GRITZ, DDUBB, PLMMR));
  }

  private static Transition RW09B() {
    Leg VA = VA();
    Leg LIDAS = DF("LIDAS", 33.63363611111111, -84.26920277777778);
    Leg ERWIN = TF("ERWIN", 33.634166666666665, -84.10055555555554);
    Leg PLMMR = TF("PLMMR", 33.952822222222224, -83.72700555555556);
    return transition("RW09B", "PLMMR2", "KATL", TransitionType.RUNWAY, ProcedureType.SID,
        Arrays.asList(VA, LIDAS, ERWIN, PLMMR));
  }

  private static Transition SPA() {
    Leg PLMMR = IF("PLMMR", 33.952822222222224, -83.72700555555556);
    Leg TENSE = TF("TENSE", 34.30863055555555, -83.14446666666667);
    Leg NWANT = TF("NWANT", 34.66439722222222, -82.55944722222222);
    Leg SPA = TF("SPA", 35.033625, -81.92701111111111);
    return transition("SPA", "PLMMR2", "KATL", TransitionType.ENROUTE, ProcedureType.SID,
        Arrays.asList(PLMMR, TENSE, NWANT, SPA));
  }

  private static Transition RW27L() {
    Leg VI = VI();
    Leg FUTBL = CF("FUTBL", 33.58815555555556, -84.58463888888889);
    Leg ZALLE = TF("ZALLE", 33.45709166666667, -84.58449444444445);
    Leg GGOLF = TF("GGOLF", 33.45582777777778, -84.33613333333332);
    Leg PLMMR = TF("PLMMR", 33.952822222222224, -83.72700555555556);
    return transition("RW27L", "PLMMR2", "KATL", TransitionType.RUNWAY, ProcedureType.SID,
        Arrays.asList(VI, FUTBL, ZALLE, GGOLF, PLMMR));
  }

  private static Transition RW08L() {
    Leg VI = VI();
    Leg RONII = CF("RONII", 33.684866666666665, -84.17909166666666);
    Leg IGEBE = TF("IGEBE", 33.73305, -84.0372388888889);
    Leg PLMMR = TF("PLMMR", 33.952822222222224, -83.72700555555556);
    return transition("RW08L", "PLMMR2", "KATL", TransitionType.RUNWAY, ProcedureType.SID,
        Arrays.asList(VI, RONII, IGEBE, PLMMR));
  }

  private static Transition RW26R() {
    Leg VI = VI();
    Leg MPASS = CF("MPASS", 33.693038888888886, -84.58203055555555);
    Leg ZELAN = TF("ZELAN", 33.811277777777775, -84.58069444444445);
    Leg SMOGG = TF("SMOGG", 33.81305277777778, -84.34001111111111);
    Leg DLLNN = TF("DLLNN", 33.89196666666667, -83.90228055555556);
    Leg PLMMR = TF("PLMMR", 33.952822222222224, -83.72700555555556);
    return transition("RW26R", "PLMMR2", "KATL", TransitionType.RUNWAY, ProcedureType.SID,
        Arrays.asList(VI, MPASS, ZELAN, SMOGG, DLLNN, PLMMR));
  }

  private static Transition RW27R() {
    Leg VI = VI();
    Leg FUTBL = CF("FUTBL", 33.58815555555556, -84.58463888888889);
    Leg ZALLE = TF("ZALLE", 33.45709166666667, -84.58449444444445);
    Leg GGOLF = TF("GGOLF", 33.45582777777778, -84.33613333333332);
    Leg PLMMR = TF("PLMMR", 33.952822222222224, -83.72700555555556);
    return transition("RW27R", "PLMMR2", "KATL", TransitionType.RUNWAY, ProcedureType.SID,
        Arrays.asList(VI, FUTBL, ZALLE, GGOLF, PLMMR));
  }

  private static Transition RW26L() {
    Leg VI = VI();
    Leg MPASS = CF("MPASS", 33.693038888888886, -84.58203055555555);
    Leg ZELAN = TF("ZELAN", 33.811277777777775, -84.58069444444445);
    Leg SMOGG = TF("SMOGG", 33.81305277777778, -84.34001111111111);
    Leg DLLNN = TF("DLLNN", 33.89196666666667, -83.90228055555556);
    Leg PLMMR = TF("PLMMR", 33.952822222222224, -83.72700555555556);
    return transition("RW26L", "PLMMR2", "KATL", TransitionType.RUNWAY, ProcedureType.SID,
        Arrays.asList(VI, MPASS, ZELAN, SMOGG, DLLNN, PLMMR));
  }

  private static Transition RW08R() {
    Leg VI = VI();
    Leg RONII = CF("RONII", 33.684866666666665, -84.17909166666666);
    Leg IGEBE = TF("IGEBE", 33.73305, -84.0372388888889);
    Leg PLMMR = TF("PLMMR", 33.952822222222224, -83.72700555555556);
    return transition("RW08R", "PLMMR2", "KATL", TransitionType.RUNWAY, ProcedureType.SID,
        Arrays.asList(VI, RONII, IGEBE, PLMMR));
  }
}
