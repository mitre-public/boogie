package org.mitre.tdp.boogie.models;

import java.util.Collection;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Infrastructure;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Transition;

public interface Procedure<F extends Fix, L extends Leg<F>, T extends Transition<F, L>> extends Infrastructure {

  /**
   * Collection of all the transitions referenced by the procedure.
   */
  Collection<T> transitions();

  default String airport() {
    return transitions().iterator().next().airport();
  }
}
