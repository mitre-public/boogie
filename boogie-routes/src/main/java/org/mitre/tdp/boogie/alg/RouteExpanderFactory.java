package org.mitre.tdp.boogie.alg;

import static java.util.Objects.requireNonNull;

import java.util.Collection;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.alg.chooser.GraphBasedRouteChooser;
import org.mitre.tdp.boogie.alg.resolve.AirportResolver;
import org.mitre.tdp.boogie.alg.resolve.AirwayResolver;
import org.mitre.tdp.boogie.alg.resolve.FixResolver;
import org.mitre.tdp.boogie.alg.resolve.LatLonResolver;
import org.mitre.tdp.boogie.alg.resolve.SectionResolver;
import org.mitre.tdp.boogie.alg.resolve.SidStarResolver;
import org.mitre.tdp.boogie.alg.split.IfrFormatSectionSplitter;
import org.mitre.tdp.boogie.validate.EnforceSequentiallyOrderedLegs;

public final class RouteExpanderFactory {

  private RouteExpanderFactory() {
    throw new IllegalStateException("Cannot instantiate static factory class.");
  }

  /**
   * Returns a new instance of a {@link RouteExpander} configured with the:
   * <br>
   * 1. {@link IfrFormatSectionSplitter} - which is the common format for domestic FAA flight plans
   * 2. {@link GraphBasedRouteChooser} - which leverages shortest path algorithms to resolve the flown routes from a collection
   * of known matching infrastructure elements.
   */
  public static RouteExpander newGraphicalRouteExpander(
      LookupService<Fix> fixService,
      LookupService<Airway> airwayService,
      LookupService<Airport> airportService,
      LookupService<Procedure> procedureService) {
    return new RouteExpander(
        IfrFormatSectionSplitter.INSTANCE,
        fixService,
        airwayService,
        airportService,
        procedureService,
        LookupService.noop(),
        new GraphBasedRouteChooser()
    );
  }

  /**
   * Returns a new instance of a {@link RouteExpander} configured with the:
   * <br>
   * 1. {@link IfrFormatSectionSplitter} - which is the common format for domestic FAA flight plans
   * 2. {@link GraphBasedRouteChooser} - which leverages shortest path algorithms to resolve the flown routes from a collection
   * of known matching infrastructure elements.
   */
  public static RouteExpander newGraphicalRouteExpander(
      LookupService<Fix> fixService,
      LookupService<Airway> airwayService,
      LookupService<Airport> airportService,
      LookupService<Procedure> procedureService,
      LookupService<Procedure> proceduresAtAirport) {
    return new RouteExpander(
        IfrFormatSectionSplitter.INSTANCE,
        fixService,
        airwayService,
        airportService,
        procedureService,
        proceduresAtAirport,
        new GraphBasedRouteChooser()
    );
  }

  /**
   * Same implementation as {@link #newGraphicalRouteExpander(LookupService, LookupService, LookupService, LookupService)} with
   * modified signature to allow internal instantiation of the {@link LookupService}s.
   */
  public static RouteExpander newGraphicalRouteExpander(
      Collection<? extends Fix> fixes,
      Collection<? extends Airway> airways,
      Collection<? extends Airport> airports,
      Collection<? extends Procedure> procedures) {

    // check expectations on leg ordering before they're handed off to the RouteExpander
    airways.forEach(airway -> EnforceSequentiallyOrderedLegs.INSTANCE.accept(airway.legs()));
    procedures.forEach(procedure -> procedure.transitions().forEach(transition -> EnforceSequentiallyOrderedLegs.INSTANCE.accept(transition.legs())));

    return newGraphicalRouteExpander(
        // ugh collection type casting... makes it nicer to use though...
        (LookupService<Fix>) DefaultLookupService.newLookupService(fixes, Fix::fixIdentifier),
        (LookupService<Airway>) DefaultLookupService.newLookupService(airways, Airway::airwayIdentifier),
        (LookupService<Airport>) DefaultLookupService.newLookupService(airports, Airport::airportIdentifier),
        (LookupService<Procedure>) DefaultLookupService.newLookupService(procedures, Procedure::procedureIdentifier),
        (LookupService<Procedure>) DefaultLookupService.newLookupService(procedures, Procedure::airportIdentifier)
    );
  }

  /**
   * Implementation of a {@link SectionResolver} for all standard portions of a route string.
   * <br>
   * 1. Fixes (Tailored/Waypoint/Navaids)
   * 2. Procedures (SID/STAR)
   * 3. Airways
   * 4. Airports
   * 5. Lat/Lon Literals
   * <br>
   * If the {@link LookupService} for {@link Procedure} records is secondarily indexed on airport then it will be used for approach
   * procedure resolution when the expander is given a runway.
   */
  public static SectionResolver newStandardSectionResolver(
      LookupService<Fix> fixService,
      LookupService<Airway> airwayService,
      LookupService<Airport> airportService,
      LookupService<Procedure> procedureService
  ) {
    return SectionResolver.composeAll(
        new FixResolver(requireNonNull(fixService)),
        new AirwayResolver(requireNonNull(airwayService)),
        new AirportResolver(requireNonNull(airportService)),
        new SidStarResolver(requireNonNull(procedureService)),
        new LatLonResolver()
    );
  }

  /**
   * Alternate signature for {@link #newStandardSectionResolver(LookupService, LookupService, LookupService, LookupService)}.
   */
  public static SectionResolver newStandardSectionResolver(
      Collection<? extends Fix> fixes,
      Collection<? extends Airway> airways,
      Collection<? extends Airport> airports,
      Collection<? extends Procedure> transitions
  ) {
    return newStandardSectionResolver(
        (LookupService<Fix>) DefaultLookupService.newLookupService(fixes, Fix::fixIdentifier),
        (LookupService<Airway>) DefaultLookupService.newLookupService(airways, Airway::airwayIdentifier),
        (LookupService<Airport>) DefaultLookupService.newLookupService(airports, Airport::airportIdentifier),
        // multi-index the procedures within the same lookup service - this feels a bit hacky but it allows us another reliable
        // way to look-up procedure information for approach resolution
        (LookupService<Procedure>) DefaultLookupService.newLookupService(transitions, Procedure::procedureIdentifier)
    );
  }
}
