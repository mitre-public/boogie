package org.mitre.tdp.boogie.alg;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.alg.approach.ApproachPredictor;
import org.mitre.tdp.boogie.alg.approach.impl.NoApproachPredictor;
import org.mitre.tdp.boogie.alg.resolve.RunwayPredictor;
import org.mitre.tdp.boogie.alg.resolve.resolver.RouteResolver;
import org.mitre.tdp.boogie.alg.resolve.resolver.RouteResolverFactory;
import org.mitre.tdp.boogie.alg.split.IfrFormatSectionSplitter;
import org.mitre.tdp.boogie.service.LookupService;
import org.mitre.tdp.boogie.service.ProcedureService;

/**
 * Factory class for generating instances of {@link RouteExpander}s. This class decorates a {@link RouteResolverFactory} to
 * generate the {@link RouteResolver} to use in expansion, then tacks on a default {@link IfrFormatSectionSplitter} and the
 * {@link NoApproachPredictor}.
 */
public final class RouteExpanderFactory {

  private final RouteResolverFactory resolverFactory;

  public RouteExpanderFactory(RouteResolverFactory resolverFactory) {
    this.resolverFactory = checkNotNull(resolverFactory);
  }

  /**
   * Generates a new factory for {@link RouteExpander}s with the given lookup services to be used in the {@link RouteResolver}.
   */
  public static RouteExpanderFactory newFactory(
      LookupService<Fix> fixService,
      LookupService<Airway> airwayService,
      LookupService<Airport> airportService,
      ProcedureService procedureService) {
    return new RouteExpanderFactory(new RouteResolverFactory(fixService, airwayService, airportService, procedureService));
  }

  /**
   * Generates a new factory for {@link RouteExpander}s with the given collections of infrastructure elements to be used in
   * the {@link RouteResolver}.
   */
  public static RouteExpanderFactory newFactory(
      Collection<? extends Fix> fixes,
      Collection<? extends Airway> airways,
      Collection<? extends Airport> airports,
      Collection<? extends Transition> transitions
  ) {
    return new RouteExpanderFactory(new RouteResolverFactory(fixes, airways, airports, transitions));
  }

  /**
   * Generates a new {@link RouteExpander} with no arrival or departure runway predictions.
   */
  public RouteExpander newExpander() {
    return newExpander(RunwayPredictor.noop(), RunwayPredictor.noop());
  }

  /**
   * Generates a new {@link RouteExpander} with the provided arrival and departure runway predictions. With these supplied the
   * expansion will be attempted all the way through the runway transition of the arrival/departure procedures.
   */
  public RouteExpander newExpander(RunwayPredictor arrivalPredictor, RunwayPredictor departurePredictor) {
    return new RouteExpander(
        new IfrFormatSectionSplitter(),
        resolverFactory.newResolver(arrivalPredictor, departurePredictor),
        new NoApproachPredictor()
    );
  }

  public RouteExpander newExpander(RunwayPredictor arrivalPredictor, RunwayPredictor departurePredictor, ApproachPredictor approachPredictor) {
    return new RouteExpander(
        new IfrFormatSectionSplitter(),
        resolverFactory.newResolver(arrivalPredictor, departurePredictor),
        approachPredictor
    );
  }
}
