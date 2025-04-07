package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Objects.requireNonNull;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.arinc.model.*;

/**
 * Assembler class for converting multiple flavors of fix-like 424 record types into client-defined fix data models.
 *
 * <p>This class can be used with the {@link FixAssemblyStrategy#standard()} to generate lightweight Boogie-defined {@link Fix}
 * implementations that can be used with other Boogie algorithms.
 */
public interface FixAssembler<F> {

  static FixAssembler<Fix> standard() {
    return withStrategy(FixAssemblyStrategy.standard());
  }

  /**
   * Create a new fix assembler with the given assembly strategy handling conversion of all the common 424 record types that are
   * referenced in airway/procedure leg definitions.
   *
   * @param strategy strategy class for converting the various 424 record types airway/procedure legs can reference as fixes into
   *                 client-defined fix models
   */
  static <F> FixAssembler<F> withStrategy(FixAssemblyStrategy<F> strategy) {
    return new Standard<>(strategy);
  }

  F assemble(ArincModel model);

  final class Standard<F> implements FixAssembler<F> {

    private final FixAssemblyStrategy<F> strategy;

    private Standard(FixAssemblyStrategy<F> strategy) {
      this.strategy = requireNonNull(strategy);
    }

    @Override
    public F assemble(ArincModel arincModel) {
      String sectionSubSection = arincModel.sectionCode().name().concat(arincModel.subSectionCode().orElse(""));

      // airports
      return switch (sectionSubSection) {
        case "PA" -> strategy.convertAirport((ArincAirport) arincModel);
        // Enroute NDB Navaids
        case "DB" -> strategy.convertNdbNavaid((ArincNdbNavaid) arincModel);
        // Terminal NDB Navaids
        case "PN" -> strategy.convertNdbNavaid((ArincNdbNavaid) arincModel);
        // VHF Navaids
        case "D" -> strategy.convertVhfNavaid((ArincVhfNavaid) arincModel);
        // Enroute waypoints
        case "EA" -> strategy.convertWaypoint((ArincWaypoint) arincModel);
        // Terminal waypoints
        case "PC" -> strategy.convertWaypoint((ArincWaypoint) arincModel);
        // runways - generally terminal fix of the final fix of the final approach portion of an approach procedure (or centerFix of an RF)
        case "PG" -> strategy.convertRunway((ArincRunway) arincModel);
        // localizerGlideSlopes - generally used as a recommended navaid on some approaches
        case "PI" -> strategy.convertLocalizerGlideSlope((ArincLocalizerGlideSlope) arincModel);
        //gnss landing systems - usually used on gls approach or as rec navs rarely
        case "PT" -> strategy.convertGnssLandingSystem((ArincGnssLandingSystem) arincModel);
        //helipads at airports
        case "PH" -> strategy.convertHelipad((ArincHelipad) arincModel);
        // anything else is not explicitly supported as a reference object in a leg
        default ->
            throw new IllegalStateException("Unknown referenced section/subsection for lookup of location: ".concat(sectionSubSection));
      };
    }
  }
}
