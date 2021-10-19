package org.mitre.tdp.boogie.alg.chooser;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.alg.LookupService;

/**
 * Returns a view of an input collection of procedure filtered in a tiered order to based on the supplied collection of predicates.
 * <br>
 * The first predicate to return a non-empty view of the supplied collection will have its data returned.
 * <br>
 * This paradigm is generally used in conjunction with other functions (e.g. {@link LookupService#thenApply(Function)}) to select
 * on return to a subset of options (e.g. return RNP > RNAV > CONV approaches).
 */
final class PreferredProcedures implements UnaryOperator<Collection<Procedure>> {

  private final List<Predicate<Procedure>> tieredPredicates;

  @SafeVarargs
  public PreferredProcedures(Predicate<Procedure>... tieredPredicates) {
    this(Arrays.asList(tieredPredicates));
  }

  public PreferredProcedures(List<Predicate<Procedure>> tieredPredicates) {
    this.tieredPredicates = tieredPredicates;
  }

  @Override
  public Collection<Procedure> apply(Collection<Procedure> procedures) {
    return tieredPredicates.stream()
        .map(predicate -> procedures.stream().filter(predicate).collect(toList()))
        .filter(col -> !col.isEmpty())
        .findFirst()
        .orElse(Collections.emptyList());
  }
}
