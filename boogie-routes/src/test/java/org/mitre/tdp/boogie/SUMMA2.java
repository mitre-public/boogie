package org.mitre.tdp.boogie;

import static org.mitre.tdp.boogie.MockObjects.CF;
import static org.mitre.tdp.boogie.MockObjects.IF;
import static org.mitre.tdp.boogie.MockObjects.TF;
import static org.mitre.tdp.boogie.MockObjects.VI;
import static org.mitre.tdp.boogie.MockObjects.VM;
import static org.mitre.tdp.boogie.MockObjects.transition;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableMap;

public class SUMMA2 implements Procedure {

  public static final SUMMA2 INSTANCE = new SUMMA2();

  private final Map<String, Transition> transitions;

  private SUMMA2() {
    Map<String, Transition> map = Stream.of(RW34C(), RW34L(), RW34R(), RW16L(), RW16C(), RW16R(), LKV(), BKE())
        .collect(Collectors.toMap(t -> t.transitionIdentifier().orElse(null), Function.identity()));
    this.transitions = ImmutableMap.copyOf(map);
  }

  @Override
  public String procedureIdentifier() {
    return "SUMMA2";
  }

  @Override
  public String airportIdentifier() {
    return "KSEA";
  }

  @Override
  public String airportRegion() {
    return "K1";
  }

  @Override
  public ProcedureType procedureType() {
    return ProcedureType.SID;
  }

  @Override
  public RequiredNavigationEquipage requiredNavigationEquipage() {
    return RequiredNavigationEquipage.CONV;
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

  private static Transition RW34C() {
    Leg vi = VI();
    Leg NEZUG = CF("NEZUG", 47.56866666666667, -122.30966666666666);
    Leg vm = VM();
    return transition("RW34C", "SUMMA2", "KSEA", TransitionType.RUNWAY, ProcedureType.SID,
        Arrays.asList(vi, NEZUG, vm));
  }

  private static Transition RW34L() {
    Leg vi = VI();
    Leg NEZUG = CF("NEZUG", 47.56866666666667, -122.30966666666666);
    Leg vm = VM();
    return transition("RW34L", "SUMMA2", "KSEA", TransitionType.RUNWAY, ProcedureType.SID,
        Arrays.asList(vi, NEZUG, vm));
  }

  private static Transition RW16L() {
    Leg vi1 = VI();
    Leg NEVJO = CF("NEVJO", 47.25216666666667, -122.30966666666666);
    Leg vi2 = VI();
    Leg SUMMA = CF("SUMMA", 46.61785833333333, -121.98832222222222);
    return transition("RW16L", "SUMMA2", "KSEA", TransitionType.RUNWAY, ProcedureType.SID,
        Arrays.asList(vi1, NEVJO, vi2, SUMMA));
  }

  private static Transition LKV() {
    Leg SUMMA = IF("SUMMA", 46.61785833333333, -121.98832222222222);
    Leg LKV = TF("LKV", 42.492844444444444, -120.50710555555555);
    return transition("LKV", "SUMMA2", "KSEA", TransitionType.ENROUTE, ProcedureType.SID,
        Arrays.asList(SUMMA, LKV));
  }

  private static Transition RW16C() {
    Leg vi1 = VI();
    Leg NEVJO = CF("NEVJO", 47.25216666666667, -122.30966666666666);
    Leg vi2 = VI();
    Leg SUMMA = CF("SUMMA", 46.61785833333333, -121.98832222222222);
    return transition("RW16C", "SUMMA2", "KSEA", TransitionType.RUNWAY, ProcedureType.SID,
        Arrays.asList(vi1, NEVJO, vi2, SUMMA));
  }

  private static Transition RW34R() {
    Leg vi = VI();
    Leg NEZUG = CF("NEZUG", 47.56866666666667, -122.30966666666666);
    Leg vm = VM();
    return transition("RW34R", "SUMMA2", "KSEA", TransitionType.RUNWAY, ProcedureType.SID,
        Arrays.asList(vi, NEZUG, vm));
  }

  private static Transition BKE() {
    Leg SUMMA = IF("SUMMA", 46.61785833333333, -121.98832222222222);
    Leg BKE = TF("BKE", 44.8406, -117.80787222222222);
    return transition("BKE", "SUMMA2", "KSEA", TransitionType.ENROUTE, ProcedureType.SID,
        Arrays.asList(SUMMA, BKE));
  }

  private static Transition RW16R() {
    Leg vi1 = VI();
    Leg NEVJO = CF("NEVJO", 47.25216666666667, -122.30966666666666);
    Leg vi2 = VI();
    Leg SUMMA = CF("SUMMA", 46.61785833333333, -121.98832222222222);
    return transition("RW16R", "SUMMA2", "KSEA", TransitionType.RUNWAY, ProcedureType.SID,
        Arrays.asList(vi1, NEVJO, vi2, SUMMA));
  }
}
