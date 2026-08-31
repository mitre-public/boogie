package org.mitre.tdp.boogie.arinc.model;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

/**
 * An immutable, point-in-time result produced by a {@link ConvertingArincRecordConsumer}.
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
    arincHeaderOne = requireNonNull(arincHeaderOne);
    arincHeliports = List.copyOf(arincHeliports);
  }
}
