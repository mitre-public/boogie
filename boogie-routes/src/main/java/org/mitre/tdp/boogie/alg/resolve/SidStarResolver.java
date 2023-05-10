package org.mitre.tdp.boogie.alg.resolve;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.TransitionMaskedProcedure;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.validate.RecordElectorFactory;

/**
 * Class for resolving candidate {@link SidElement}/{@link StarElement}s from a {@link SectionSplit}.
 */
final class SidStarResolver implements SectionResolver {

  private final LookupService<Procedure> lookupService;

  SidStarResolver(LookupService<Procedure> lookupService) {
    this.lookupService = checkNotNull(lookupService).thenFilterWith(p -> !ProcedureType.APPROACH.equals(p.procedureType()));
  }

  @Override
  public ResolvedSection resolve(@Nullable SectionSplit previous, SectionSplit sectionSplit, @Nullable SectionSplit next) {
    return new ResolvedSection(sectionSplit, convertToResolvedElements(proceduresFor(previous, sectionSplit, next)));
  }

  /**
   * Returns the collection of procedures matching the current section split + applying some filtering to preferentially select
   * procedures with the same source airports as the previous or next section split.
   * <br>
   * See {@link #newAirportFilter(SectionSplit, SectionSplit)}.
   */
  Collection<Procedure> proceduresFor(@Nullable SectionSplit previous, SectionSplit sectionSplit, @Nullable SectionSplit next) {
    Collection<Procedure> procedures = lookupService.apply(sectionSplit.value());

    // Down-select to procedures matching the filed arr/dep airport (assuming we have them)
    Predicate<Procedure> airportFilter = newAirportFilter(previous, next);
    Collection<Procedure> proceduresAtAirport = procedures.stream().filter(airportFilter).collect(Collectors.toList());

    // We want to down-select where we can, but something is better than nothing, so allow the no-match
    // case to return anything that matched - the algo is generic enough to handle it smartly
    return proceduresAtAirport.isEmpty() ? procedures : proceduresAtAirport;
  }

  List<ResolvedElement> convertToResolvedElements(Collection<Procedure> procedures) {
    return procedures.stream()
        .map(procedure -> {
          Procedure withoutRunwayTransitions = new TransitionMaskedProcedure(procedure, COMMON_OR_ENROUTE);

          if (ProcedureType.SID.equals(procedure.procedureType())) {
            return new SidElement(withoutRunwayTransitions);
          } else if (ProcedureType.STAR.equals(procedure.procedureType())) {
            return new StarElement(withoutRunwayTransitions);
          } else {
            throw new IllegalArgumentException("Unsupported procedure type for conversion: ".concat(procedure.procedureType().name()));
          }
        })
        .collect(Collectors.toList());
  }

  /**
   * Returns a new {@link Procedure} filter to down-select to procedures who's {@link Procedure#airportIdentifier()} matches the
   * ones filed as arrival/departure in the route string.
   * <br>
   * This is necessary as most navigation data sources publish multiple <i>similar</i> versions of the same procedure if it serves
   * multiple airports within an area. These cases are explicitly not filtered in the {@link RecordElectorFactory} methods.
   * <br>
   * e.g. HOBBT2 serves KATL, SATELLITE1, SATELLITE2... we get a copy of HOBBT2 in the raw data for each of those airports, this
   * is meant to prefer the implementation who's {@link Procedure#airportIdentifier()} matches the filed arrival/departure airport.
   * <br>
   * Note this requires some upstream standardization of route strings (as many real route strings interchangeable use the ICAO/FAA
   * code for the arrival/departure airport).
   */
  Predicate<Procedure> newAirportFilter(@Nullable SectionSplit previous, @Nullable SectionSplit next) {
    return procedure -> {
      if (ProcedureType.SID.equals(procedure.procedureType())) {
        return Optional.ofNullable(previous)
            .filter(p -> p.value().equalsIgnoreCase(procedure.airportIdentifier())).isPresent();
      }
      if (ProcedureType.STAR.equals(procedure.procedureType())) {
        return Optional.ofNullable(next)
            .filter(n -> n.value().equalsIgnoreCase(procedure.airportIdentifier())).isPresent();
      }
      return false;
    };
  }

  private static final Predicate<Transition> COMMON_OR_ENROUTE = transition ->
      TransitionType.COMMON.equals(transition.transitionType()) || TransitionType.ENROUTE.equals(transition.transitionType());
}
