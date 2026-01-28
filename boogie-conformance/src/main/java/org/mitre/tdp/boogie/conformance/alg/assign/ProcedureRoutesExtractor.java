package org.mitre.tdp.boogie.conformance.alg.assign;

import java.util.Collection;
import java.util.function.Function;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.model.ProcedureFactory;
import org.mitre.tdp.boogie.model.ProcedureGraph;

import com.google.common.annotations.Beta;

/**
 * This class is used to extract all the routes from a {@link Procedure}.
 */
@Beta
public final class ProcedureRoutesExtractor implements Function<Procedure, Collection<Route>> {
  public static final ProcedureRoutesExtractor INSTANCE = new ProcedureRoutesExtractor();
  private ProcedureRoutesExtractor() {}
  @Override
  public Collection<Route> apply(Procedure procedure) {
    return ProcedureFactory.newProcedureGraph(procedure).allPaths().stream()
        .map(l -> Route.newRoute(l, procedure))
        .toList();
  }
}
