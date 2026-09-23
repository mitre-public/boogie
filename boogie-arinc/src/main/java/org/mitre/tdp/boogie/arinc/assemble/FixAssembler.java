package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Objects.requireNonNull;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.arinc.model.ArincAirport;
import org.mitre.tdp.boogie.arinc.model.ArincGnssLandingSystem;
import org.mitre.tdp.boogie.arinc.model.ArincHelipad;
import org.mitre.tdp.boogie.arinc.model.ArincHeliport;
import org.mitre.tdp.boogie.arinc.model.ArincLocalizerGlideSlope;
import org.mitre.tdp.boogie.arinc.model.ArincModel;
import org.mitre.tdp.boogie.arinc.model.ArincNdbNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;
import org.mitre.tdp.boogie.arinc.model.ArincVhfNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincWaypoint;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

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
      SectionCode sectionCode = requireNonNull(arincModel.sectionCode());
      String subsection = arincModel.subSectionCode().orElse("");

      return switch (sectionCode) {
        case D -> switch (subsection) {
          case "" -> strategy.convertVhfNavaid((ArincVhfNavaid) arincModel);
          case "B" -> strategy.convertNdbNavaid((ArincNdbNavaid) arincModel);
          default -> throw unknownSection(sectionCode, subsection);
        };
        case E -> switch (subsection) {
          case "A" -> strategy.convertWaypoint((ArincWaypoint) arincModel);
          default -> throw unknownSection(sectionCode, subsection);
        };
        case P -> switch (subsection) {
          case "A" -> strategy.convertAirport((ArincAirport) arincModel);
          case "N" -> strategy.convertNdbNavaid((ArincNdbNavaid) arincModel);
          case "C" -> strategy.convertWaypoint((ArincWaypoint) arincModel);
          case "G" -> strategy.convertRunway((ArincRunway) arincModel);
          case "I" -> strategy.convertLocalizerGlideSlope((ArincLocalizerGlideSlope) arincModel);
          case "T" -> strategy.convertGnssLandingSystem((ArincGnssLandingSystem) arincModel);
          case "H" -> strategy.convertHelipad((ArincHelipad) arincModel);
          default -> throw unknownSection(sectionCode, subsection);
        };
        case H -> switch (subsection) {
          case "A" -> strategy.convertHeliport((ArincHeliport) arincModel);
          case "C" -> strategy.convertWaypoint((ArincWaypoint) arincModel);
          case "I" -> strategy.convertLocalizerGlideSlope((ArincLocalizerGlideSlope) arincModel);
          case "T" -> strategy.convertGnssLandingSystem((ArincGnssLandingSystem) arincModel);
          case "H" -> strategy.convertHelipad((ArincHelipad) arincModel);
          default -> throw unknownSection(sectionCode, subsection);
        };
        default -> throw unknownSection(sectionCode, subsection);
      };
    }

    private static IllegalStateException unknownSection(SectionCode sectionCode, String subsection) {
      return new IllegalStateException("Unknown referenced section/subsection for lookup of location: " + sectionCode.name() + subsection);
    }
  }
}
