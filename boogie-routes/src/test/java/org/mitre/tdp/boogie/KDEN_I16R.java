package org.mitre.tdp.boogie;

import static org.mitre.tdp.boogie.MockObjects.CA;
import static org.mitre.tdp.boogie.MockObjects.CF;
import static org.mitre.tdp.boogie.MockObjects.HM;
import static org.mitre.tdp.boogie.MockObjects.IF;
import static org.mitre.tdp.boogie.MockObjects.TF;
import static org.mitre.tdp.boogie.MockObjects.VI;
import static org.mitre.tdp.boogie.MockObjects.transition;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableMap;

/**
 * Mock of I16R approach procedure for runway 16R at KDEN.
 */
public final class KDEN_I16R implements Procedure {

  public static final Procedure I16R = new KDEN_I16R();

  private final Map<String, Transition> transitions;

  private KDEN_I16R() {
    Map<String, Transition> map = Stream.of(KIPPR(), KAILE(), TSHNR(), COMMON())
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
    return "I16R";
  }

  @Override
  public String airportIdentifier() {
    return "KDEN";
  }

  @Override
  public ProcedureType procedureType() {
    return ProcedureType.APPROACH;
  }

  @Override
  public RequiredNavigationEquipage requiredNavigationEquipage() {
    return RequiredNavigationEquipage.CONV;
  }

  public Collection<Transition> transitions() {
    return transitions.values();
  }

  private static Transition KIPPR() {
    Leg KIPPR = IF("KIPPR", 40.34953333333333, -104.56333055555555);
    Leg HAYLY = TF("HAYLY", 40.33148611111111, -104.66150277777778);
    Leg SKIMJ = TF("SKIMJ", 40.29274722222222, -104.69146944444445);
    Leg SEEYU = TF("SEEYU", 40.24271111111111, -104.69205277777777);
    Leg SHRED = TF("SHRED", 40.207258333333336, -104.69246666666668);
    Leg SAKIC = TF("SAKIC", 40.16171388888889, -104.69299722222223);
    Leg NEWLN = TF("NEWLN", 40.09677777777778, -104.69375277777777);
    Leg MERYN = CF("MERYN", 40.03339722222222, -104.6944888888889);
    return transition("KIPPR", "I16R", "KDEN", TransitionType.APPROACH, ProcedureType.APPROACH,
        Arrays.asList(KIPPR, HAYLY, SKIMJ, SEEYU, SHRED, SAKIC, NEWLN, MERYN));
  }

  private static Transition KAILE() {
    Leg KAILE = IF("KAILE", 40.26343055555556, -104.76843611111111);
    Leg SHROM = TF("SHROM", 40.238958333333336, -104.70918055555556);
    Leg SHRED = TF("SHRED", 40.207258333333336, -104.69246666666668);
    Leg SAKIC = TF("SAKIC", 40.16171388888889, -104.69299722222223);
    Leg NEWLN = TF("NEWLN", 40.09677777777778, -104.69375277777777);
    Leg MERYN = CF("MERYN", 40.03339722222222, -104.6944888888889);
    return transition("KAILE", "I16R", "KDEN", TransitionType.APPROACH, ProcedureType.APPROACH,
        Arrays.asList(KAILE, SHROM, SHRED, SAKIC, NEWLN, MERYN));
  }

  private static Transition TSHNR() {
    Leg TSHNR = IF("TSHNR", 40.355869444444444, -104.74770555555556);
    Leg SKIMJ = TF("SKIMJ", 40.29274722222222, -104.69146944444445);
    Leg SEEYU = TF("SEEYU", 40.24271111111111, -104.69205277777777);
    Leg SHRED = TF("SHRED", 40.207258333333336, -104.69246666666668);
    Leg SAKIC = TF("SAKIC", 40.16171388888889, -104.69299722222223);
    Leg NEWLN = TF("NEWLN", 40.09677777777778, -104.69375277777777);
    Leg MERYN = CF("MERYN", 40.03339722222222, -104.6944888888889);
    return transition("TSHNR", "I16R", "KDEN", TransitionType.APPROACH, ProcedureType.APPROACH,
        Arrays.asList(TSHNR, SKIMJ, SEEYU, SHRED, SAKIC, NEWLN, MERYN));
  }

  private static Transition COMMON() {
    Leg MERYN = IF("MERYN", 40.03339722222222, -104.6944888888889);
    Leg JETSN = CF("JETSN", 39.980786111111115, -104.69510000000001);
    Leg KDEN = CF("KDEN", 39.861666666666665, -104.67316666666667);
    Leg CA = CA();
    Leg VI = VI();
    Leg BREWS = CF("BREWS", 39.65445833333333, -105.18080277777779);
    Leg BREWS2 = HM("BREWS", 39.65445833333333, -105.18080277777779);
    return transition("", "I16R", "KDEN", TransitionType.COMMON, ProcedureType.APPROACH,
        Arrays.asList(MERYN, JETSN, KDEN, CA, VI, BREWS, BREWS2));
  }
}

