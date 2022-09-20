package org.mitre.tdp.boogie;

import com.google.common.collect.ImmutableMap;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mitre.tdp.boogie.MockObjects.TF;
import static org.mitre.tdp.boogie.MockObjects.transition;

/**
 * This is a faked star to provide overlap with the PLMMR2 object
 */
public class STAR_FAKE implements Procedure {

  public static final STAR_FAKE INSTANCE = new STAR_FAKE();

  private final Map<String, Transition> transitions;

  private STAR_FAKE() {
    Map<String, Transition> map = Stream.of(ALL()).collect(Collectors.toMap(t -> t.transitionIdentifier().orElse(null), Function.identity()));
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
  public String airportRegion() {
    return "K7";
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
    return transitions.values();
  }

  private static Transition ALL() {
    Leg ZALLE = TF("ZALLE", 33.45709166666667, -84.58449444444445);
    Leg GGOLF = TF("GGOLF", 33.45582777777778, -84.33613333333332);
    return transition("ALL", "PLMMR2", "KATL", TransitionType.RUNWAY, ProcedureType.SID,
        Arrays.asList(ZALLE, GGOLF));
  }
}
