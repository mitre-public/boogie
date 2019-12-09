package org.mitre.tdp.boogie.utils;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Collections {

  public static <U, V> List<V> transform(List<U> col, Function<U, V> transform) {
    return col.stream().map(transform).collect(Collectors.toList());
  }

  public static <U, V> Collection<V> transform(Collection<U> col, Function<U, V> transform) {
    return col.stream().map(transform).collect(Collectors.toList());
  }

  public static <U> Collection<U> filter(Collection<U> iterable, Predicate<U> filter) {
    return iterable.stream().filter(filter).collect(Collectors.toList());
  }

  public static <U, V> boolean allMatch(Collection<U> iterable, Function<U, V> ext) {
    return iterable.stream().map(ext).distinct().count() == 1;
  }

  public static <U, V> boolean allMatch(Collection<U> iterable, Function<U, V> ext, V value) {
    return iterable.stream().allMatch(u -> ext.apply(u).equals(value));
  }

  public static <U, V> boolean noneMatch(Collection<U> iterable, Function<U, V> ext, V value) {
    return iterable.stream().noneMatch(u -> ext.apply(u).equals(value));
  }
}
