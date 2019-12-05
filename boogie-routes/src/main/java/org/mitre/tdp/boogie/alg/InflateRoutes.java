package org.mitre.tdp.boogie.alg;

import java.util.Collection;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Runway;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.service.LookupService;
import org.mitre.tdp.boogie.service.impl.ProcedureGraphService;

public final class InflateRoutes<F extends Fix, L extends Leg<F>, W extends Airway<L>, T extends Transition<F, L>, R extends Runway, A extends Airport<R>> {

  private final LookupService<F> fixService;
  private final LookupService<W> airwayService;
  private final LookupService<A> airportService;
  private final ProcedureGraphService<F, L, T> procedureService;

  private InflateRoutes()

  public static <F extends Fix, L extends Leg<F>, W extends Airway<L>, T extends Transition<F, L>, R extends Runway, A extends Airport<R>> InflateRoutes<F, L, W, T, R, A> with(
      Collection<F> fixes, Collection<W> airways, Collection<A> airports, Collection<T> transitions) {

  }
}
