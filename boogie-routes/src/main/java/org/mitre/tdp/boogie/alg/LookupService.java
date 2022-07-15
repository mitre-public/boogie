package org.mitre.tdp.boogie.alg;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Returns the set of elements of type 'I' matching a provided string identifier (typically a name for a procedure, airport, etc.).
 *
 * <p>This is provided as an interface to allow for calls to this to be backed by a variety of data sources ranging from local KV
 * maps structures to a remote database backend (though if using a remote service you may want to cache lookups locally).
 *
 * <p>Regardless any {@link RouteExpander} implementations shouldn't care <i>how</i> this data is provided beyond the contract of
 * this interface.
 */
@FunctionalInterface
public interface LookupService<I> extends Function<String, Collection<I>> {

  /**
   * Returns a new {@link LookupService} which returns an immutable empty collection on any query.
   *
   * <p>This is useful as a stub for the optional services which can be provided to expander implementations if the client cannot
   * provide a backing query mechanism for those lookups.
   */
  static <I> LookupService<I> noop() {
    return s -> emptyList();
  }

  /**
   * Returns a new {@link LookupService} representing the composition of this service with another.
   * <br>
   * The resultant service will return the unique entries from either - ordering elements of the calling service before those of
   * the provided service.
   */
  default LookupService<I> composeWith(LookupService<I> other) {
    requireNonNull(other);
    return s -> {
      Collection<I> lookedUp = new LinkedHashSet<>();
      lookedUp.addAll(this.apply(s));
      lookedUp.addAll(other.apply(s));
      return lookedUp;
    };
  }

  default LookupService<I> thenFilterWith(Predicate<I> predicate) {
    return s -> this.apply(s).stream().filter(predicate).collect(Collectors.toList());
  }

  default LookupService<I> thenApply(Function<Collection<I>, Collection<I>> function) {
    return s -> function.apply(this.apply(s));
  }
}
