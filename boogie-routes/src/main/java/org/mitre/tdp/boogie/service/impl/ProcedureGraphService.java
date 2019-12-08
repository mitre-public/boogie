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

public final class ProcedureGraphService implements LookupService<ProcedureGraph> {

  private final Multimap<String, ProcedureGraph> byIdentifier;

  private ProcedureGraphService(Multimap<String, ProcedureGraph> map) {
    this.byIdentifier = map;
  }

  @Override
  public Collection<ProcedureGraph> allMatchingIdentifiers(String identifier) {
    return byIdentifier.get(identifier);
  }

  public static ProcedureGraphService with(Collection<? extends Transition> transitions) {
    Map<String, List<Transition>> procedures = transitions.stream()
        .collect(Collectors.groupingBy(t -> t.procedure() + t.airport() + t.source().name()));

    Multimap<String, ProcedureGraph> byId = HashMultimap.create();
    procedures.values().forEach(ts -> {
      ProcedureGraph graph = ProcedureGraph.from(ts);
      byId.put(graph.identifier(), graph);
    });

    return new ProcedureGraphService(byId);
  }
}
