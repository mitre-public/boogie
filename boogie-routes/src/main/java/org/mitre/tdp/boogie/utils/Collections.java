package org.mitre.tdp.boogie.utils;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.mitre.caasd.commons.Pair;

import com.google.common.collect.Iterables;

public class Collections {

  /**
   * Sort method for use on general collections and returning a List which can be inserted
   * into afterward unlike {@link List#sort(Comparator)}.
   */
  public static <U> List<U> sort(Collection<U> coll, Comparator<? super U> comparator) {
    return coll.stream().sorted(comparator).collect(Collectors.toList());
  }

  public static <U, V> List<V> transform(List<U> col, Function<U, V> transform) {
    return col.stream().map(transform).collect(Collectors.toList());
  }

  public static <U, V> Collection<V> transform(Collection<U> col, Function<U, V> transform) {
    return col.stream().map(transform).collect(Collectors.toList());
  }

  public static <U> List<U> filter(Collection<U> iterable, Predicate<U> filter) {
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

  /**
   * Zips together a pair of iterables on an extracted key.
   * <p>
   * Note if a particular key shows up multiple times in one of the iterables the first instance of that key
   * is kept and all others are ignored.
   */
  public static <K, V> Map<K, Pair<V, V>> zipByKey(Iterable<V> col1, Iterable<V> col2, Function<V, K> keyFn) {
    Map<K, Pair<V, V>> zipMap = new HashMap<>();

    Iterable<Pair<V, V>> pairs1 = Iterables.transform(col1, v -> Pair.of(v, null));
    Iterable<Pair<V, V>> pairs2 = Iterables.transform(col2, v -> Pair.of(null, v));

    Iterables.concat(pairs1, pairs2).spliterator().forEachRemaining(pair -> {
      V p1 = pair.first();
      V p2 = pair.second();

      V value = p1 == null ? p2 : p1;
      K key = keyFn.apply(value);
      if (zipMap.containsKey(key)) {
        Pair<V, V> cv = zipMap.get(key);

        // check merge-able pair
        if ((cv.first() != null && pair.first() != null) || (cv.second() != null && pair.second() != null)) {
          // if we see a duplicate of a given key from one iterable we skip it - see docs.
        } else {
          if (p1 == null) {
            zipMap.put(key, Pair.of(cv.first(), p2));
          } else {
            zipMap.put(key, Pair.of(p1, cv.second()));
          }
        }
      } else {
        zipMap.put(key, pair);
      }
    });

    return zipMap;
  }
}
