package org.mitre.tdp.boogie.alg.approach.impl;

import static org.mitre.tdp.boogie.utils.Collections.filter;
import static org.mitre.tdp.boogie.utils.Collections.transform;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.alg.RouteExpander;
import org.mitre.tdp.boogie.alg.approach.ApproachPredictor;
import org.mitre.tdp.boogie.alg.graph.RouteLegGraph;
import org.mitre.tdp.boogie.alg.graph.ProcedureGraph;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;
import org.mitre.tdp.boogie.alg.resolve.element.ProcedureElement;
import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.models.Procedure;

/**
 * "Predicts" all of the available approach procedures at the airport.
 *
 * <p>When used this will read into the final {@link RouteLegGraph} all of the procedures at the given airport resolving
 * which to fly via the shortest path algorithm.
 */
public class AllApproachPredictor implements ApproachPredictor {

  private RouteExpander expander;

  @Override
  public void configure(RouteExpander expander) {
    this.expander = expander;
  }

  @Override
  public Optional<ResolvedSection> predictCandidateApproaches(ResolvedSection prev, ResolvedSection last) {
    List<ResolvedElement<?>> candidates = last.elements().stream()
        .filter(element -> element.type().equals(ElementType.AIRPORT))
        .map(this::approachProceduresAtAirport)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());

    SectionSplit split = new SectionSplit.Builder().setValue("APCH").setIndex(last.sectionSplit().index()).build();
    return Optional.of(new ResolvedSection(split).setElements(candidates));
  }

  /**
   * Queries the {@link RouteExpander#procedureService()} for the collection of all approach procedures into
   * the resolved airport element.
   */
  private List<ResolvedElement<?>> approachProceduresAtAirport(ResolvedElement<?> element) {
    Airport airport = (Airport) element.reference();
    Collection<Procedure> procedures = expander.procedureService().allMatchingAirport(airport.identifier());

    List<Procedure> approaches = filter(procedures, procedure -> procedure.type().equals(ProcedureType.APPROACH));
    return transform(approaches, ProcedureElement::new);
  }
}
