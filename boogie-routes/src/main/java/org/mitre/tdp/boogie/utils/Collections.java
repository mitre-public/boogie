package org.mitre.tdp.boogie.utils;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Collections {

  public static <U> Collection<U> filter(Collection<U> iterable, Predicate<U> filter) {
    return iterable.stream().filter(filter).collect(Collectors.toList());
  }

  public static <U, V> boolean allMatch(Collection<U> iterable, Function<U, V> ext) {
    return iterable.stream().map(ext).distinct().count() == 1;
  }

  public static <U, V> boolean allMatch(Collection<U> iterable, Function<U, V> ext, V value) {
    return iterable.stream().allMatch(u -> ext.apply(u).equals(value));
  }
}
