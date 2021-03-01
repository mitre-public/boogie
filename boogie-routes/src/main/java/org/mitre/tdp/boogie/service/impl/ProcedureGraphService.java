package org.mitre.tdp.boogie.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.alg.graph.ProcedureGraph;
import org.mitre.tdp.boogie.models.Procedure;
import org.mitre.tdp.boogie.service.ProcedureService;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

/**
 * Service providing procedure lookup both by the procedure identifier (e.g. CSTL6, WYNDE8) as well as by the airport they
 * service (e.g. KATL, KORD).
 */
public final class ProcedureGraphService implements ProcedureService {

  private final Multimap<String, ProcedureGraph> byIdentifier;
  private final Multimap<String, ProcedureGraph> byAirport;

  public ProcedureGraphService(Multimap<String, ProcedureGraph> id, Multimap<String, ProcedureGraph> apt) {
    this.byIdentifier = id;
    this.byAirport = apt;
  }

  @Override
  public Collection<Procedure> allMatchingIdentifier(String identifier) {
    return (Collection<Procedure>) (Collection) allGraphsMatchingIdentifier(identifier);
  }

  public Collection<ProcedureGraph> allGraphsMatchingIdentifier(String identifier) {
    return byIdentifier.get(identifier);
  }

  @Override
  public Collection<Procedure> allMatchingAirport(String airport) {
    return (Collection<Procedure>) (Collection) allGraphsMatchingAirport(airport);
  }

  /**
   * Returns all indexed procedure graph matching the airport as procedure graphs.
   */
  public Collection<ProcedureGraph> allGraphsMatchingAirport(String airport) {
    return byAirport.get(airport);
  }

  /**
   * Generates a new {@link ProcedureGraphService} with the configured collection of procedures.
   */
  public static ProcedureGraphService withProcedures(Collection<? extends ProcedureGraph> procedures) {
    Multimap<String, ProcedureGraph> byId = LinkedHashMultimap.create();
    Multimap<String, ProcedureGraph> byApt = LinkedHashMultimap.create();

    procedures.forEach(procedure -> {
      byId.put(procedure.identifier(), procedure);
      byApt.put(procedure.airport(), procedure);
    });

    return new ProcedureGraphService(byId, byApt);
  }

  /**
   * Generates a new {@link ProcedureGraphService} with the given transitions assembled into their associated procedureGraphs.
   */
  public static ProcedureGraphService withTransitions(Collection<? extends Transition> transitions) {
    Map<String, List<Transition>> procedures = transitions.stream()
        .collect(Collectors.groupingBy(t ->
            t.procedure()
                + t.airport()
                + t.procedureType().name()
                + t.navigationSource().name()));

    return withProcedures(procedures.values().stream().map(ProcedureGraph::from).collect(Collectors.toList()));
  }
}
