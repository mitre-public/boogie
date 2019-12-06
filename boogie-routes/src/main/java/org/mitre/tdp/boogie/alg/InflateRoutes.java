package org.mitre.tdp.boogie.alg;

import java.time.Instant;
import java.util.Collection;

import com.google.common.base.Preconditions;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.alg.resolve.SectionResolver;
import org.mitre.tdp.boogie.alg.split.SectionSplitter;
import org.mitre.tdp.boogie.models.Procedure;
import org.mitre.tdp.boogie.service.LookupService;
import org.mitre.tdp.boogie.service.impl.AirportService;
import org.mitre.tdp.boogie.service.impl.AirwayService;
import org.mitre.tdp.boogie.service.impl.FixService;
import org.mitre.tdp.boogie.service.impl.ProcedureGraphService;

/**
 * The core route inflation algorithm in TDP. This class is configured with a single
 * cycle of procedure data assumed to be overlapping with the date associated with
 * the provided route string.
 *
 * The algorithm leverages the component algorithms present in order:
 *
 * 1) {@link SectionSplitter} to split the provided route string into its components
 *
 * 2) {@link SectionResolver} to associate split sections of the route with infrastructure
 * (e.g. procedures, waypoints, etc.)
 *
 * 3) {@link LegGraph} to determine the path the flight most likely took through
 * the resolved infrastructure elements (e.g. resolving references to navaids that
 * appear with the same name in multiple ICAO regions)
 *
 * The class then finishes by publishing the output to the {@link FiledRoute}
 * object which may then be tagged with more source specific information.
 */
public final class InflateRoutes {

  private final LookupService<Fix> fixService;
  private final LookupService<Airway> airwayService;
  private final LookupService<Airport> airportService;
  private final LookupService<Procedure> procedureService;

  private InflateRoutes(LookupService<Fix> fs, LookupService<Airway> ws, LookupService<Airport> as, LookupService<Procedure> ps) {
    this.fixService = fs;
    this.airwayService = ws;
    this.airportService = as;
    this.procedureService = ps;
  }

  public LookupService<Fix> fixService() {
    return fixService;
  }

  public LookupService<Airway> airwayService() {
    return airwayService;
  }

  public LookupService<Airport> airportService() {
    return airportService;
  }

  public LookupService<Procedure> procedureService() {
    return procedureService;
  }

  public static InflateRoutes with(Collection<? extends Fix> fixes, Collection<? extends Airway> airways, Collection<? extends Airport> airports, Collection<? extends Transition> transitions) {
    FixService fs = FixService.with(fixes);
    AirwayService ws = AirwayService.with(airways);
    AirportService as = AirportService.with(airports);
    LookupService<Procedure> ps = ProcedureGraphService.with(transitions);
    return new InflateRoutes(fs, ws, as, ps);
  }
}
