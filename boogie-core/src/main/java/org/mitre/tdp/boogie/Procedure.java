package org.mitre.tdp.boogie;

import java.util.Collection;

/**
 * Represents a logical procedure object which is composed of a collection of {@link Transition}s.
 * <br>
 * These transition contain sequences of legs which represent linear paths through portions of the procedure as a whole.
 * <br>
 * When aircraft file for a procedure typically the runway they are arriving to/departing from plus the indication of an entry
 * or exit fix define a unique path through the otherwise more complex procedure DAG (directed acyclic graph - at least in the
 * SID/STAR case).
 */
public interface Procedure {

  /**
   * The identifier for the procedure.
   * <br>
   * e.g. GLAVN1, HOBBT2, GNDLF1
   */
  String procedureIdentifier();

  /**
   * The identifier of the airport the procedure serves.
   */
  String airportIdentifier();

  /**
   * The region of the airport the procedure serves is in.
   */
  String airportRegion();

  /**
   * See {@link ProcedureType} - indicates where and in what way the procedure is supposed to be used.
   */
  ProcedureType procedureType();

  /**
   * See {@link RequiredNavigationEquipage} - this indicates at a high level the required equipage needed by aircraft to fly the
   * procedure.
   * * <br>
   * Often-times to get this label correct (as this higher-level categorization isn't typically in raw navigational data sources)
   * all of the procedure's component transitions need to be inspected - as such this lives here an <i>not</i> at the transition
   * level.
   */
  RequiredNavigationEquipage requiredNavigationEquipage();

  /**
   * Collection of all the {@link Transition}s referenced by the procedure.
   */
  Collection<? extends Transition> transitions();
}