package org.mitre.tdp.boogie.arinc.model;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * An immutable, point-in-time result produced by a {@link ConvertingArincRecordConsumer}. Non-procedure collections contain
 * distinct records in encounter order; procedure legs preserve every converted record in encounter order.
 */
public record ConvertedArincRecords(
    List<ArincAirport> arincAirports,
    List<ArincAirportPrimaryExtension> arincAirportExtensions,
    List<ArincRunway> arincRunways,
    List<ArincLocalizerGlideSlope> arincLocalizerGlideSlopes,
    List<ArincNdbNavaid> arincNdbNavaids,
    List<ArincVhfNavaid> arincVhfNavaids,
    List<ArincWaypoint> arincWaypoints,
    List<ArincAirwayLeg> arincAirwayLegs,
    List<ArincProcedureLeg> arincProcedureLegs,
    List<ArincGnssLandingSystem> arincGnssLandingSystems,
    List<ArincHoldingPattern> arincHoldingPatterns,
    List<ArincFirUirLeg> arincFirUirLegs,
    List<ArincHelipad> arincHelipads,
    List<ArincControlledAirspaceLeg> arincControlledAirspaceLegs,
    List<ArincRestrictiveAirspaceLeg> arincRestrictiveAirspaceLegs,
    Optional<ArincHeaderOne> arincHeaderOne,
    List<ArincHeliport> arincHeliports
) {

  public ConvertedArincRecords {
    arincAirports = List.copyOf(arincAirports);
    arincAirportExtensions = List.copyOf(arincAirportExtensions);
    arincRunways = List.copyOf(arincRunways);
    arincLocalizerGlideSlopes = List.copyOf(arincLocalizerGlideSlopes);
    arincNdbNavaids = List.copyOf(arincNdbNavaids);
    arincVhfNavaids = List.copyOf(arincVhfNavaids);
    arincWaypoints = List.copyOf(arincWaypoints);
    arincAirwayLegs = List.copyOf(arincAirwayLegs);
    arincProcedureLegs = List.copyOf(arincProcedureLegs);
    arincGnssLandingSystems = List.copyOf(arincGnssLandingSystems);
    arincHoldingPatterns = List.copyOf(arincHoldingPatterns);
    arincFirUirLegs = List.copyOf(arincFirUirLegs);
    arincHelipads = List.copyOf(arincHelipads);
    arincControlledAirspaceLegs = List.copyOf(arincControlledAirspaceLegs);
    arincRestrictiveAirspaceLegs = List.copyOf(arincRestrictiveAirspaceLegs);
    requireNonNull(arincHeaderOne);
    arincHeliports = List.copyOf(arincHeliports);
  }
}
