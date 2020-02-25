package org.mitre.tdp.boogie.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.alg.graph.ProcedureGraph;
import org.mitre.tdp.boogie.service.LookupService;
import org.mitre.tdp.boogie.service.ProcedureService;

/**
 * Service providing procedure lookup both by the procedure identifier (e.g. CSTL6, WYNDE8)
 * as well as by the airport they service (e.g. KATL, KORD).
 */
public final class ProcedureGraphService implements ProcedureService {

  private final Multimap<String, ProcedureGraph> byIdentifier;
  private final Multimap<String, ProcedureGraph> byAirport;

  private ProcedureGraphService(Multimap<String, ProcedureGraph> id, Multimap<String, ProcedureGraph> apt) {
    this.byIdentifier = id;
    this.byAirport = apt;
  }

  @Override
  public Collection<ProcedureGraph> allMatchingIdentifiers(String identifier) {
    return byIdentifier.get(identifier);
  }

  @Override
  public Collection<ProcedureGraph> allMatchingAirport(String airport) {
    return byAirport.get(airport);
  }

  public static ProcedureGraphService with(Collection<? extends Transition> transitions) {
    Map<String, List<Transition>> procedures = transitions.stream()
        .collect(Collectors.groupingBy(t -> t.procedure() + t.airport() + t.source().name()));

    Multimap<String, ProcedureGraph> byId = HashMultimap.create();
    Multimap<String, ProcedureGraph> byApt = HashMultimap.create();

    procedures.values().forEach(ts -> {
      ProcedureGraph graph = ProcedureGraph.from(ts);
      byId.put(graph.identifier(), graph);
      byApt.put(graph.airport(), graph);
    });

    return new ProcedureGraphService(byId, byApt);
  }
}
