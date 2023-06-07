package org.mitre.tdp.boogie;

import static org.mitre.tdp.boogie.MockObjects.CF;
import static org.mitre.tdp.boogie.MockObjects.HM;
import static org.mitre.tdp.boogie.MockObjects.IF;
import static org.mitre.tdp.boogie.MockObjects.transition;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableMap;

public final class KMCO_I17R implements Procedure {

  public static final Procedure I17R = new KMCO_I17R();

  private final Map<String, Transition> transitions;

  private KMCO_I17R() {
    Map<String, Transition> map = Stream.of(ALL(), MISSED(), RATOY())
        .collect(Collectors.toMap(t -> t.transitionIdentifier().orElse(null), Function.identity()));
    this.transitions = ImmutableMap.copyOf(map);
  }

  public Leg get(String fname, String tname) {
    Transition transition = get(tname);
    return transition.legs().stream().filter(l -> l.associatedFix().isPresent()).filter(l -> fname.equals(l.associatedFix().get().fixIdentifier())).findFirst().orElse(null);
  }

  public Transition get(String tname) {
    return this.transitions.get(tname);
  }

  @Override
  public String procedureIdentifier() {
    return "I17R";
  }

  @Override
  public String airportIdentifier() {
    return "KMCO";
  }

  @Override
  public ProcedureType procedureType() {
    return ProcedureType.APPROACH;
  }

  @Override
  public RequiredNavigationEquipage requiredNavigationEquipage() {
    return RequiredNavigationEquipage.CONV;
  }

  @Override
  public Collection<? extends Transition> transitions() {
    return transitions.values();
  }

  @Override
  public void accept(Visitor visitor) {
  }

  private static Transition ALL() {
    Leg SACRO = IF("SACRO", 28.74290277777778, -81.29910555555556);
    Leg TACOT = CF("TACOT", 28.690266666666666, -81.29855277777777);
    Leg DALTY = CF("DALTY", 28.637633333333333, -81.298);
    Leg ELLAN = CF("ELLAN", 28.58499722222222, -81.29744722222222);
    Leg GLOSI = CF("GLOSI", 28.55859722222222, -81.29716944444445);
    Leg MINCO = CF("MINCO", 28.511105555555556, -81.29667222222223);
    Leg RW17R = CF("RW17R", 28.43561111111111, -81.29588055555556);
    return transition("ALL", "I17R", "KMCO", TransitionType.COMMON, ProcedureType.APPROACH,
        Arrays.asList(SACRO, TACOT, DALTY, ELLAN, GLOSI, MINCO, RW17R));
  }

  private static Transition MISSED() {
    Leg NOGGI = CF("NOGGI", 28.163497222222222, -81.29303888888889);
    Leg NOGGI2 = HM("NOGGI", 28.163497222222222, -81.29303888888889);
    return transition("MISSED", "I17R", "KMCO", TransitionType.MISSED, ProcedureType.APPROACH,
        Arrays.asList(NOGGI, NOGGI2));
  }

  private static Transition RATOY() {
    Leg RATOY = IF("RATOY", 28.82301111111111, -81.29995);
    Leg SACRO = CF("SACRO", 28.74290277777778, -81.29910555555556);
    return transition("RATOY", "I17R", "KMCO", TransitionType.APPROACH, ProcedureType.APPROACH,
        Arrays.asList(RATOY, SACRO));
  }
}
