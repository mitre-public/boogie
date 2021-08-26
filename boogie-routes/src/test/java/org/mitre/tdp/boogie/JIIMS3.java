package org.mitre.tdp.boogie;

import static org.mitre.tdp.boogie.MockObjects.FM;
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

public final class JIIMS3 implements Procedure {

  public static final JIIMS3 INSTANCE = new JIIMS3();

  private final Map<String, Transition> transitions;

  private JIIMS3() {
    Map<String, Transition> map = Stream.of(SWL(), DASHA(), BRIGS(), RW17(), RW09B(), RW35(), RW26(), RW27B())
        .collect(Collectors.toMap(t -> t.transitionIdentifier().orElse(null), Function.identity()));
    this.transitions = ImmutableMap.copyOf(map);
  }

  @Override
  public String procedureIdentifier() {
    return "JIIMS3";
  }

  @Override
  public String airportIdentifier() {
    return "KPHL";
  }

  @Override
  public String airportRegion() {
    return "K4";
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

  public Leg get(String fname, String tname) {
    Transition transition = get(tname);
    return transition.legs().stream().filter(l -> l.associatedFix().isPresent()).filter(l -> fname.equals(l.associatedFix().get().fixIdentifier())).findFirst().orElse(null);
  }

  public Transition get(String tname) {
    return this.transitions.get(tname);
  }


  private static Transition SWL() {
    Leg SWL = IF("SWL", 38.05659444444444, -75.46390000000001);
    Leg RADDS = TF("RADDS", 38.648555555555554, -75.08846666666666);
    Leg WNSTN = TF("WNSTN", 39.09550277777778, -74.80033333333333);
    Leg HEKMN = TF("HEKMN", 39.377075, -74.90637222222223);
    Leg JIIMS = TF("JIIMS", 39.53767222222222, -74.96714444444444);
    return transition("SWL", "JIIMS3", "KPHL", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(SWL, RADDS, WNSTN, HEKMN, JIIMS));
  }

  private static Transition RW17() {
    Leg JIIMS = IF("JIIMS", 39.53767222222222, -74.96714444444444);
    Leg SNEDE = TF("SNEDE", 39.56268055555555, -75.02313611111111);
    Leg SNEDEFM = FM("SNEDE", 39.56268055555555, -75.02313611111111);
    return transition("RW17", "JIIMS3", "KPHL", TransitionType.RUNWAY, ProcedureType.STAR,
        Arrays.asList(JIIMS, SNEDE, SNEDEFM));
  }

  private static Transition RW09B() {
    Leg JIIMS = IF("JIIMS", 39.53767222222222, -74.96714444444444);
    Leg TLUKS = TF("TLUKS", 39.56359444444444, -75.30269166666666);
    Leg CHEAZ = TF("CHEAZ", 39.738327777777776, -75.37936388888889);
    Leg STAYK = TF("STAYK", 39.70346388888889, -75.538575);
    Leg STAYFM = FM("STAYK", 39.70346388888889, -75.538575);
    return transition("RW09B", "JIIMS3", "KPHL", TransitionType.RUNWAY, ProcedureType.STAR,
        Arrays.asList(JIIMS, TLUKS, CHEAZ, STAYK, STAYFM));
  }

  private static Transition RW35() {
    Leg JIIMS = IF("JIIMS", 39.53767222222222, -74.96714444444444);
    Leg SNEDE = TF("SNEDE", 39.56268055555555, -75.02313611111111);
    Leg SNEDEFM = FM("SNEDE", 39.56268055555555, -75.02313611111111);
    return transition("RW35", "JIIMS3", "KPHL", TransitionType.RUNWAY, ProcedureType.STAR,
        Arrays.asList(JIIMS, SNEDE, SNEDEFM));
  }

  private static Transition RW26() {
    Leg JIIMS = IF("JIIMS", 39.53767222222222, -74.96714444444444);
    Leg BEELZ = TF("BEELZ", 39.659283333333335, -75.07007777777777);
    Leg WOJIK = TF("WOJIK", 39.80231666666666, -75.08707222222222);
    Leg PSOUT = TF("PSOUT", 39.83503888888889, -74.93254444444445);
    Leg PSOUTFM = FM("PSOUT", 39.83503888888889, -74.93254444444445);
    return transition("RW26", "JIIMS3", "KPHL", TransitionType.RUNWAY, ProcedureType.STAR,
        Arrays.asList(JIIMS, BEELZ, WOJIK, PSOUT, PSOUTFM));
  }

  private static Transition DASHA() {
    Leg DASHA = IF("DASHA", 38.871969444444446, -74.34454722222222);
    Leg WNSTN = TF("WNSTN", 39.09550277777778, -74.80033333333333);
    Leg HEKMN = TF("HEKMN", 39.377075, -74.90637222222223);
    Leg JIIMS = TF("JIIMS", 39.53767222222222, -74.96714444444444);
    return transition("DASHA", "JIIMS3", "KPHL", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(DASHA, WNSTN, HEKMN, JIIMS));
  }

  private static Transition RW27B() {
    Leg JIIMS = IF("JIIMS", 39.53767222222222, -74.96714444444444);
    Leg BEELZ = TF("BEELZ", 39.659283333333335, -75.07007777777777);
    Leg WOJIK = TF("WOJIK", 39.80231666666666, -75.08707222222222);
    Leg PSOUT = TF("PSOUT", 39.83503888888889, -74.93254444444445);
    Leg PSOUTFM = FM("PSOUT", 39.83503888888889, -74.93254444444445);
    return transition("RW27B", "JIIMS3", "KPHL", TransitionType.RUNWAY, ProcedureType.STAR,
        Arrays.asList(JIIMS, BEELZ, WOJIK, PSOUT, PSOUTFM));
  }

  private static Transition BRIGS() {
    Leg BRIGS = IF("BRIGS", 39.52353333333333, -74.13879722222222);
    Leg IROKT = TF("IROKT", 39.53436388888889, -74.75079722222222);
    Leg JIIMS = TF("JIIMS", 39.53767222222222, -74.96714444444444);
    return transition("BRIGS", "JIIMS3", "KPHL", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(BRIGS, IROKT, JIIMS));
  }
}
