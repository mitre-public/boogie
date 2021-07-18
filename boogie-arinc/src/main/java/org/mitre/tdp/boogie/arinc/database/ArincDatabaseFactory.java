package org.mitre.tdp.boogie.arinc.database;

import java.util.Collection;

import org.mitre.tdp.boogie.arinc.model.ArincAirport;
import org.mitre.tdp.boogie.arinc.model.ArincLocalizerGlideSlope;
import org.mitre.tdp.boogie.arinc.model.ArincNdbNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;
import org.mitre.tdp.boogie.arinc.model.ArincVhfNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincWaypoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.LinkedHashMultimap;

/**
 * Factory class for generating a variety of
 */
public final class ArincDatabaseFactory {

  private static final Logger LOG = LoggerFactory.getLogger(ArincDatabaseFactory.class);

  private ArincDatabaseFactory() {
    throw new IllegalStateException("Cannot instantiate static factory class.");
  }


  /**
   * Factory method for generating a new {@link FixDatabase} which indexes various fix-like ARINC data models by:
   * <br>
   * 1. Identifier
   * 2. ICAO Region
   * 3. Section
   * 4. SubSection (if present for the record type)
   */
  public static FixDatabase newFixDatabase(
      Collection<ArincNdbNavaid> ndbNavaids,
      Collection<ArincVhfNavaid> vhfNavaids,
      Collection<ArincWaypoint> waypoints,
      Collection<ArincAirport> airports) {
    return new FixDatabase(LinkedHashMultimap.create());
  }

  /**
   * Factory method for generating a new {@link TerminalAreaDatabase} which indexes various ARINC records native to Airports by
   * their associated airport identifier and ICAO region.
   */
  public static TerminalAreaDatabase newTerminalAreaDatabase(
      Collection<ArincAirport> airports,
      Collection<ArincRunway> runways,
      Collection<ArincLocalizerGlideSlope> localizerGlideSlopes,
      Collection<ArincNdbNavaid> ndbNavaids,
      Collection<ArincVhfNavaid> vhfNavaids,
      Collection<ArincWaypoint> waypoints,
      Collection<ArincProcedureLeg> procedureLegs) {
    return new TerminalAreaDatabase(LinkedHashMultimap.create());
  }
}
