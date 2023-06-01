package org.mitre.tdp.boogie;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableMap;

import static org.mitre.tdp.boogie.MockObjects.IF;
import static org.mitre.tdp.boogie.MockObjects.TF;
import static org.mitre.tdp.boogie.MockObjects.transition;

public class CZM implements Procedure {

  public static final CZM INSTANCE = new CZM();

  private final Map<String, Transition> transitions;

  @Override
  public String procedureIdentifier() {
    return "CZM";
  }

  @Override
  public String airportIdentifier() {
    return "MMCZ";
  }

  @Override
  public ProcedureType procedureType() {
    return ProcedureType.STAR;
  }

  @Override
  public RequiredNavigationEquipage requiredNavigationEquipage() {
    return RequiredNavigationEquipage.CONV;
  }

  @Override
  public Collection<? extends Transition> transitions() {
    return transitions.values();
  }

  private CZM() {
    Map<String, Transition> map = Stream.of(DUPIX(), ALL(), AVSIM(), ITAKU(), LIDEK(), BOTOP(), OTEDI(), KESPO(), DAXUM(), ANIKO(), GOSUL(), ITPIG(), VOBED())
        .collect(Collectors.toMap(t -> t.transitionIdentifier().orElse(null), Function.identity()));
    this.transitions = ImmutableMap.copyOf(map);
  }

  private static Transition DUPIX(){
    Leg DUPIX=IF ( "DUPIX" , 20.547777777777778 , -87.35388888888889);
    Leg CZM=TF ( "CZM" , 20.507472222222223 , -86.912);
    return transition("DUPIX","CZM","MMCZ", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(DUPIX, CZM));
  }
  private static Transition ALL(){
    Leg CZM=IF ( "CZM" , 20.507472222222223 , -86.912);
    return transition("ALL","CZM","MMCZ", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(CZM));
  }
  private static Transition AVSIM(){
    Leg AVSIM=IF ( "AVSIM" , 20.350583333333336 , -87.78355555555555);
    Leg OKUVA=TF ( "OKUVA" , 20.429444444444446 , -87.34805555555555);
    Leg CZM=TF ( "CZM" , 20.507472222222223 , -86.912);
    return transition("AVSIM","CZM","MMCZ", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(AVSIM, OKUVA, CZM));
  }
  private static Transition ITAKU(){
    Leg ITAKU=IF ( "ITAKU" , 20.14611111111111 , -86.6888888888889);
    Leg CZM=TF ( "CZM" , 20.507472222222223 , -86.912);
    return transition("ITAKU","CZM","MMCZ", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(ITAKU, CZM));
  }
  private static Transition LIDEK(){
    Leg LIDEK=IF ( "LIDEK" , 20.76638888888889 , -86.88527777777779);
    Leg CZM=TF ( "CZM" , 20.507472222222223 , -86.912);
    return transition("LIDEK","CZM","MMCZ", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(LIDEK, CZM));
  }
  private static Transition BOTOP(){
    Leg BOTOP=IF ( "BOTOP" , 20.765555555555554 , -86.60694444444444);
    Leg CZM=TF ( "CZM" , 20.507472222222223 , -86.912);
    return transition("BOTOP","CZM","MMCZ", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(BOTOP, CZM));
  }
  private static Transition OTEDI(){
    Leg OTEDI=IF ( "OTEDI" , 20.161666666666665 , -87.16111111111111);
    Leg CZM=TF ( "CZM" , 20.507472222222223 , -86.912);
    return transition("OTEDI","CZM","MMCZ", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(OTEDI, CZM));
  }
  private static Transition KESPO(){
    Leg KESPO=IF ( "KESPO" , 21.709722222222222 , -85.99527777777777);
    Leg D25=TF ( "D25" , 20.848166666666664 , -86.65429722222223);
    Leg CZM=TF ( "CZM" , 20.507472222222223 , -86.912);
    return transition("KESPO","CZM","MMCZ", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(KESPO, D25, CZM));
  }
  private static Transition DAXUM(){
    Leg DAXUM=IF ( "DAXUM" , 20.126583333333333 , -87.09511111111111);
    Leg CZM=TF ( "CZM" , 20.507472222222223 , -86.912);
    return transition("DAXUM","CZM","MMCZ", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(DAXUM, CZM));
  }
  private static Transition ANIKO(){
    Leg ANIKO=IF ( "ANIKO" , 19.048611111111114 , -86.9063888888889);
    Leg DME50=TF ( "50DME" , 19.67103888888889 , -86.90721666666667);
    Leg OBTAM=TF ( "OBTAM" , 20.089277777777777 , -86.90769444444445);
    Leg CZM=TF ( "CZM" , 20.507472222222223 , -86.912);
    return transition("ANIKO","CZM","MMCZ", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(ANIKO, DME50, OBTAM, CZM));
  }
  private static Transition GOSUL(){
    Leg GOSUL=IF ( "GOSUL" , 20.421111111111113 , -86.47777777777777);
    Leg CZM=TF ( "CZM" , 20.507472222222223 , -86.912);
    return transition("GOSUL","CZM","MMCZ", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(GOSUL, CZM));
  }
  private static Transition ITPIG(){
    Leg ITPIG=IF ( "ITPIG" , 20.09675 , -86.99552777777778);
    Leg CZM=TF ( "CZM" , 20.507472222222223 , -86.912);
    return transition("ITPIG","CZM","MMCZ", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(ITPIG, CZM));
  }
  private static Transition VOBED(){
    Leg VOBED=IF ( "VOBED" , 20.939722222222223 , -87.22027777777778);
    Leg CZM=TF ( "CZM" , 20.507472222222223 , -86.912);
    return transition("VOBED","CZM","MMCZ", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(VOBED, CZM));
  }
}
