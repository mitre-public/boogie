package org.mitre.tdp.boogie;

import static org.mitre.tdp.boogie.MockObjects.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Range;

/**
 * RNAV Departure SID out of Denver from cycle 1910 for use in testing.
 */
public final class CONNR5 implements Procedure {

  public static final CONNR5 INSTANCE = new CONNR5();

  private final Map<String, Transition> transitions;

  private CONNR5() {
    Map<String, Transition> map = Stream.of(RW08(), RW16L(), RW16R(), RW17L(), RW17R(), RW25(), RW34B(), RW35B(), common())
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

  @Override
  public void accept(Visitor visitor) {
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
    Leg VA = Leg.builder(PathTerminator.VA, 10)
        .outboundMagneticCourse(83.0)
        .altitudeConstraint(Range.atLeast(5934.0))
        .build();
    Leg VM = Leg.builder(PathTerminator.VM, 20)
        .outboundMagneticCourse(83.0)
        .build();
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
}
