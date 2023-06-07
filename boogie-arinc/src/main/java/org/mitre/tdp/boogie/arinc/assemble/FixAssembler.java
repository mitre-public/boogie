package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.arinc.model.ArincAirport;
import org.mitre.tdp.boogie.arinc.model.ArincGnssLandingSystem;
import org.mitre.tdp.boogie.arinc.model.ArincLocalizerGlideSlope;
import org.mitre.tdp.boogie.arinc.model.ArincModel;
import org.mitre.tdp.boogie.arinc.model.ArincNdbNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;
import org.mitre.tdp.boogie.arinc.model.ArincVhfNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincWaypoint;

/**
 * Assembler class for converting multiple flavors of fix-like 424 record types into client-defined fix data models.
 *
 * <p>This class can be used with the {@link FixAssemblyStrategy#standard()} to generate lightweight Boogie-defined {@link Fix}
 * implementations that can be used with other Boogie algorithms.
 */
public final class FixAssembler<F> implements Function<ArincModel, F> {

  private final FixAssemblyStrategy<F> strategy;

  private FixAssembler(FixAssemblyStrategy<F> strategy) {
    this.strategy = requireNonNull(strategy);
  }

  /**
   * Create a new fix assembler with the given assembly strategy handling conversion of all the common 424 record types that are
   * referenced in airway/procedure leg definitions.
   *
   * @param strategy strategy class for converting the various 424 record types airway/procedure legs can reference as fixes into
   *                 client-defined fix models
   */
  public static <F> FixAssembler<F> create(FixAssemblyStrategy<F> strategy) {
    return new FixAssembler<>(strategy);
  }

  @Override
  public F apply(ArincModel arincModel) {
    String sectionSubSection = arincModel.sectionCode().name().concat(arincModel.subSectionCode().orElse(""));

    // airports
    switch (sectionSubSection) {
      case "PA":
        return strategy.convertAirport((ArincAirport) arincModel);
      // Enroute NDB Navaids
      case "DB":
        return strategy.convertNdbNavaid((ArincNdbNavaid) arincModel);
      // Terminal NDB Navaids
      case "PN":
        return strategy.convertNdbNavaid((ArincNdbNavaid) arincModel);
      // VHF Navaids
      case "D":
        return strategy.convertVhfNavaid((ArincVhfNavaid) arincModel);
      // Enroute waypoints
      case "EA":
        return strategy.convertWaypoint((ArincWaypoint) arincModel);
      // Terminal waypoints
      case "PC":
        return strategy.convertWaypoint((ArincWaypoint) arincModel);
      // runways - generally terminal fix of the final fix of the final approach portion of an approach procedure (or centerFix of an RF)
      case "PG":
        return strategy.convertRunway((ArincRunway) arincModel);
      // localizerGlideSlopes - generally used as a recommended navaid on some approaches
      case "PI":
        return strategy.convertLocalizerGlideSlope((ArincLocalizerGlideSlope) arincModel);
      //gnss landing systems - usually used on gls approach or as rec navs rarely
      case "PT":
        return strategy.convertGnssLandingSystem((ArincGnssLandingSystem) arincModel);
      // anything else is not explicitly supported as a reference object in a leg
      default:
        throw new IllegalStateException("Unknown referenced section/subsection for lookup of location: ".concat(sectionSubSection));
    }
  }
}
