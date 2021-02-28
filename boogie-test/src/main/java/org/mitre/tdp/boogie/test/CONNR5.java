package org.mitre.tdp.boogie.test;

import static org.mitre.tdp.boogie.test.MockObjects.CF;
import static org.mitre.tdp.boogie.test.MockObjects.DF;
import static org.mitre.tdp.boogie.test.MockObjects.IF;
import static org.mitre.tdp.boogie.test.MockObjects.TF;
import static org.mitre.tdp.boogie.test.MockObjects.VA;
import static org.mitre.tdp.boogie.test.MockObjects.VI;
import static org.mitre.tdp.boogie.test.MockObjects.VM;
import static org.mitre.tdp.boogie.test.MockObjects.transition;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;

/**
 * RNAV Departure SID out of Denver from cycle 1910 for use in testing.
 */
public final class CONNR5 {

  private final Map<String, Transition> transitions;

  private CONNR5(Map<String, Transition> transitions) {
    this.transitions = transitions;
  }

  public static CONNR5 build() {
    Map<String, Transition> tmap = Stream.of(RW08(), RW16L(), RW16R(), RW17L(), RW17R(), RW25(), RW34B(), RW35B(), common())
        .collect(Collectors.toMap(Transition::identifier, Function.identity()));
    return new CONNR5(tmap);
  }

  private static Transition RW17L() {
    Leg VI = VI();
    Leg GOROC = CF("GOROC", 39.75694444444444, -104.71472222222222);
    Leg YORVT = TF("YORVT", 39.75661111111111, -104.76627222222223);
    Leg HURDL = TF("HURDL", 39.75709166666667, -104.90278333333333);
    Leg HAWPE = TF("HAWPE", 39.771947222222224, -105.01069166666667);
    Leg TUNNN = TF("TUNNN", 39.79627222222222, -105.20430277777778);
    Leg TAVRN = TF("TAVRN", 39.78148611111111, -105.27345555555556);
    Leg VONNN = TF("VONNN", 39.747572222222225, -105.43400277777778);
    Leg TEEBO = TF("TEEBO", 39.72018055555556, -105.56080555555555);
    Leg CONNR = TF("CONNR", 39.69906388888889, -105.66577777777778);
    return transition("RW17L", "CONNR5", "KDEN", TransitionType.RUNWAY, ProcedureType.SID,
        Arrays.asList(VI, GOROC, YORVT, HURDL, HAWPE, TUNNN, TAVRN, VONNN, TEEBO, CONNR));
  }

  private static Transition RW08() {
    Leg VA = VA();
    Leg VM = VM();
    Leg CONNR = DF("CONNR", 39.69906388888889, -105.66577777777778);
    return transition("RW08", "CONNR5", "KDEN", TransitionType.RUNWAY, ProcedureType.SID,
        Arrays.asList(VA, VM, CONNR));
  }

  private static Transition RW16R() {
    Leg VI = VI();
    Leg GOROC = CF("GOROC", 39.75694444444444, -104.71472222222222);
    Leg HURDL = TF("HURDL", 39.75709166666667, -104.90278333333333);
    Leg HAWPE = TF("HAWPE", 39.771947222222224, -105.01069166666667);
    Leg TUNNN = TF("TUNNN", 39.79627222222222, -105.20430277777778);
    Leg TAVRN = TF("TAVRN", 39.78148611111111, -105.27345555555556);
    Leg VONNN = TF("VONNN", 39.747572222222225, -105.43400277777778);
    Leg TEEBO = TF("TEEBO", 39.72018055555556, -105.56080555555555);
    Leg CONNR = TF("CONNR", 39.69906388888889, -105.66577777777778);
    return transition("RW16R", "CONNR5", "KDEN", TransitionType.RUNWAY, ProcedureType.SID,
        Arrays.asList(VI, GOROC, HURDL, HAWPE, TUNNN, TAVRN, VONNN, TEEBO, CONNR));
  }

  private static Transition common() {
    Leg CONNR = IF("CONNR", 39.69906388888889, -105.66577777777778);
    Leg BULDG = TF("BULDG", 39.626305555555554, -106.31375);
    Leg DBL = TF("DBL", 39.439344444444444, -106.89468055555557);
    return transition("", "CONNR5", "KDEN", TransitionType.COMMON, ProcedureType.SID,
        Arrays.asList(CONNR, BULDG, DBL));
  }

  private static Transition RW25() {
    Leg VA = VA();
    Leg WRIPS = DF("WRIPS", 39.83831666666667, -104.90175555555557);
    Leg MYALE = TF("MYALE", 39.83782222222222, -105.01248333333334);
    Leg TUNNN = TF("TUNNN", 39.79627222222222, -105.20430277777778);
    Leg TAVRN = TF("TAVRN", 39.78148611111111, -105.27345555555556);
    Leg VONNN = TF("VONNN", 39.747572222222225, -105.43400277777778);
    Leg TEEBO = TF("TEEBO", 39.72018055555556, -105.56080555555555);
    Leg CONNR = TF("CONNR", 39.69906388888889, -105.66577777777778);
    return transition("RW25", "CONNR5", "KDEN", TransitionType.RUNWAY, ProcedureType.SID,
        Arrays.asList(VA, WRIPS, MYALE, TUNNN, TAVRN, VONNN, TEEBO, CONNR));
  }

  private static Transition RW17R() {
    Leg VI = VI();
    Leg GOROC = CF("GOROC", 39.75694444444444, -104.71472222222222);
    Leg YORVT = TF("YORVT", 39.75661111111111, -104.76627222222223);
    Leg HURDL = TF("HURDL", 39.75709166666667, -104.90278333333333);
    Leg HAWPE = TF("HAWPE", 39.771947222222224, -105.01069166666667);
    Leg TUNNN = TF("TUNNN", 39.79627222222222, -105.20430277777778);
    Leg TAVRN = TF("TAVRN", 39.78148611111111, -105.27345555555556);
    Leg VONNN = TF("VONNN", 39.747572222222225, -105.43400277777778);
    Leg TEEBO = TF("TEEBO", 39.72018055555556, -105.56080555555555);
    Leg CONNR = TF("CONNR", 39.69906388888889, -105.66577777777778);
    return transition("RW17R", "CONNR5", "KDEN", TransitionType.RUNWAY, ProcedureType.SID,
        Arrays.asList(VI, GOROC, YORVT, HURDL, HAWPE, TUNNN, TAVRN, VONNN, TEEBO, CONNR));
  }

  private static Transition RW34B() {
    Leg VA = VA();
    Leg YOBUB = DF("YOBUB", 39.86156388888889, -104.90173333333334);
    Leg MYALE = TF("MYALE", 39.83782222222222, -105.01248333333334);
    Leg TUNNN = TF("TUNNN", 39.79627222222222, -105.20430277777778);
    Leg TAVRN = TF("TAVRN", 39.78148611111111, -105.27345555555556);
    Leg VONNN = TF("VONNN", 39.747572222222225, -105.43400277777778);
    Leg TEEBO = TF("TEEBO", 39.72018055555556, -105.56080555555555);
    Leg CONNR = TF("CONNR", 39.69906388888889, -105.66577777777778);
    return transition("RW34B", "CONNR5", "KDEN", TransitionType.RUNWAY, ProcedureType.SID,
        Arrays.asList(VA, YOBUB, MYALE, TUNNN, TAVRN, VONNN, TEEBO, CONNR));
  }

  private static Transition RW35B() {
    Leg VA = VA();
    Leg YOBUB = DF("YOBUB", 39.86156388888889, -104.90173333333334);
    Leg MYALE = TF("MYALE", 39.83782222222222, -105.01248333333334);
    Leg TUNNN = TF("TUNNN", 39.79627222222222, -105.20430277777778);
    Leg TAVRN = TF("TAVRN", 39.78148611111111, -105.27345555555556);
    Leg VONNN = TF("VONNN", 39.747572222222225, -105.43400277777778);
    Leg TEEBO = TF("TEEBO", 39.72018055555556, -105.56080555555555);
    Leg CONNR = TF("CONNR", 39.69906388888889, -105.66577777777778);
    return transition("RW35B", "CONNR5", "KDEN", TransitionType.RUNWAY, ProcedureType.SID,
        Arrays.asList(VA, YOBUB, MYALE, TUNNN, TAVRN, VONNN, TEEBO, CONNR));
  }

  private static Transition RW16L() {
    Leg VI = VI();
    Leg GOROC = CF("GOROC", 39.75694444444444, -104.71472222222222);
    Leg HURDL = TF("HURDL", 39.75709166666667, -104.90278333333333);
    Leg HAWPE = TF("HAWPE", 39.771947222222224, -105.01069166666667);
    Leg TUNNN = TF("TUNNN", 39.79627222222222, -105.20430277777778);
    Leg TAVRN = TF("TAVRN", 39.78148611111111, -105.27345555555556);
    Leg VONNN = TF("VONNN", 39.747572222222225, -105.43400277777778);
    Leg TEEBO = TF("TEEBO", 39.72018055555556, -105.56080555555555);
    Leg CONNR = TF("CONNR", 39.69906388888889, -105.66577777777778);
    return transition("RW16L", "CONNR5", "KDEN", TransitionType.RUNWAY, ProcedureType.SID,
        Arrays.asList(VI, GOROC, HURDL, HAWPE, TUNNN, TAVRN, VONNN, TEEBO, CONNR));
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
