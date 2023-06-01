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

public class CUN implements Procedure {

  public static final CUN INSTANCE = new CUN();

  private final Map<String, Transition> transitions;

  private CUN() {
    Map<String, Transition> map = Stream.of(AGNIX(), LIDEK(), LETIS(), OTMUT(), XORAR(), TAKUX(), DUGNI(), XOTNA(), SEBAK(), LOKMA(), ALL())
        .collect(Collectors.toMap(t -> t.transitionIdentifier().orElse(null), Function.identity()));
    this.transitions = ImmutableMap.copyOf(map);
  }

  @Override
  public String procedureIdentifier() {
    return "CUN";
  }

  @Override
  public String airportIdentifier() {
    return "MMUN";
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

  private static Transition AGNIX(){
    Leg AGNIX=IF ( "AGNIX" , 21.889930555555555 , -87.70383611111112);
    Leg ALSEG=TF ( "ALSEG" , 21.87 , -87.68416666666667);
    Leg EPNEL=TF ( "EPNEL" , 21.64263888888889 , -87.46072222222223);
    Leg ADSUN=TF ( "ADSUN" , 21.515555555555554 , -87.33638888888889);
    Leg D20=TF ( "D20" , 21.272375 , -87.09891944444443);
    Leg CUN=TF ( "CUN" , 21.025108333333332 , -86.85871666666667);
    return transition("AGNIX","CUN","MMUN", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(AGNIX, ALSEG, EPNEL, ADSUN, D20, CUN));
  }
  private static Transition LIDEK(){
    Leg LIDEK=IF ( "LIDEK" , 20.76638888888889 , -86.88527777777779);
    Leg CUN=TF ( "CUN" , 21.025108333333332 , -86.85871666666667);
    return transition("LIDEK","CUN","MMUN", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(LIDEK, CUN));
  }
  private static Transition LETIS(){
    Leg LETIS=IF ( "LETIS" , 21.528666666666666 , -86.38988888888889);
    Leg CUN=TF ( "CUN" , 21.025108333333332 , -86.85871666666667);
    return transition("LETIS","CUN","MMUN", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(LETIS, CUN));
  }
  private static Transition OTMUT(){
    Leg OTMUT=IF ( "OTMUT" , 20.628333333333334 , -86.71861111111112);
    Leg CUN=TF ( "CUN" , 21.025108333333332 , -86.85871666666667);
    return transition("OTMUT","CUN","MMUN", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(OTMUT, CUN));
  }
  private static Transition XORAR(){
    Leg XORAR=IF ( "XORAR" , 22.287222222222223 , -87.33972222222222);
    Leg CUN=TF ( "CUN" , 21.025108333333332 , -86.85871666666667);
    return transition("XORAR","CUN","MMUN", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(XORAR, CUN));
  }
  private static Transition TAKUX(){
    Leg TAKUX=IF ( "TAKUX" , 20.026944444444442 , -85.89611111111111);
    Leg R139=TF ( "R139" , 20.15639722222222 , -86.02486388888889);
    Leg AVSEB=TF ( "AVSEB" , 20.370333333333335 , -86.22528611111112);
    Leg BOTOP=TF ( "BOTOP" , 20.765555555555554 , -86.60694444444444);
    Leg CUN=TF ( "CUN" , 21.025108333333332 , -86.85871666666667);
    return transition("TAKUX","CUN","MMUN", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(TAKUX, R139, AVSEB, BOTOP, CUN));
  }
  private static Transition DUGNI(){
    Leg DUGNI=IF ( "DUGNI" , 21.102777777777778 , -86.46027777777778);
    Leg CUN=TF ( "CUN" , 21.025108333333332 , -86.85871666666667);
    return transition("DUGNI","CUN","MMUN", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(DUGNI, CUN));
  }
  private static Transition XOTNA(){
    Leg XOTNA=IF ( "XOTNA" , 21.199444444444445 , -87.64083333333333);
    Leg CUN=TF ( "CUN" , 21.025108333333332 , -86.85871666666667);
    return transition("XOTNA","CUN","MMUN", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(XOTNA, CUN));
  }
  private static Transition SEBAK(){
    Leg SEBAK=IF ( "SEBAK" , 21.61722222222222 , -88.22638888888889);
    Leg D070=TF ( "D070" , 21.516147222222223 , -87.99272500000001);
    Leg D050=TF ( "D050" , 21.376633333333334 , -87.66795555555557);
    Leg NOVEV=TF ( "NOVEV" , 21.17 , -87.19027777777778);
    Leg CUN=TF ( "CUN" , 21.025108333333332 , -86.85871666666667);
    return transition("SEBAK","CUN","MMUN", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(SEBAK, D070, D050, NOVEV, CUN));
  }
  private static Transition LOKMA(){
    Leg LOKMA=IF ( "LOKMA" , 20.991944444444446 , -88.10805555555555);
    Leg R270=TF ( "R270" , 21.002469444444444 , -87.74910555555556);
    Leg R270X=TF ( "R270X" , 21.014925 , -87.2861361111111);
    Leg CUN=TF ( "CUN" , 21.025108333333332 , -86.85871666666667);
    return transition("LOKMA","CUN","MMUN", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(LOKMA, R270, R270X, CUN));
  }
  private static Transition ALL(){
    Leg CUN=IF ( "CUN" , 21.025108333333332 , -86.85871666666667);
    return transition("ALL","CUN","MMUN", TransitionType.ENROUTE, ProcedureType.STAR,
        Arrays.asList(CUN));
  }
}
