package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.arinc.model.*;
import org.mitre.tdp.boogie.model.BoogieFix;

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
public final class FixAssembler implements Function<ArincModel, Fix> {

  /**
   * Singleton instance generating default {@link Fix} implementations based on the {@link BoogieFix} data model.
   */
  public static final FixAssembler INSTANCE = new FixAssembler();

  private final Function<ArincWaypoint, Fix> waypointConverter;
  private final Function<ArincNdbNavaid, Fix> ndbNavaidConverter;
  private final Function<ArincVhfNavaid, Fix> vhfNavaidConverter;
  private final Function<ArincAirport, Fix> airportConverter;
  private final Function<ArincRunway, Fix> runwayConverter;
  private final Function<ArincLocalizerGlideSlope, Fix> localizerGlideSlopeConverter;

  private final Function<ArincGnssLandingSystem, Fix> gnssLandingSystemConverter;

  private FixAssembler() {
    this(
        ArincToBoogieConverterFactory::newFixFrom,
        ArincToBoogieConverterFactory::newFixFrom,
        ArincToBoogieConverterFactory::newFixFrom,
        ArincToBoogieConverterFactory::newFixFrom,
        ArincToBoogieConverterFactory::newFixFrom,
        ArincToBoogieConverterFactory::newFixFrom,
        ArincToBoogieConverterFactory::newFixFrom
    );
  }

  public FixAssembler(
      Function<ArincWaypoint, Fix> waypointConverter,
      Function<ArincNdbNavaid, Fix> ndbNavaidConverter,
      Function<ArincVhfNavaid, Fix> vhfNavaidConverter,
      Function<ArincAirport, Fix> airportConverter,
      Function<ArincRunway, Fix> runwayConverter,
      Function<ArincLocalizerGlideSlope, Fix> localizerGlideSlopeConverter,
      Function<ArincGnssLandingSystem, Fix> gnssLandingSystemConverter) {
    this.waypointConverter = requireNonNull(waypointConverter);
    this.ndbNavaidConverter = requireNonNull(ndbNavaidConverter);
    this.vhfNavaidConverter = requireNonNull(vhfNavaidConverter);
    this.airportConverter = requireNonNull(airportConverter);
    this.runwayConverter = requireNonNull(runwayConverter);
    this.localizerGlideSlopeConverter = requireNonNull(localizerGlideSlopeConverter);
    this.gnssLandingSystemConverter = requireNonNull(gnssLandingSystemConverter);
  }

  @Override
  public Fix apply(ArincModel arincModel) {
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
