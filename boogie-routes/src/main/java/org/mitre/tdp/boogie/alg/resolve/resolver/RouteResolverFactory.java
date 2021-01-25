package org.mitre.tdp.boogie.alg.resolve.resolver;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.alg.resolve.RunwayPredictor;
import org.mitre.tdp.boogie.service.LookupService;
import org.mitre.tdp.boogie.service.ProcedureService;
import org.mitre.tdp.boogie.service.impl.AirportService;
import org.mitre.tdp.boogie.service.impl.AirwayService;
import org.mitre.tdp.boogie.service.impl.FixService;
import org.mitre.tdp.boogie.service.impl.ProcedureGraphService;

/**
 * Factory class for generating new instances of {@link RouteResolver}s with different arrival/departure runway predictions.
 */
public final class RouteResolverFactory {

  private final LookupService<Fix> fixService;
  private final LookupService<Airway> airwayService;
  private final LookupService<Airport> airportService;
  private final ProcedureService procedureService;

  public RouteResolverFactory(
      LookupService<Fix> fixService,
      LookupService<Airway> airwayService,
      LookupService<Airport> airportService,
      ProcedureService procedureService
  ) {
    this.fixService = checkNotNull(fixService);
    this.airwayService = checkNotNull(airwayService);
    this.airportService = checkNotNull(airportService);
    this.procedureService = checkNotNull(procedureService);
  }

  public RouteResolverFactory(
      Collection<? extends Fix> fixes,
      Collection<? extends Airway> airways,
      Collection<? extends Airport> airports,
      Collection<? extends Transition> transitions
  ) {
    this(
        FixService.with(fixes),
        AirwayService.with(airways),
        AirportService.with(airports),
        ProcedureGraphService.withTransitions(transitions)
    );
  }

  public RouteResolver newResolver() {
    return newResolver(RunwayPredictor.noop(), RunwayPredictor.noop());
  }

  /**
   * Generates a new {@link RouteResolver} with the provided {@link RunwayPredictor}s for arrival/departure runway respectively.
   *
   * When provided the arrival/departure runway predictors will be used to generate candidate route segments through the runway
   * transition of any matched sid/stars from the flightplan.
   */
  public RouteResolver newResolver(RunwayPredictor arrivalPredictor, RunwayPredictor departurePredictor) {
    return new RouteResolver(
        new FixResolver(fixService),
        new AirwayResolver(airwayService),
        new AirportResolver(airportService),
        new ProcedureResolver(
            procedureService,
            arrivalPredictor,
            departurePredictor
        ),
        new LatLonResolver()
    );
  }
}
