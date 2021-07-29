package org.mitre.tdp.boogie;

import java.util.List;
import java.util.Optional;

/**
 * Common interface for transition objects in TDP. This interface maintains many of the same accessors as the {@link Procedure}
 * interface does primarily because Boogie does most of its work with procedures at the transition level - and so having the high
 * level contextual information about the procedure present is convenient.
 * <br>
 * Transitions represent logical <i>linear</i> sequences of legs within a procedure.
 * <br>
 * Multiple transitions together represent the collection of linear paths through a procedure and can typically be zipped together
 * into a DAG representing the overall procedure 'graph'. In the cases of SIDs/STARs this typically works out as:
 * <br>
 * 1. SIDs: The end fix of the runway transitions are the first fix of the common portion - then the end fix of the common portion
 * is the start fix of the enroute transitions.
 * 2. STARs: The end fix of the enroute transitions is the first fix of the common portion - then the end fix of the common portion
 * is the start fix of the runway transitions.
 * <br>
 * Approach procedures operate somewhat similarly but they don't have runway transitions. They have "approach" transitions which
 * typically either directly connect to a STAR or serve to collect traffic of the end of a STAR/from terminal airspace onto the
 * final straight-in common portion of the approach.
 * <br>
 * Optionally most approach procedures will contain a MISSED transition which can be flown when the aircraft initiates a go-around
 * due to something that occurred during the approach. Often MISSED transitions will end in a leg that indicates it's associated
 * fix is {@link Leg#isPublishedHoldingFix()} - so the aircraft can be held before being re-inserted into the arrival sequencing
 * at the airport.
 */
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
   * See {@link Procedure#procedureIdentifier()}.
   */
  String procedureIdentifier();

  /**
   * The identifier of the serviced airport.
   * <br>
   * See {@link Procedure#airportIdentifier()}.
   */
  String airportIdentifier();

  /**
   * The region in which the serviced airport is located.
   * <br>
   * See {@link Procedure#airportRegion()}.
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
  List<? extends Leg> legs();
}
