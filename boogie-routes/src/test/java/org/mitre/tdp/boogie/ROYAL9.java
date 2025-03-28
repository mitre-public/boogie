package org.mitre.tdp.boogie;

import static org.mitre.tdp.boogie.MockObjects.IF;
import static org.mitre.tdp.boogie.MockObjects.TF;
import static org.mitre.tdp.boogie.MockObjects.transition;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableMap;

public final class ROYAL9 implements Procedure {

  public static final ROYAL9 INSTANCE = new ROYAL9();

  private final Map<String, Transition> transitions;

  private ROYAL9() {
    Map<String, Transition> map = Stream.of(TONCE(), ARENZ(), BODYN(), common())
        .collect(Collectors.toMap(t -> t.transitionIdentifier().orElse(null), Function.identity()));
    this.transitions = ImmutableMap.copyOf(map);
  }

  @Override
  public String procedureIdentifier() {
    return "CONNR5";
  }

  @Override
  public String airportIdentifier() {
    return "KDEN";
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

  @Override
  public void accept(Visitor visitor) {
  }

  private static Transition TONCE() {
    Leg MCI = IF("MCI", 39.28528333333333, -94.73706111111112, 10);
    Leg JDOGG = TF("JDOGG", 39.94994444444444, -94.66150833333334, 20);
    Leg TONCE = TF("TONCE", 40.282225, -94.62318333333333, 30);
    return transition("TONCE", "ROYAL9", "KLXT", TransitionType.ENROUTE, ProcedureType.SID,
        Arrays.asList(MCI, JDOGG, TONCE));
  }

  private static Transition ARENZ() {
    Leg MCI = IF("MCI", 39.28528333333333, -94.73706111111112, 10);
    Leg CAYKO = TF("CAYKO", 39.763708333333334, -94.13650555555556, 20);
    Leg JTHRO = TF("JTHRO", 40.001730555555554, -93.83311111111111, 30);
    Leg ARENZ = TF("ARENZ", 40.14373888888889, -93.65056111111112, 40);
    return transition("ARENZ", "ROYAL9", "KLXT", TransitionType.ENROUTE, ProcedureType.SID,
        Arrays.asList(MCI, CAYKO, JTHRO, ARENZ));
  }

  private static Transition BODYN() {
    Leg MCI = IF("MCI", 39.28528333333333, -94.73706111111112, 10);
    Leg CAYKO = TF("CAYKO", 39.763708333333334, -94.13650555555556, 20);
    Leg JTHRO = TF("JTHRO", 40.001730555555554, -93.83311111111111, 30);
    Leg ARENZ = TF("ARENZ", 40.14373888888889, -93.65056111111112, 40);
    Leg BODYN = TF("BODYN", 40.34196388888889, -92.13501111111111, 40);
    return transition("BODYN", "ROYAL9", "KLXT", TransitionType.ENROUTE, ProcedureType.SID,
        Arrays.asList(MCI, CAYKO, JTHRO, ARENZ, BODYN));
  }

  private static Transition common() {
    Leg MCI = IF("MCI", 39.28528333333333, -94.73706111111112, 10);
    return transition("ALL", "ROYAL9", "KLXT", TransitionType.COMMON, ProcedureType.SID, Arrays.asList(MCI));
  }
}
