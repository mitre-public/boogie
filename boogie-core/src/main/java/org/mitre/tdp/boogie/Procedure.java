package org.mitre.tdp.boogie;

import java.util.Collection;
import java.util.List;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Infrastructure;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.NavigationSource;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;

public interface Procedure extends Infrastructure {

  /**
   * Collection of all the transitions referenced by the procedure.
   */
  Collection<? extends Transition> transitions();

  /**
   * Returns the list of viable paths through the procedure between the provided.
   */
  List<List<Leg>> pathsBetween(Fix start, Fix end);

  @Override
  default String identifier() {
    return transitions().iterator().next().procedure();
  }

  default String airport() {
    return transitions().iterator().next().airport();
  }

  @Override
  default NavigationSource navigationSource() {
    return transitions().iterator().next().navigationSource();
  }

  default ProcedureType type() {
    return transitions().iterator().next().procedureType();
  }
}
