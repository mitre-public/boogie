package org.mitre.tdp.boogie.conformance.alg.assign;

import java.util.Collection;
import java.util.function.Function;

import org.mitre.tdp.boogie.model.ProcedureGraph;

public final class ProcedureRoutesExtractor implements Function<ProcedureGraph, Collection<Route<ProcedureGraph>>> {
  public static final ProcedureRoutesExtractor INSTANCE = new ProcedureRoutesExtractor();
  private ProcedureRoutesExtractor() {}
  @Override
  public Collection<Route<ProcedureGraph>> apply(ProcedureGraph procedureGraph) {
    return procedureGraph.allPaths().stream()
        .map(l -> Route.newRoute(l, procedureGraph))
        .toList();
  }
}
