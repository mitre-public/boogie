package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.arinc.model.*;

/**
 * Functional class for converting certain types of {@link ArincModel} records to {@link Fix} records based on their section and
 * subsection within the database. Currently the FixAssembler supports:
 * <br>
 * 1. Waypoints
 * 2. NDB/VHF Navaids
 * 3. Airports
 * 4. Runways
 * 5. Localizer/GlideSlope records
 * <br>
 * This is order to be reflective of what {@link ArincProcedureLeg}s can reference as "fix-like" objects within a procedure.
 * <br>
 * Standard "fixes" typically only encompass Waypoints + VHF/NDB Navaids.
 */
public final class FixAssembler<F> implements Function<ArincModel, F> {

  private final Function<ArincWaypoint, F> waypointConverter;
  private final Function<ArincNdbNavaid, F> ndbNavaidConverter;
  private final Function<ArincVhfNavaid, F> vhfNavaidConverter;
  private final Function<ArincAirport, F> airportConverter;
  private final Function<ArincRunway, F> runwayConverter;
  private final Function<ArincLocalizerGlideSlope, F> localizerGlideSlopeConverter;

  private final Function<ArincGnssLandingSystem, F> gnssLandingSystemConverter;

  public FixAssembler(
      Function<ArincWaypoint, F> waypointConverter,
      Function<ArincNdbNavaid, F> ndbNavaidConverter,
      Function<ArincVhfNavaid, F> vhfNavaidConverter,
      Function<ArincAirport, F> airportConverter,
      Function<ArincRunway, F> runwayConverter,
      Function<ArincLocalizerGlideSlope, F> localizerGlideSlopeConverter,
      Function<ArincGnssLandingSystem, F> gnssLandingSystemConverter) {
    this.waypointConverter = requireNonNull(waypointConverter);
    this.ndbNavaidConverter = requireNonNull(ndbNavaidConverter);
    this.vhfNavaidConverter = requireNonNull(vhfNavaidConverter);
    this.airportConverter = requireNonNull(airportConverter);
    this.runwayConverter = requireNonNull(runwayConverter);
    this.localizerGlideSlopeConverter = requireNonNull(localizerGlideSlopeConverter);
    this.gnssLandingSystemConverter = requireNonNull(gnssLandingSystemConverter);
  }

  @Override
  public F apply(ArincModel arincModel) {
    String sectionSubSection = arincModel.sectionCode().name().concat(arincModel.subSectionCode().orElse(""));

    // airports
    switch (sectionSubSection) {
      case "PA":
        return airportConverter.apply((ArincAirport) arincModel);
      // Enroute NDB Navaids
      case "DB":
        return ndbNavaidConverter.apply((ArincNdbNavaid) arincModel);
      // Terminal NDB Navaids
      case "PN":
        return ndbNavaidConverter.apply((ArincNdbNavaid) arincModel);
      // VHF Navaids
      case "D":
        return vhfNavaidConverter.apply((ArincVhfNavaid) arincModel);
      // Enroute waypoints
      case "EA":
        return waypointConverter.apply((ArincWaypoint) arincModel);
      // Terminal waypoints
      case "PC":
        return waypointConverter.apply((ArincWaypoint) arincModel);
      // runways - generally terminal fix of the final fix of the final approach portion of an approach procedure (or centerFix of an RF)
      case "PG":
        return runwayConverter.apply((ArincRunway) arincModel);
      // localizerGlideSlopes - generally used as a recommended navaid on some approaches
      case "PI":
        return localizerGlideSlopeConverter.apply((ArincLocalizerGlideSlope) arincModel);
      //gnss landing systems - usually used on gls approach or as rec navs rarely
      case "PT":
        return gnssLandingSystemConverter.apply((ArincGnssLandingSystem) arincModel);
      // anything else is not explicitly supported as a reference object in a leg
      default:
        throw new IllegalStateException("Unknown referenced section/subsection for lookup of location: ".concat(sectionSubSection));
    }
  }
}
