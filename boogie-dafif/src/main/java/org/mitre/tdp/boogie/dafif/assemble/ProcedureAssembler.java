package org.mitre.tdp.boogie.dafif.assemble;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.caasd.commons.util.Partitioners;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.dafif.database.ConvertingLegDereferencer;
import org.mitre.tdp.boogie.dafif.database.DafifFixDatabase;
import org.mitre.tdp.boogie.dafif.database.DafifTerminalAreaDatabase;
import org.mitre.tdp.boogie.dafif.model.DafifTerminalParent;
import org.mitre.tdp.boogie.dafif.model.DafifTerminalSegment;

/**
 * This class is used to assemble DAFIF records into boogie standard objects.
 * This class can be used with the standard implementation of {@link ProcedureAssemblyStrategy} and {@link FixAssemblyStrategy} or with user supplied ones.
 * @param <P> the procedure class being used.
 */
public interface ProcedureAssembler<P> {

  static ProcedureAssembler<Procedure> standard(DafifTerminalAreaDatabase tad, DafifFixDatabase fixDatabase, ProcedureAssemblyStrategy<Procedure, Transition, Leg, Fix> procedureAssemblyStrategy, FixAssemblyStrategy<Fix> fixStrategy) {
    return withStrategy(tad, fixDatabase, procedureAssemblyStrategy, fixStrategy);
  }

  static <P, T, L, F> ProcedureAssembler<P> withStrategy(DafifTerminalAreaDatabase tad, DafifFixDatabase fixDatabase, ProcedureAssemblyStrategy<P, T, L, F> procedureAssemblyStrategy, FixAssemblyStrategy<F> fixStrategy) {
    return new Standard<>(tad, fixDatabase, procedureAssemblyStrategy, fixStrategy);
  }

  Stream<P> assemble(Collection<DafifTerminalParent> parents);

  class Standard<P, T, L, F> implements ProcedureAssembler<P> {
    private final DafifTerminalAreaDatabase tad;
    private final ProcedureAssemblyStrategy<P, T, L, F> procedureAssemblyStrategy;
    private final ConvertingLegDereferencer<F> dereferencer;

    public Standard(DafifTerminalAreaDatabase tad, DafifFixDatabase fixDatabase, ProcedureAssemblyStrategy<P, T, L, F> procedureAssemblyStrategy, FixAssemblyStrategy<F> fixStrategy) {
      this.tad = tad;
      this.procedureAssemblyStrategy = procedureAssemblyStrategy;
      this.dereferencer = ConvertingLegDereferencer.from(fixDatabase, tad, fixStrategy);
    }

    @Override
    public Stream<P> assemble(Collection<DafifTerminalParent> parents) {
      Map<String, List<DafifTerminalParent>> byAirport = parents.stream().collect(Collectors.groupingBy(DafifTerminalParent::airportIdentification));
      return byAirport.values().stream().flatMap(this::airportsProcedures);
    }

    private Stream<P> airportsProcedures(Collection<DafifTerminalParent> parents) {
      Map<String, DafifTerminalParent> parentByProcedureIdent = parents.stream()
          .collect(Collectors.toMap(this::procedureIdentifier, Function.identity()));
      Map<String, List<DafifTerminalSegment>> segmentsByProcedureIdent = parents.stream()
          .findFirst()
          .map(p -> tad.terminalSegmentsAt(p.airportIdentification()).stream().collect(Collectors.groupingBy(this::procedureIdentifier)))
          .orElse(Collections.emptyMap());

      return parentByProcedureIdent.keySet().stream()
          .filter(segmentsByProcedureIdent::containsKey)
          .map(k -> oneProcedure(parentByProcedureIdent.get(k), segmentsByProcedureIdent.get(k)));
    }

    private P oneProcedure(DafifTerminalParent parent, List<DafifTerminalSegment> proceduresSegments) {
      DafifTerminalSegment representative = proceduresSegments.stream()
          .filter(s -> TransitionType.COMMON.equals(TransitionTypeEvaluator.INSTANCE.apply(s)))
          .findFirst()
          .orElseGet(() -> proceduresSegments.get(0));

      Map<Optional<String>, List<DafifTerminalSegment>> segmentsByTransition = proceduresSegments.stream()
          .collect(Collectors.groupingBy(DafifTerminalSegment::transitionIdentifier));

      List<T> transitions = switch (representative.terminalProcedureType()) {
        case 1,2 -> sidStarTransitions(segmentsByTransition);
        case 3 -> approachTransitions(segmentsByTransition);
        default -> throw new IllegalArgumentException("Unknown procedure type: " + representative.terminalProcedureType());
      };

      return procedureAssemblyStrategy.convertProcedure(parent, representative, transitions);
    }

    private List<T> sidStarTransitions(Map<Optional<String>, List<DafifTerminalSegment>> segmentsByTransition) {
      return segmentsByTransition.values().stream()
          .map(l -> l.stream().sorted(Comparator.comparing(DafifTerminalSegment::terminalSequenceNumber)).toList())
          .map(this::oneTransition)
          .toList();
    }

    private List<T> approachTransitions(Map<Optional<String>, List<DafifTerminalSegment>> segmentsByTransition) {
      return segmentsByTransition.values().stream()
          .map(l -> l.stream().sorted(Comparator.comparing(DafifTerminalSegment::terminalSequenceNumber)).toList())
          .map(this::repartition)
          .flatMap(Collection::stream)
          .map(this::oneTransition)
          .toList();
    }

    /**
     * We need to split out the missed approach which is coded as part of the final.
     * @param segments the segments that belong to this transition by name
     * @return a list of lists that will split out the missed approach.
     */
    private List<List<DafifTerminalSegment>> repartition(List<DafifTerminalSegment> segments) {
      return Partitioners.splitOnPairwiseChange(segments, (ls, next) -> !next.terminalWaypointDescriptionCode3().equals(Optional.of("M")));
    }

    private T oneTransition(List<DafifTerminalSegment> segs) {
      List<L> legs = segs.stream()
          .map(this::oneLeg)
          .toList();

      return procedureAssemblyStrategy.convertTransition(segs.get(0), legs);
    }

    private L oneLeg(DafifTerminalSegment seg) {
      F associatedFix = seg.terminalWaypointDescriptionCode1()
          .map(w -> dereferencer.fix(w, seg.airportIdentification(), seg.termSegWaypointIdentifier().orElse(""), seg.waypointCountryCode().orElse("")))
          .orElse(null);
      F nav1 = seg.navaid1Identifier()
          .map(w -> dereferencer.nav1(seg.airportIdentification(), w, seg.navaid1Type().orElse(""), seg.navaid1CountryCode().orElse(""), seg.navaid1KeyCode().orElse(9999)))
          .orElse(null);
      F arcCenter = seg.arcWaypointIdentifier()
          .map(w -> dereferencer.arcCenter(w, seg.arcWaypointCountryCode().orElse("")))
          .orElse(null);
      return procedureAssemblyStrategy.convertLeg(seg, associatedFix, nav1, arcCenter);
    }

    private String procedureIdentifier(DafifTerminalParent parent) {
      return parent.terminalProcedureType() + parent.terminalIdentifier();
    }

    private String procedureIdentifier(DafifTerminalSegment seg) {
      return seg.terminalProcedureType() + seg.terminalIdentifier();
    }
  }
}
