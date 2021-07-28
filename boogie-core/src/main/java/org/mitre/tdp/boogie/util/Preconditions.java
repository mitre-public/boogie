package org.mitre.tdp.boogie.util;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.function.Function;

public final class Preconditions {

  private Preconditions() {
    throw new IllegalStateException("Unable to instantiate static utility class.");
  }

  public static <U, V> boolean allMatch(Collection<U> iterable, Function<U, V> ext) {
    requireNonNull(ext);
    return iterable.stream().map(ext).distinct().count() == 1;
  }

  public static <U, V> boolean allMatch(Collection<U> iterable, Function<U, V> ext, V value) {
    requireNonNull(ext);
    return iterable.stream().allMatch(u -> ext.apply(u).equals(value));
  }
}
