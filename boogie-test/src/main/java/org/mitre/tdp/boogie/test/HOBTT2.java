package org.mitre.tdp.boogie.test;

import static org.mitre.tdp.boogie.test.MockObjects.FM;
import static org.mitre.tdp.boogie.test.MockObjects.IF;
import static org.mitre.tdp.boogie.test.MockObjects.TF;
import static org.mitre.tdp.boogie.test.MockObjects.transition;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;

/**
 * A mocked copy of the HOBTT2 Procedure at KATL from cycle 1913 for use in testing.
 */
public final class HOBTT2 {

  private final Map<String, Transition> transitions;

  private HOBTT2(Map<String, Transition> transitions) {
    this.transitions = transitions;
  }

  public static HOBTT2 build() {
    Map<String, Transition> tmap = Stream.of(
        DRSDN(), KHMYA(), COOUP(), BEORN(), FRDDO(), ENNTT(), ORRKK(),
        GOLLM(), STRDR(), SHYRE(), COMMON(), RW26B(), RW27B(), RW28())
        .collect(Collectors.toMap(Transition::identifier, Function.identity()));
    return new HOBTT2(tmap);
  }

  private static Transition DRSDN() {
    Leg DRSDN = IF("DRSDN", 33.06475, -86.183083);
    Leg SMAWG = TF("SMAWG", 33.02375833333333, -85.22195555555555);
    Leg HOBTT = TF("HOBTT", 33.179288888888884, -85.02036111111111);
    return transition("DRSDN", "HOBTT2", "KATL", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(DRSDN, SMAWG, HOBTT));
  }

  private static Transition KHMYA() {
    Leg KHMYA = IF("KHMYA", 32.33565277777778, -87.04967777777777);
    Leg CNDLR = TF("CNDLR", 32.60141388888889, -86.35531666666667);
    Leg FNLEY = TF("FNLEY", 32.70103888888889, -86.09133055555554);
    Leg SMAWG = TF("SMAWG", 33.02375833333333, -85.22195555555555);
    Leg HOBTT = TF("HOBTT", 33.179288888888884, -85.02036111111111);
    return transition("KHMYA", "HOBTT2", "KATL", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(KHMYA, CNDLR, FNLEY, SMAWG, HOBTT));
  }

  private static Transition COOUP() {
    Leg COOUP = IF("COOUP", 31.855105555555557, -88.27097499999999);
    Leg KHMYA = TF("KHMYA", 32.33565277777778, -87.04967777777777);
    Leg CNDLR = TF("CNDLR", 32.60141388888889, -86.35531666666667);
    Leg FNLEY = TF("FNLEY", 32.70103888888889, -86.09133055555554);
    Leg SMAWG = TF("SMAWG", 33.02375833333333, -85.22195555555555);
    Leg HOBTT = TF("HOBTT", 33.179288888888884, -85.02036111111111);
    return transition("COOUP", "HOBTT2", "KATL", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(COOUP, KHMYA, CNDLR, FNLEY, SMAWG, HOBTT));
  }

  private static Transition BEORN() {
    Leg BEORN = IF("BEORN", 31.918355555555557, -84.83196111111111);
    Leg STRDR = TF("STRDR", 32.22058888888889, -84.97675277777778);
    Leg GIMLY = TF("GIMLY", 32.50375555555556, -85.1132361111111);
    Leg AZZOG = TF("AZZOG", 32.78584444444444, -85.25019722222223);
    Leg SMAWG = TF("SMAWG", 33.02375833333333, -85.22195555555555);
    Leg HOBTT = TF("HOBTT", 33.179288888888884, -85.02036111111111);
    return transition("BEORN", "HOBTT2", "KATL", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(BEORN, STRDR, GIMLY, AZZOG, SMAWG, HOBTT));
  }

  private static Transition FRDDO() {
    Leg FRDDO = IF("FRDDO", 32.321758333333335, -86.44720555555556);
    Leg BLLBO = TF("BLLBO", 32.42866111111111, -86.26363611111111);
    Leg BGGNS = TF("BGGNS", 32.575497222222225, -86.00875);
    Leg SMAWG = TF("SMAWG", 33.02375833333333, -85.22195555555555);
    Leg HOBTT = TF("HOBTT", 33.179288888888884, -85.02036111111111);
    return transition("FRDDO", "HOBTT2", "KATL", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(FRDDO, BLLBO, BGGNS, SMAWG, HOBTT));
  }

  private static Transition ENNTT() {
    Leg ENNTT = IF("ENNTT", 31.689280555555555, -86.31729722222222);
    Leg GONDR = TF("GONDR", 32.367216666666664, -85.76585555555556);
    Leg SMAWG = TF("SMAWG", 33.02375833333333, -85.22195555555555);
    Leg HOBTT = TF("HOBTT", 33.179288888888884, -85.02036111111111);
    return transition("ENNTT", "HOBTT2", "KATL", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(ENNTT, GONDR, SMAWG, HOBTT));
  }

  private static Transition ORRKK() {
    Leg ORRKK = IF("ORRKK", 32.53959722222222, -87.95693055555556);
    Leg MORDR = TF("MORDR", 32.752541666666666, -86.79886666666667);
    Leg RAETH = TF("RAETH", 32.818400000000004, -86.42725);
    Leg NZGUL = TF("NZGUL", 32.866055555555555, -86.15402222222222);
    Leg SMAWG = TF("SMAWG", 33.02375833333333, -85.22195555555555);
    Leg HOBTT = TF("HOBTT", 33.179288888888884, -85.02036111111111);
    return transition("ORRKK", "HOBTT2", "KATL", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(ORRKK, MORDR, RAETH, NZGUL, SMAWG, HOBTT));
  }

  private static Transition GOLLM() {
    Leg GOLLM = IF("GOLLM", 31.85336666666667, -85.37298888888888);
    Leg ROHUN = TF("ROHUN", 32.22695277777778, -85.32518611111111);
    Leg AZZOG = TF("AZZOG", 32.78584444444444, -85.25019722222223);
    Leg SMAWG = TF("SMAWG", 33.02375833333333, -85.22195555555555);
    Leg HOBTT = TF("HOBTT", 33.179288888888884, -85.02036111111111);
    return transition("GOLLM", "HOBTT2", "KATL", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(GOLLM, ROHUN, AZZOG, SMAWG, HOBTT));
  }

  private static Transition STRDR() {
    Leg STRDR = IF("STRDR", 32.22058888888889, -84.97675277777778);
    Leg GIMLY = TF("GIMLY", 32.50375555555556, -85.1132361111111);
    Leg AZZOG = TF("AZZOG", 32.78584444444444, -85.25019722222223);
    Leg SMAWG = TF("SMAWG", 33.02375833333333, -85.22195555555555);
    Leg HOBTT = TF("HOBTT", 33.179288888888884, -85.02036111111111);
    return transition("STRDR", "HOBTT2", "KATL", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(STRDR, GIMLY, AZZOG, SMAWG, HOBTT));
  }

  private static Transition SHYRE() {
    Leg SHYRE = IF("SHYRE", 31.52595277777778, -87.79020555555556);
    Leg FRDDO = TF("FRDDO", 32.321758333333335, -86.44720555555556);
    Leg BLLBO = TF("BLLBO", 32.42866111111111, -86.26363611111111);
    Leg BGGNS = TF("BGGNS", 32.575497222222225, -86.00875);
    Leg SMAWG = TF("SMAWG", 33.02375833333333, -85.22195555555555);
    Leg HOBTT = TF("HOBTT", 33.179288888888884, -85.02036111111111);
    return transition("SHYRE", "HOBTT2", "KATL", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(SHYRE, FRDDO, BLLBO, BGGNS, SMAWG, HOBTT));
  }

  private static Transition COMMON() {
    Leg HOBTT = IF("HOBTT", 33.179288888888884, -85.02036111111111);
    Leg ENSLL = TF("ENSLL", 33.31919166666667, -84.83804444444444);
    return transition("", "HOBTT2", "KATL", TransitionType.COMMON, ProcedureType.STAR,
        Arrays.asList(HOBTT, ENSLL));
  }

  private static Transition RW26B() {
    Leg ENSLL = IF("ENSLL", 33.31919166666667, -84.83804444444444);
    Leg RAIIN = TF("RAIIN", 33.73294722222222, -84.58158055555555);
    Leg KLOWD = TF("KLOWD", 33.73295277777778, -84.43460277777778);
    Leg SWEPT = TF("SWEPT", 33.73293888888889, -84.30531388888889);
    Leg KYMMY = TF("KYMMY", 33.73308055555556, -84.13879722222222);
    Leg KEAVY = TF("KEAVY", 33.733183333333336, -84.038875);
    Leg KEAVY2 = FM("KEAVY", 33.733183333333336, -84.038875);
    return transition("RW26B", "HOBTT2", "KATL", TransitionType.RUNWAY, ProcedureType.STAR,
        Arrays.asList(ENSLL, RAIIN, KLOWD, SWEPT, KYMMY, KEAVY, KEAVY2));
  }

  private static Transition RW27B() {
    Leg ENSLL = IF("ENSLL", 33.31919166666667, -84.83804444444444);
    Leg EAGYL = TF("EAGYL", 33.536791666666666, -84.58458333333333);
    Leg SHURT = TF("SHURT", 33.53684166666667, -84.43048055555556);
    Leg FOGER = TF("FOGER", 33.53689166666666, -84.30732777777777);
    Leg HITTT = TF("HITTT", 33.537025, -84.13477777777778);
    Leg YURII = TF("YURII", 33.53713055555556, -84.03544166666667);
    Leg YURII2 = FM("YURII", 33.53713055555556, -84.03544166666667);
    return transition("RW27B", "HOBTT2", "KATL", TransitionType.RUNWAY, ProcedureType.STAR,
        Arrays.asList(ENSLL, EAGYL, SHURT, FOGER, HITTT, YURII, YURII2));
  }

  private static Transition RW28() {
    Leg ENSLL = IF("ENSLL", 33.31919166666667, -84.83804444444444);
    Leg EAGYL = TF("EAGYL", 33.536791666666666, -84.58458333333333);
    Leg SHURT = TF("SHURT", 33.53684166666667, -84.43048055555556);
    Leg FOGER = TF("FOGER", 33.53689166666666, -84.30732777777777);
    Leg HITTT = TF("HITTT", 33.537025, -84.13477777777778);
    Leg YURII = TF("YURII", 33.53713055555556, -84.03544166666667);
    Leg YURII2 = FM("YURII", 33.53713055555556, -84.03544166666667);
    return transition("RW28", "HOBTT2", "KATL", TransitionType.RUNWAY, ProcedureType.STAR,
        Arrays.asList(ENSLL, EAGYL, SHURT, FOGER, HITTT, YURII, YURII2));
  }

  public Leg get(String fname, String tname) {
    Transition transition = get(tname);
    return transition.legs().stream().filter(l -> l.pathTerminator() != null).filter(l -> fname.equals(l.pathTerminator().identifier())).findFirst().orElse(null);
  }

  public Transition get(String tname) {
    return this.transitions.get(tname);
  }

  public Collection<Transition> transitions() {
    return transitions.values();
  }
}
