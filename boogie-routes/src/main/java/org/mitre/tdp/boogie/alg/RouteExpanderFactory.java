package org.mitre.tdp.boogie.alg;

import java.util.Collection;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.alg.chooser.GraphBasedRouteChooser;
import org.mitre.tdp.boogie.alg.chooser.RouteChooser;
import org.mitre.tdp.boogie.alg.resolve.SectionResolver;
import org.mitre.tdp.boogie.alg.split.SectionSplitter;
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
    return newGraphicalRouteExpander(SectionSplitter.faaIfrFormat(), fixService, airwayService, airportService, procedureService);
  }

  /**
   * Functionally identical to the {@link #newGraphicalRouteExpander(LookupService, LookupService, LookupService, LookupService)}
   * but allowing substitution of one of the custom {@link SectionSplitter} implementations.
   * <ul>
   *   <li>{@link IfrFormatSectionSplitter} - for internal NAS-format routes</li>
   *   <li>{@link InternationalIfrFormatSectionSplitter} - or international-format routes</li>
   * </ul>
   */
  public static RouteExpander newGraphicalRouteExpander(
      SectionSplitter sectionSplitter,
      LookupService<Fix> fixService,
      LookupService<Airway> airwayService,
      LookupService<Airport> airportService,
      LookupService<Procedure> procedureService
  ) {
    return new RouteExpander(
        sectionSplitter,
        procedureService,
        LookupService.noop(),
        SectionResolver.composeAll(
            SectionResolver.fix(fixService),
            SectionResolver.airway(airwayService),
            SectionResolver.airport(airportService),
            SectionResolver.sidStar(procedureService),
            SectionResolver.latlong(null)),
        RouteChooser.graphical()
    );
  }

  /**
   * Functionally identical to the {@link #newGraphicalRouteExpander(LookupService, LookupService, LookupService, LookupService)}
   * but allowing substitution of one of the custom {@link SectionSplitter} implementations and will throw errors if the section
   * resolver fails to find e.g., a waypoint.
   * <ul>
   *   <li>{@link IfrFormatSectionSplitter} - for internal NAS-format routes</li>
   *   <li>{@link InternationalIfrFormatSectionSplitter} - or international-format routes</li>
   * </ul>
   */
  public static RouteExpander newSurlyGraphicalRouteExpander(
      SectionSplitter sectionSplitter,
      LookupService<Fix> fixService,
      LookupService<Airway> airwayService,
      LookupService<Airport> airportService,
      LookupService<Procedure> procedureService
  ) {
    return new RouteExpander(
        sectionSplitter,
        procedureService,
        LookupService.noop(),
        SectionResolver.surly(SectionResolver.composeAll(
            SectionResolver.fix(fixService),
            SectionResolver.airway(airwayService),
            SectionResolver.airport(airportService),
            SectionResolver.sidStar(procedureService),
            SectionResolver.latlong(null))),
        RouteChooser.graphical()
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
    return newGraphicalRouteExpander(SectionSplitter.faaIfrFormat(), fixService, airwayService, airportService, procedureService, proceduresAtAirport);
  }

  /**
   * Returns a new instance of a {@link RouteExpander} configured with the:
   * <br>
   * 1. {@link IfrFormatSectionSplitter} - which is the common format for domestic FAA flight plans
   * 2. {@link GraphBasedRouteChooser} - which leverages shortest path algorithms to resolve the flown routes from a collection
   * of known matching infrastructure elements.
   */
  public static RouteExpander newSurlyGraphicalRouteExpander(
      LookupService<Fix> fixService,
      LookupService<Airway> airwayService,
      LookupService<Airport> airportService,
      LookupService<Procedure> procedureService,
      LookupService<Procedure> proceduresAtAirport) {
    return newSurlyGraphicalRouteExpander(SectionSplitter.faaIfrFormat(), fixService, airwayService, airportService, procedureService, proceduresAtAirport);
  }

  /**
   * Functionally identical to the {@link #newSurlyGraphicalRouteExpander(LookupService, LookupService, LookupService, LookupService, LookupService)}
   * but allowing substitution of one of the custom {@link SectionSplitter} implementations.
   * <ul>
   *   <li>{@link IfrFormatSectionSplitter} - for internal NAS-format routes</li>
   *   <li>{@link InternationalIfrFormatSectionSplitter} - or international-format routes</li>
   * </ul>
   */
  public static RouteExpander newSurlyGraphicalRouteExpander(
      SectionSplitter sectionSplitter,
      LookupService<Fix> fixService,
      LookupService<Airway> airwayService,
      LookupService<Airport> airportService,
      LookupService<Procedure> procedureService,
      LookupService<Procedure> proceduresAtAirport) {
    return new RouteExpander(
        sectionSplitter,
        procedureService,
        proceduresAtAirport,
        SectionResolver.surly(SectionResolver.composeAll(
            SectionResolver.fix(fixService),
            SectionResolver.airway(airwayService),
            SectionResolver.airport(airportService),
            SectionResolver.sidStar(procedureService),
            SectionResolver.latlong(null))
        ),
        RouteChooser.graphical()
    );
  }

  /**
   * Functionally identical to the {@link #newGraphicalRouteExpander(LookupService, LookupService, LookupService, LookupService, LookupService)}
   * but allowing substitution of one of the custom {@link SectionSplitter} implementations.
   * <ul>
   *   <li>{@link IfrFormatSectionSplitter} - for internal NAS-format routes</li>
   *   <li>{@link InternationalIfrFormatSectionSplitter} - or international-format routes</li>
   * </ul>
   */
  public static RouteExpander newGraphicalRouteExpander(
      SectionSplitter sectionSplitter,
      LookupService<Fix> fixService,
      LookupService<Airway> airwayService,
      LookupService<Airport> airportService,
      LookupService<Procedure> procedureService,
      LookupService<Procedure> proceduresAtAirport) {
    return new RouteExpander(
        sectionSplitter,
        procedureService,
        proceduresAtAirport,
        SectionResolver.composeAll(
            SectionResolver.fix(fixService),
            SectionResolver.airway(airwayService),
            SectionResolver.airport(airportService),
            SectionResolver.sidStar(procedureService),
            SectionResolver.latlong(null)
        ),
        RouteChooser.graphical()
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
    return newGraphicalRouteExpander(SectionSplitter.faaIfrFormat(), fixes, airways, airports, procedures);
  }

  /**
   * Same implementation as {@link #newGraphicalRouteExpander(LookupService, LookupService, LookupService, LookupService)} with
   * modified signature to allow internal instantiation of the {@link LookupService}s, except this has surly section resolvers
   */
  public static RouteExpander newSurlyGraphicalRouteExpander(
      Collection<? extends Fix> fixes,
      Collection<? extends Airway> airways,
      Collection<? extends Airport> airports,
      Collection<? extends Procedure> procedures) {
    return newSurlyGraphicalRouteExpander(SectionSplitter.faaIfrFormat(), fixes, airways, airports, procedures);
  }

  /**
   * Functionally identical to the {@link #newGraphicalRouteExpander(Collection, Collection, Collection, Collection)} but allowing
   * substitution of one of the custom {@link SectionSplitter} implementations.
   * <ul>
   *   <li>{@link IfrFormatSectionSplitter} - for internal NAS-format routes</li>
   *   <li>{@link InternationalIfrFormatSectionSplitter} - or international-format routes</li>
   * </ul>
   */
  public static RouteExpander newGraphicalRouteExpander(
      SectionSplitter sectionSplitter,
      Collection<? extends Fix> fixes,
      Collection<? extends Airway> airways,
      Collection<? extends Airport> airports,
      Collection<? extends Procedure> procedures) {

    // check expectations on leg ordering before they're handed off to the RouteExpander
    airways.forEach(airway -> EnforceSequentiallyOrderedLegs.INSTANCE.accept(airway.legs()));
    procedures.forEach(procedure -> procedure.transitions().forEach(transition -> EnforceSequentiallyOrderedLegs.INSTANCE.accept(transition.legs())));

    return newGraphicalRouteExpander(
        sectionSplitter,
        // ugh collection type casting... makes it nicer to use though...
        (LookupService<Fix>) LookupService.inMemory(fixes, f -> Stream.of(f.fixIdentifier())),
        (LookupService<Airway>) LookupService.inMemory(airways, a -> Stream.of(a.airwayIdentifier())),
        (LookupService<Airport>) LookupService.inMemory(airports, a -> Stream.of(a.airportIdentifier())),
        (LookupService<Procedure>) LookupService.inMemory(procedures, p -> Stream.of(p.procedureIdentifier())),
        (LookupService<Procedure>) LookupService.inMemory(procedures, p -> Stream.of(p.airportIdentifier()))
    );
  }

  /**
   * Functionally identical to the {@link #newGraphicalRouteExpander(Collection, Collection, Collection, Collection)} but allowing
   * substitution of one of the custom {@link SectionSplitter} implementations and uses the surly section resolver which
   * will throw an error if a section does not resolve.
   * <ul>
   *   <li>{@link IfrFormatSectionSplitter} - for internal NAS-format routes</li>
   *   <li>{@link InternationalIfrFormatSectionSplitter} - or international-format routes</li>
   * </ul>
   */
  public static RouteExpander newSurlyGraphicalRouteExpander(
      SectionSplitter sectionSplitter,
      Collection<? extends Fix> fixes,
      Collection<? extends Airway> airways,
      Collection<? extends Airport> airports,
      Collection<? extends Procedure> procedures) {

    // check expectations on leg ordering before they're handed off to the RouteExpander
    airways.forEach(airway -> EnforceSequentiallyOrderedLegs.INSTANCE.accept(airway.legs()));
    procedures.forEach(procedure -> procedure.transitions().forEach(transition -> EnforceSequentiallyOrderedLegs.INSTANCE.accept(transition.legs())));

    return newSurlyGraphicalRouteExpander(
        sectionSplitter,
        // ugh collection type casting... makes it nicer to use though...
        (LookupService<Fix>) LookupService.inMemory(fixes, f -> Stream.of(f.fixIdentifier())),
        (LookupService<Airway>) LookupService.inMemory(airways, a -> Stream.of(a.airwayIdentifier())),
        (LookupService<Airport>) LookupService.inMemory(airports, a -> Stream.of(a.airportIdentifier())),
        (LookupService<Procedure>) LookupService.inMemory(procedures, p -> Stream.of(p.procedureIdentifier())),
        (LookupService<Procedure>) LookupService.inMemory(procedures, p -> Stream.of(p.airportIdentifier()))
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
        SectionResolver.fix(fixService),
        SectionResolver.airway(airwayService),
        SectionResolver.airport(airportService),
        SectionResolver.sidStar(procedureService),
        SectionResolver.latlong(null)
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
        (LookupService<Fix>) LookupService.inMemory(fixes, f -> Stream.of(f.fixIdentifier())),
        (LookupService<Airway>) LookupService.inMemory(airways, a -> Stream.of(a.airwayIdentifier())),
        (LookupService<Airport>) LookupService.inMemory(airports, a -> Stream.of(a.airportIdentifier())),
        // multi-index the procedures within the same lookup service - this feels a bit hacky but it allows us another reliable
        // way to look-up procedure information for approach resolution
        (LookupService<Procedure>) LookupService.inMemory(transitions, p -> Stream.of(p.procedureIdentifier()))
    );
  }
}
