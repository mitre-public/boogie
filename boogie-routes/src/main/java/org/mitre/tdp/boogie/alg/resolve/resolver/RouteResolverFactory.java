package org.mitre.tdp.boogie.alg.resolve.resolver;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.alg.DefaultLookupService;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.model.ProcedureFactory;

/**
 * Factory class for generating new instances of {@link RouteResolver}s with different arrival/departure runway predictions.
 */
public final class RouteResolverFactory {

  private final LookupService<Fix> fixService;
  private final LookupService<Airway> airwayService;
  private final LookupService<Airport> airportService;
  private final LookupService<Procedure> procedureService;

  /**
   * Creates a new {@link RouteResolverFactory} with the provided {@link LookupService}s for the infrastructure element types
   * which can appear in the route string of a flight.
   */
  public RouteResolverFactory(
      LookupService<Fix> fixService,
      LookupService<Airway> airwayService,
      LookupService<Airport> airportService,
      LookupService<Procedure> procedureService
  ) {
    this.fixService = checkNotNull(fixService);
    this.airwayService = checkNotNull(airwayService);
    this.airportService = checkNotNull(airportService);
    this.procedureService = checkNotNull(procedureService);
  }

  /**
   * Creates a new {@link RouteResolverFactory} with the provided collections of infrastructure element types known to be used
   * in route strings - indexing them in default {@link LookupService} implementations.
   */
  public RouteResolverFactory(
      Collection<? extends Fix> fixes,
      Collection<? extends Airway> airways,
      Collection<? extends Airport> airports,
      Collection<? extends Transition> transitions
  ) {
    this(
        // ugh collection type casting... makes it nicer to use though...
        (LookupService<Fix>) DefaultLookupService.newLookupService(Fix::fixIdentifier, fixes),
        (LookupService<Airway>) DefaultLookupService.newLookupService(Airway::airwayIdentifier, airways),
        (LookupService<Airport>) DefaultLookupService.newLookupService(Airport::airportIdentifier, airports),
        DefaultLookupService.newLookupService(Procedure::procedureIdentifier, ProcedureFactory.newProcedures(transitions))
    );
  }

  /**
   * Generates a new {@link RouteResolver} which uses no runway predictions in it's expansion - this means all SID/STAR expansions
   * will be cut off at the start/end of the common portion of the procedures.
   */
  public RouteResolver newResolver() {
    return new RouteResolver(
        new FixResolver(fixService),
        new AirwayResolver(airwayService),
        new AirportResolver(airportService),
        new ProcedureResolver(procedureService),
        new LatLonResolver()
    );
  }
}
