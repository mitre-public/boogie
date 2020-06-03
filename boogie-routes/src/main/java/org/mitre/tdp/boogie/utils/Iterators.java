package org.mitre.tdp.boogie.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import org.mitre.tdp.boogie.fn.TriConsumer;

import com.google.common.base.Preconditions;

/**
 * Collection of methods for executing common iteration patterns.
 */
public class Iterators {

  private Iterators() {
    throw new IllegalStateException("Utility Class");
  }

  /**
   * Iterates through the input list in a pairwise fashion, providing to
   * the consumer the subsequent elements of the list.
   */
  public static <T> void pairwise(List<T> list, BiConsumer<T, T> consumer) {
    Preconditions.checkArgument(list.size() >= 2);
    IntStream.range(1, list.size())
        .forEach(i -> consumer.accept(list.get(i - 1), list.get(i)));
  }

  /**
   * Takes a list of elements, a predicate, and a TriConsumer and iterates
   * through the list providing to the consumer the first and last elements
   * which matched the predicate and a boolean indicating whether there were
   * elements in between them.
   */
  public static <T> void fastslow(List<T> list, Predicate<T> match, TriConsumer<T, T, Boolean> consumer) {
    fastslow2(list, match, (l, h, ls) -> consumer.accept(l, h, !ls.isEmpty()));
  }

  /**
   * Returns whether the given predicate has enough matches in the list (>=2) to
   * perform the {@link Iterators#fastslow2(List, Predicate, TriConsumer)} operation.
   */
  public static <T> boolean checkMatchCount(List<T> list, Predicate<T> match) {
    return Collections.filter(list, match).size() >= 2;
  }

  /**
   * Fast/Slow iterator for list of elements. When both the fast and slow
   * iterators have a match the the consumer is called on the pair along
   * with the list of skipped elements between the fast and slow iterators.
   *
   * Note - This method requires that there be at least two elements which
   * match the supplied predicate to avoid silently never calling the consumer.
   */
  public static <T> void fastslow2(List<T> list, Predicate<T> match, TriConsumer<T, T, List<T>> consumer) {
    Preconditions.checkArgument(checkMatchCount(list, match));
    int n = list.size();
    int l = 0;
    int h = 1;
    List<T> skipped = new ArrayList<>();
    while (h < n) {
      T lo = list.get(l);
      T hi = list.get(h);
      if (match.test(lo)) {
        if (match.test(hi)) {
          consumer.accept(lo, hi, skipped);
          skipped.clear();
          l = h;
          h++;
        } else {
          skipped.add(hi);
          h++;
        }
      } else {
        l++;
        h++;
      }
    }
  }
}
