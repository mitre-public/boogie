package org.mitre.tdp.boogie.contract.routes;

import java.util.Collection;

import org.immutables.value.Value;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableProcedure.class)
@JsonDeserialize(as = ImmutableProcedure.class)
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
   * Collection of all the {@link org.mitre.tdp.boogie.Transition}s referenced by the procedure.
   */
  Collection<Transition> transitions();
}
