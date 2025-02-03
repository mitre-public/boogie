package org.mitre.tdp.boogie;

import static org.mitre.tdp.boogie.CategoryOrType.CAT_A;
import static org.mitre.tdp.boogie.CategoryOrType.CAT_B;
import static org.mitre.tdp.boogie.CategoryOrType.CAT_C;
import static org.mitre.tdp.boogie.CategoryOrType.CAT_D;
import static org.mitre.tdp.boogie.MockObjects.CF;
import static org.mitre.tdp.boogie.MockObjects.DF;
import static org.mitre.tdp.boogie.MockObjects.FA;
import static org.mitre.tdp.boogie.MockObjects.FD;
import static org.mitre.tdp.boogie.MockObjects.HM;
import static org.mitre.tdp.boogie.MockObjects.IF;
import static org.mitre.tdp.boogie.MockObjects.TF;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class GQNO_D34Y implements Procedure {

  public static final GQNO_D34Y INSTANCE = new GQNO_D34Y();

  private final List<Transition> transitions;

  public GQNO_D34Y() {
    this.transitions = List.of(otab(), otcd(), all(), missed());
  }
  @Override
  public String procedureIdentifier() {
    return "D34Y";
  }

  @Override
  public String airportIdentifier() {
    return "GQNO";
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
    return transitions;
  }

  @Override
  public void accept(Visitor visitor) {
    throw new UnsupportedOperationException("Not supported by test class.");
  }

  private static Transition otab() {
    Leg IF = IF("OT", 18.28044444444444, -15.951722222222221);
    Leg FD = FD("OT", 18.28044444444444, -15.951722222222221);
    Leg CF = CF("OT39", 18.224152777777775, -15.917747222222221);
    List<Leg> legs = List.of(IF, FD, CF);
    Set<CategoryOrType> cats = Set.of(CAT_A, CAT_B);
    return Transition.builder()
        .legs(legs)
        .categoryOrTypes(cats)
        .transitionType(TransitionType.APPROACH)
        .transitionIdentifier("OT")
        .build();
  }

  /**
   * Yes the only difference is infields in the FD leg that we don't care about for expansion
   */
  private static Transition otcd() {
    Leg IF = IF("OT", 18.28044444444444, -15.951722222222221);
    Leg FD = FD("OT", 18.28044444444444, -15.951722222222221);
    Leg CF = CF("OT39", 18.224152777777775, -15.917747222222221);
    List<Leg> legs = List.of(IF, FD, CF);
    Set<CategoryOrType> cats = Set.of(CAT_C, CAT_D);
    return Transition.builder()
        .transitionIdentifier("OT")
        .legs(legs)
        .categoryOrTypes(cats)
        .transitionType(TransitionType.APPROACH)
        .build();
  }

  private static Transition all() {
    Leg IF = IF("OT39", 18.224152777777775, -15.917747222222221);
    Leg TF = TF("OT", 18.28044444444444, -15.951722222222221);
    List<Leg> legs = List.of(IF, TF);
    Set<CategoryOrType> cats = Set.of(CAT_A, CAT_B, CAT_C, CAT_D);
    return Transition.builder()
        .transitionIdentifier("ALL")
        .legs(legs)
        .categoryOrTypes(cats)
        .transitionType(TransitionType.APPROACH)
        .build();
  }

  private static Transition missed() {
    Leg FA = FA("OT", 18.28044444444444, -15.951722222222221);
    Leg DF = DF("OT", 18.28044444444444, -15.951722222222221);
    Leg HM = HM("OT", 18.28044444444444, -15.951722222222221);
    List<Leg> legs = List.of(FA, DF, HM);
    Set<CategoryOrType> cats = Set.of(CAT_A, CAT_B, CAT_C, CAT_D);
    return Transition.builder()
        .transitionIdentifier("MISSED")
        .legs(legs)
        .categoryOrTypes(cats)
        .transitionType(TransitionType.MISSED)
        .build();
  }
}
