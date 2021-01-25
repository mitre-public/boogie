package org.mitre.tdp.boogie.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import org.mitre.caasd.commons.collect.HashedLinkedSequence;
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
   * Iterates through the input list in a pairwise fashion, providing to the consumer the subsequent elements of the list.
   */
  public static <T> void pairwise(List<T> list, BiConsumer<T, T> consumer) {
    Preconditions.checkArgument(list.size() >= 2);
    IntStream.range(1, list.size()).forEach(i -> consumer.accept(list.get(i - 1), list.get(i)));
  }

  /**
   * Iterates through the provided {@link HashedLinkedSequence} in a pairwise fashion.
   */
  public static <T> void pairwise(HashedLinkedSequence<T> sequence, BiConsumer<T, T> consumer) {
    Preconditions.checkArgument(sequence.size() >= 1);
    sequence.stream().skip(1).forEach(entry -> consumer.accept(sequence.getElementBefore(entry), entry));
  }

  /**
   * Method for iteration through triples of records in a sequence.
   */
  public static <T> void triples(List<T> list, TriConsumer<T, T, T> consumer) {
    Preconditions.checkArgument(list.size() > 2);
    IntStream.range(1, list.size() - 1).forEach(i -> consumer.accept(list.get(i - 1), list.get(i), list.get(i + 1)));
  }

  /**
   * Method for iterating through triples of elements within a list of size > 1.
   */
  public static <T> void triples2(List<T> list, TriConsumer<Optional<T>, T, Optional<T>> consumer) {
    Preconditions.checkArgument(!list.isEmpty());
    IntStream.range(0, list.size()).forEach(i -> consumer.accept(
        i - 1 < 0 ? Optional.empty() : Optional.of(list.get(i - 1)),
        list.get(i),
        i + 1 >= list.size() ? Optional.empty() : Optional.of(list.get(i + 1))
    ));
  }

  /**
   * Takes a list of elements, a predicate, and a TriConsumer and iterates through the list providing to the consumer the first
   * and last elements which matched the predicate and a boolean indicating whether there were elements in between them.
   */
  public static <T> void fastslow(List<T> list, Predicate<T> match, TriConsumer<T, T, Boolean> consumer) {
    fastslow2(list, match, (l, h, ls) -> consumer.accept(l, h, !ls.isEmpty()));
  }

  /**
   * Returns whether the given predicate has enough matches in the list (>=2) to perform the {@link Iterators#fastslow2(List, Predicate, TriConsumer)}
   * operation.
   */
  public static <T> boolean checkMatchCount(List<T> list, Predicate<T> match) {
    return Collections.filter(list, match).size() >= 2;
  }

  /**
   * Fast/Slow iterator for list of elements. When both the fast and slow iterators have a match the the consumer is called on
   * the pair along with the list of skipped elements between the fast and slow iterators.
   *
   * Note - This method requires that there be at least two elements which match the supplied predicate to avoid silently never
   * calling the consumer.
   */
  public static <T> void fastslow2(List<T> list, Predicate<T> match, TriConsumer<T, T, List<T>> consumer) {
    Preconditions.checkArgument(checkMatchCount(list, match));
    openClose2(list, match, (lo, hi) -> match.test(hi), consumer);
  }

  /**
   * See {@link #openClose2(List, Predicate, BiPredicate, TriConsumer)}.
   */
  public static <T> void openClose(List<T> list, Predicate<T> open, Predicate<T> close, TriConsumer<T, T, Boolean> consumer) {
    openClose2(list, open, (lo, hi) -> close.test(hi), (l, h, ls) -> consumer.accept(l, h, !ls.isEmpty()));
  }

  /**
   * See {@link #openClose2(List, Predicate, BiPredicate, TriConsumer)}.
   */
  public static <T> void openClose(List<T> list, Predicate<T> open, BiPredicate<T, T> close, TriConsumer<T, T, Boolean> consumer) {
    openClose2(list, open, close, (l, h, ls) -> consumer.accept(l, h, !ls.isEmpty()));
  }

  public static <T> void openClose2(List<T> list, Predicate<T> open, Predicate<T> close, TriConsumer<T, T, List<T>> consumer) {
    openClose2(list, open, (lo, hi) -> close.test(hi), consumer);
  }

  /**
   * Open/Close iterator for a sequence of elements, similar to a fast/slow iterator except the start lo condition is different
   * than the hi condition. The close bipredicate provides arguments as (lo, hi) in case the lo element is necessary as well for
   * determining a close condition.
   *
   * Note - There are no checks in this method to ensure the close ever returns true - this means the iteration may run without
   * ever directly calling the consumer.
   */
  public static <T> void openClose2(List<T> list, Predicate<T> open, BiPredicate<T, T> close, TriConsumer<T, T, List<T>> consumer) {
    int n = list.size();
    int l = 0;
    int h = 1;
    List<T> skipped = new ArrayList<>();
    while (h < n) {
      T lo = list.get(l);
      T hi = list.get(h);
      if (open.test(lo)) {
        if (close.test(lo, hi)) {
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
