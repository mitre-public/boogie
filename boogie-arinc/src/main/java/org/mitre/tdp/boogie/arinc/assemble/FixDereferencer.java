package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.arinc.database.ArincFixDatabase;
import org.mitre.tdp.boogie.arinc.database.ArincTerminalAreaDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincModel;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

/**
 * This class decorates a {@link ArincTerminalAreaDatabase} and a {@link ArincFixDatabase} to farm out queries for fixes as referenced in
 * various legs (of procedures or airways) to the appropriate lookup method.
 * <br>
 * This class is hitting NDB/VHF navaids, waypoints, airports, runways, and localizers to find the appropriate database entry to
 * populate the leg fields and returns the result of those lookups as {@link Fix} records.
 */
public final class FixDereferencer<F> {

  /**
   * Function to use to convert the selected {@link ArincModel} to the appropriate {@link Fix} implementation.
   * <br>
   * Generally speaking this will either be the {@link FixAssembler} or some version of a fix assembler with injected
   * converters.
   */
  private final FixAssembler<F> fixAssembler;
  private final ArincTerminalAreaDatabase arincTerminalAreaDatabase;
  private final ArincFixDatabase arincFixDatabase;

  FixDereferencer(FixAssembler<F> fixAssembler, ArincTerminalAreaDatabase arincTerminalAreaDatabase, ArincFixDatabase arincFixDatabase) {
    this.fixAssembler = requireNonNull(fixAssembler);
    this.arincTerminalAreaDatabase = requireNonNull(arincTerminalAreaDatabase);
    this.arincFixDatabase = requireNonNull(arincFixDatabase);
  }

  Optional<F> dereferenceNavaid(String identifier, String icaoRegion) {
    F navaid = dereference(identifier, null, icaoRegion, SectionCode.D, null).orElseGet(() -> dereference(identifier, null, icaoRegion, SectionCode.D, "B").orElseGet(() -> dereference(identifier, null, icaoRegion, SectionCode.P, "n").orElse(null)));
    return Optional.ofNullable(navaid);
  }

  /**
   * We allow the airport to be nullable so this signature can be used in conjunction with the {@link AirwayAssembler}.
   */
  Optional<F> dereference(String identifier, @Nullable String airport, String icaoRegion, SectionCode sectionCode, @Nullable String subSectionCode) {
    String sectionSubSection = sectionCode.name().concat(Optional.ofNullable(subSectionCode).orElse(""));

    switch (sectionSubSection) {
      case "PA":  // airports
        return arincFixDatabase.airport(identifier, icaoRegion).map(fixAssembler::assemble);
      // Enroute NDB Navaids
      case "DB":
        return arincFixDatabase.enrouteNdbNavaid(identifier, icaoRegion).map(fixAssembler::assemble);
      // Terminal NDB Navaids
      case "PN":
        return arincFixDatabase.terminalNdbNavaid(identifier, icaoRegion).map(fixAssembler::assemble);
      // VHF Navaids
      case "D":
        return arincFixDatabase.vhfNavaid(identifier, icaoRegion).map(fixAssembler::assemble);
      // Enroute waypoints
      case "EA":
        return arincFixDatabase.enrouteWaypoint(identifier, icaoRegion).map(fixAssembler::assemble);
      // Terminal waypoints
      case "PC":
        return arincTerminalAreaDatabase.waypointAt(airport, icaoRegion, identifier).map(fixAssembler::assemble);
      // runways - generally terminal fix of the final fix of the final approach portion of an approach procedure (or centerFix of an RF)
      case "PG":
        return airport == null ? Optional.empty() : arincTerminalAreaDatabase.runwayAt(airport, identifier).map(fixAssembler::assemble);
      // localizerGlideSlopes - generally used as a recommended navaid on some approaches
      case "PI":
        return airport == null ? Optional.empty() : arincTerminalAreaDatabase.localizerGlideSlopeAt(airport, identifier).map(fixAssembler::assemble);
      case "PT":
        return airport == null ? Optional.empty() : arincTerminalAreaDatabase.gnssLandingSystemAt(airport, identifier).map(fixAssembler::assemble);
      // anything else is not explicitly supported as a reference object in a leg
      default:
        throw new IllegalStateException("Unknown referenced section/subsection for lookup of location: ".concat(sectionSubSection));
    }
  }
}