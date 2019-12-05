package org.mitre.tdp.boogie.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.models.ProcedureGraph;
import org.mitre.tdp.boogie.service.LookupService;

public final class ProcedureGraphService<F extends Fix, L extends Leg<F>, T extends Transition<F, L>> implements LookupService<ProcedureGraph<F, L, T>> {

  private final Multimap<String, ProcedureGraph<F, L, T>> byIdentifier;

  private ProcedureGraphService(Multimap<String, ProcedureGraph<F, L, T>> map) {
    this.byIdentifier = map;
  }

  @Override
  public Collection<ProcedureGraph<F, L, T>> allMatchingIdentifiers(String identifier) {
    return byIdentifier.get(identifier);
  }

  public static <F extends Fix, L extends Leg<F>, T extends Transition<F, L>> ProcedureGraphService<F, L, T> with(Collection<T> transitions) {
    Map<String, List<T>> procedures = transitions.stream()
        .collect(Collectors.groupingBy(t -> t.procedure() + t.airport() + t.source().name()));

    Multimap<String, ProcedureGraph<F, L, T>> byId = new HashMultimap<>();
    procedures.values().forEach(ts -> {
      ProcedureGraph<F, L, T> graph = ProcedureGraph.from(ts);
      byId.put(graph.identifier(), graph);
    });

    return new ProcedureGraphService<>(byId);
  }
}
