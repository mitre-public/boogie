package org.mitre.tdp.boogie.contract.routes;

import java.util.List;
import java.util.Optional;

import org.immutables.value.Value;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.TransitionType;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableTransition.class)
@JsonDeserialize(as = ImmutableTransition.class)
public interface Transition {
  /**
   * The optional name of the transition.
   * <br>
   * Transition naming conventions are typically to the effect of:
   * <br>
   * 1. {@link TransitionType#COMMON} - these are generally either null or named something like "ALL" as all traffic which traverses
   * the SID/STAR/APPROACH must pass through this transition. In some cases though a procedure won't have a common portion, in which
   * case there will be a common merge fix (shared by the enroute/runway transitions) - but not a clearly defined COMMON transition.
   * <br>
   * 2. {@link TransitionType#ENROUTE} - these are typically named for their entry fix (in the STAR case) or for their exit fix (in
   * the SID case). E.g. an ENROUTE STAR transition who's first fix is STRDR is likely to be named similarly... i.e. STRDR.
   * <br>
   * 3. {@link TransitionType#RUNWAY} - these are generally named after the runway (or runways) they service. E.g. a transition
   * serving a single runway may be named 'RW13R' or one servicing two (a left/right) may be called 'RW23B'.
   */
  Optional<String> transitionIdentifier();

  /**
   * The string name of the procedure which the transition is a part of.
   * <br>
   * See {@link org.mitre.tdp.boogie.Procedure#procedureIdentifier()}.
   */
  String procedureIdentifier();

  /**
   * The identifier of the serviced airport.
   * <br>
   * See {@link org.mitre.tdp.boogie.Procedure#airportIdentifier()}.
   */
  String airportIdentifier();

  /**
   * The region in which the serviced airport is located.
   * <br>
   */
  String airportRegion();

  /**
   * See {@link ProcedureType}.
   */
  ProcedureType procedureType();

  /**
   * See {@link TransitionType}.
   */
  TransitionType transitionType();

  /**
   * Returns the ordered sequence of legs which make up the transition.
   */
  List<Leg> legs();
}
