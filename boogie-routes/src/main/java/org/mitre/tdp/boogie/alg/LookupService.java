package org.mitre.tdp.boogie.alg;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Returns the set of elements of type 'I' matching a provided string identifier (typically an identifier).
 */
@FunctionalInterface
public interface LookupService<I> extends Function<String, Collection<I>> {

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

  /**
   * Returns a new {@link LookupService} which returns the empty collection on any query.
   */
  static <I> LookupService<I> noop() {
    return s -> emptyList();
  }
}
