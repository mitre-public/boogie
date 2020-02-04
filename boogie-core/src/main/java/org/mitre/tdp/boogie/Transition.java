package org.mitre.tdp.boogie;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Common interface for transition objects in TDP. This interface is leveraged in downstream
 * code for both fusing transitions across data sources as well as in things like route parsing
 * and other analytics.
 */
public interface Transition extends Infrastructure {
  /**
   * The string name of the associated airport.
   */
  String airport();

  /**
   * The string name of the procedure which the transition is a part of.
   */
  String procedure();

  /**
   * The common type of procedure. We sacrifice some of the specificity of underlying source
   * data here in order to have a common representation of the kind of procedure the transition
   * is a part of.
   *
   * See {@link ProcedureType}.
   */
  ProcedureType procedureType();

  /**
   * Returns what kind of transition is being referred to. {@link TransitionType}
   */
  TransitionType transitionType();

  /**
   * Returns the legs which make up the transition.
   */
  List<? extends Leg> legs();

  /**
   * Returns the collection of all concrete leg types.
   */
  default List<? extends Leg> concreteLegs() {
    return legs().stream().filter(leg -> LegType.CONCRETETYPES.test(leg.type())).collect(Collectors.toList());
  }
}
