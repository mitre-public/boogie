package org.mitre.tdp.boogie;

import com.google.common.collect.ImmutableMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mitre.tdp.boogie.MockObjects.TF;
import static org.mitre.tdp.boogie.MockObjects.transition;

/**
 * This approach is faked to provide overlap with the PLMMR2 object
 */
public class KATL_FAKED implements Procedure{

  public static Procedure INSTANCE = new KATL_FAKED();

  private final Map<String, Transition> transitions;

  private KATL_FAKED() {
    Map<String, Transition> map = Stream.of(ALL()).collect(Collectors.toMap(t -> t.transitionIdentifier().orElse(null), Function.identity()));
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

  @Override
  public void accept(Visitor visitor) {
  }

  private static Transition ALL() {
    Leg ZALLE = TF("ZALLE", 33.45709166666667, -84.58449444444445);
    Leg GGOLF = TF("GGOLF", 33.45582777777778, -84.33613333333332);
    return transition("ALL", "R27R", "KATL", TransitionType.COMMON, ProcedureType.APPROACH,
        List.of(ZALLE, GGOLF));
  }
}
