package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.arinc.database.FixDatabase;
import org.mitre.tdp.boogie.arinc.database.TerminalAreaDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincAirport;
import org.mitre.tdp.boogie.arinc.model.ArincLocalizerGlideSlope;
import org.mitre.tdp.boogie.arinc.model.ArincNdbNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;
import org.mitre.tdp.boogie.arinc.model.ArincVhfNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincWaypoint;
import org.mitre.tdp.boogie.arinc.utils.AiracCycle;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.model.BoogieFix;
import org.mitre.tdp.boogie.util.Declinations;

/**
 * This class decorates a {@link TerminalAreaDatabase} and a {@link FixDatabase} to farm out queries for fixes as referenced in
 * various legs (of procedures or airways) to the appropriate lookup method.
 * <br>
 * This class is hitting NDB/VHF navaids, waypoints, airports, runways, and localizers to find the appropriate database entry to
 * populate the leg fields and returns the result of those lookups as {@link Fix} records.
 */
final class LegFixDereferencer {

  private final TerminalAreaDatabase terminalAreaDatabase;
  private final FixDatabase fixDatabase;

  LegFixDereferencer(TerminalAreaDatabase terminalAreaDatabase, FixDatabase fixDatabase) {
    this.terminalAreaDatabase = requireNonNull(terminalAreaDatabase);
    this.fixDatabase = requireNonNull(fixDatabase);
  }

  /**
   * We allow the airport to be nullable so this signature can be used in conjunction with the {@link AirwayAssembler}.
   */
  Optional<Fix> dereference(String identifier, @Nullable String airport, String icaoRegion, SectionCode sectionCode, @Nullable String subSectionCode) {
    String sectionSubSection = sectionCode.name().concat(Optional.ofNullable(subSectionCode).orElse(""));

    // airports
    if ("PA".equals(sectionSubSection)) {
      return fixDatabase.airport(identifier, icaoRegion).map(this::toFix);
    }
    // Enroute NDB Navaids
    else if ("DB".equals(sectionSubSection)) {
      return fixDatabase.enrouteNdbNavaid(identifier, icaoRegion).map(this::toFix);
    }
    // Terminal NDB Navaids
    else if ("PN".equals(sectionSubSection)) {
      return fixDatabase.terminalNdbNavaid(identifier, icaoRegion).map(this::toFix);
    }
    // VHF Navaids
    else if ("D".equals(sectionSubSection)) {
      return fixDatabase.vhfNavaid(identifier, icaoRegion).map(this::toFix);
    }
    // Enroute waypoints
    else if ("EA".equals(sectionSubSection)) {
      return fixDatabase.enrouteWaypoint(identifier, icaoRegion).map(this::toFix);
    }
    // Terminal waypoints
    else if ("PC".equals(sectionSubSection)) {
      return fixDatabase.terminalWaypoint(identifier, icaoRegion).map(this::toFix);
    }
    // runways - generally terminal fix of the final fix of the final approach portion of an approach procedure (or centerFix of an RF)
    else if ("PG".equals(sectionSubSection)) {
      return airport == null ? Optional.empty() : terminalAreaDatabase.runwayAt(airport, identifier).map(this::toFix);
    }
    // localizerGlideSlopes - generally used as a recommended navaid on some approaches
    else if ("PI".equals(sectionSubSection)) {
      return airport == null ? Optional.empty() : terminalAreaDatabase.localizerGlideSlopeAt(airport, identifier).map(this::toFix);
    }
    // anything else is not explicitly supported as a reference object in a leg
    else {
      throw new IllegalStateException("Unknown referenced section/subsection for lookup of location: ".concat(sectionSubSection));
    }
  }

  Fix toFix(ArincWaypoint waypoint) {

    double modeledDeclination = Declinations.declination(
        waypoint.latitude(),
        waypoint.longitude(),
        null,
        AiracCycle.startDate(waypoint.lastUpdateCycle())
    );

    return new BoogieFix.Builder()
        .fixIdentifier(waypoint.waypointIdentifier())
        .fixRegion(waypoint.waypointIcaoRegion())
        .latitude(waypoint.latitude())
        .longitude(waypoint.longitude())
        .publishedVariation(waypoint.magneticVariation().orElse(null))
        .modeledVariation(modeledDeclination)
        .build();
  }

  Fix toFix(ArincNdbNavaid ndbNavaid) {
    double modeledDeclination = Declinations.declination(
        ndbNavaid.latitude(),
        ndbNavaid.longitude(),
        null,
        AiracCycle.startDate(ndbNavaid.lastUpdateCycle())
    );

    return new BoogieFix.Builder()
        .fixIdentifier(ndbNavaid.ndbIdentifier())
        .fixRegion(ndbNavaid.ndbIcaoRegion())
        .latitude(ndbNavaid.latitude())
        .longitude(ndbNavaid.longitude())
        .publishedVariation(ndbNavaid.magneticVariation().orElse(null))
        .modeledVariation(modeledDeclination)
        .build();
  }

  Fix toFix(ArincVhfNavaid vhfNavaid) {
    double modeledDeclination = Declinations.declination(
        vhfNavaid.latitude(),
        vhfNavaid.longitude(),
        vhfNavaid.dmeElevation().orElse(null),
        AiracCycle.startDate(vhfNavaid.lastUpdateCycle())
    );

    return new BoogieFix.Builder()
        .fixIdentifier(vhfNavaid.vhfIdentifier())
        .fixRegion(vhfNavaid.vhfIcaoRegion())
        .latitude(vhfNavaid.latitude())
        .longitude(vhfNavaid.longitude())
        .elevation(vhfNavaid.dmeElevation().orElse(null))
        .publishedVariation(vhfNavaid.stationDeclination().orElse(null))
        .modeledVariation(modeledDeclination)
        .build();
  }

  Fix toFix(ArincAirport airport) {
    double modeledDeclination = Declinations.declination(
        airport.latitude(),
        airport.longitude(),
        airport.airportElevation().orElse(null),
        AiracCycle.startDate(airport.lastUpdateCycle())
    );

    return new BoogieFix.Builder()
        .fixIdentifier(airport.airportIdentifier())
        .fixRegion(airport.airportIcaoRegion())
        .latitude(airport.latitude())
        .longitude(airport.longitude())
        .elevation(airport.airportElevation().orElse(null))
        .publishedVariation(airport.magneticVariation().orElse(null))
        .modeledVariation(modeledDeclination)
        .build();
  }

  Fix toFix(ArincRunway runway) {
    double modeledDeclination = Declinations.declination(
        runway.latitude(),
        runway.longitude(),
        runway.landingThresholdElevation().map(Integer::doubleValue).orElse(null),
        AiracCycle.startDate(runway.lastUpdateCycle())
    );

    return new BoogieFix.Builder()
        .fixIdentifier(runway.runwayIdentifier())
        .fixRegion(runway.airportIcaoRegion())
        .latitude(runway.latitude())
        .longitude(runway.longitude())
        .elevation(runway.landingThresholdElevation().map(Integer::doubleValue).orElse(null))
        .modeledVariation(modeledDeclination)
        .build();
  }

  Fix toFix(ArincLocalizerGlideSlope localizerGlideSlope) {

    double modeledDeclination = Declinations.declination(
        localizerGlideSlope.localizerLatitude().orElseGet(() -> localizerGlideSlope.glideSlopeLatitude().orElseThrow(IllegalStateException::new)),
        localizerGlideSlope.localizerLongitude().orElseGet(() -> localizerGlideSlope.glideSlopeLongitude().orElseThrow(IllegalStateException::new)),
        localizerGlideSlope.glideSlopeElevation().orElse(null),
        AiracCycle.startDate(localizerGlideSlope.lastUpdateCycle())
    );

    return new BoogieFix.Builder()
        .fixIdentifier(localizerGlideSlope.localizerIdentifier())
        .fixRegion(localizerGlideSlope.airportIcaoRegion())
        .latitude(localizerGlideSlope.localizerLatitude().orElseGet(() -> localizerGlideSlope.glideSlopeLatitude().orElseThrow(IllegalStateException::new)))
        .longitude(localizerGlideSlope.localizerLongitude().orElseGet(() -> localizerGlideSlope.glideSlopeLongitude().orElseThrow(IllegalStateException::new)))
        .elevation(localizerGlideSlope.glideSlopeElevation().orElse(null))
        .publishedVariation(localizerGlideSlope.stationDeclination().orElse(null))
        .modeledVariation(modeledDeclination)
        .build();
  }
}
