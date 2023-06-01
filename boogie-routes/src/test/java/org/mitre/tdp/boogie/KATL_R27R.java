package org.mitre.tdp.boogie;

import static org.mitre.tdp.boogie.MockObjects.DF;
import static org.mitre.tdp.boogie.MockObjects.HM;
import static org.mitre.tdp.boogie.MockObjects.IF;
import static org.mitre.tdp.boogie.MockObjects.TF;
import static org.mitre.tdp.boogie.MockObjects.transition;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableMap;

public class KATL_R27R implements Procedure {

  public static Procedure INSTANCE = new KATL_R27R();
  private final Map<String, Transition> transitions;

  private KATL_R27R() {
    Map<String, Transition> map = Stream.of(MMCAP(), YOUYU(), ALL(), MISSED())
        .collect(Collectors.toMap(t -> t.transitionIdentifier().orElse(null), Function.identity()));
    this.transitions = ImmutableMap.copyOf(map);
  }


  @Override
  public String procedureIdentifier() {
    return "R27R";
  }

  @Override
  public String airportIdentifier() {
    return "KATL";
  }

  @Override
  public ProcedureType procedureType() {
    return ProcedureType.APPROACH;
  }

  @Override
  public RequiredNavigationEquipage requiredNavigationEquipage() {
    return RequiredNavigationEquipage.RNAV;
  }

  @Override
  public Collection<? extends Transition> transitions() {
    return transitions.values();
  }

  private static Transition ALL() {
    Leg MRCER = IF("MRCER", 33.6345, -84.2041, 10);
    Leg OSTRR = TF("OSTRR", 33.6346, -84.3045, 20);
    Leg RW27R = TF("RW27R", 33.6347, -84.4089, 30);
    return transition("ALL", "R27R", "KATL", TransitionType.COMMON, ProcedureType.APPROACH,
        List.of(MRCER, OSTRR, RW27R));
  }

  private static Transition MISSED() {
    Leg ROTLE = DF("ROTLE", 33.6347, -84.5327, 40);
    Leg TEMPO = TF("TEMPO", 33.6113, -85.0386, 50);
    Leg TEMPO2 = HM("TEMPO", 33.6113, -85.0386, 60);
    return transition("MISSED", "R27R", "KATL", TransitionType.MISSED, ProcedureType.APPROACH,
        List.of(ROTLE, TEMPO, TEMPO2));
  }

  private static Transition MMCAP() {
    Leg MMCAP = IF("MMCAP", 33.6341, -84.0600, 10);
    Leg MAASN = TF("MAASN", 33.6343, -84.1290, 20);
    Leg MRCER = TF("MRCER", 33.6345, -84.2041, 30);
    return transition("MMCAP", "R27R", "KATL", TransitionType.APPROACH, ProcedureType.APPROACH,
        List.of(MMCAP, MAASN, MRCER));
  }

  private static Transition YOUYU() {
    Leg YOUYU = IF("YOUYU", 33.6339, -83.9911, 10);
    Leg MMCAP = TF("MMCAP", 33.6341, -84.0600, 20);
    Leg MAASN = TF("MAASN", 33.6343, -84.1290, 30);
    Leg MRCER = TF("MRCER", 33.6345, -84.2041, 40);
    return transition("YOUYU", "R27R", "KATL", TransitionType.APPROACH, ProcedureType.APPROACH,
        List.of(YOUYU, MMCAP, MAASN, MRCER));
  }
}
