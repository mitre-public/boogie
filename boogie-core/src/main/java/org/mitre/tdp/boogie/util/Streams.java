package org.mitre.tdp.boogie.util;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.mitre.caasd.commons.Pair;
import org.mitre.caasd.commons.collect.HashedLinkedSequence;
import org.mitre.tdp.boogie.fn.TriFunction;

import com.google.common.base.Preconditions;

/**
 * Collection of methods for common streaming iteration patterns.
 */
public final class Streams {

  private Streams() {
    throw new IllegalStateException("Utility Class");
  }

  /**
   * Returns a stream based on the input list via combining subsequent elements via the provided {@link BiFunction}.
   */
  public static <U, V> Stream<V> pairwise(List<U> list, BiFunction<U, U, V> fn) {
    Preconditions.checkArgument(list.size() >= 2);
    return pairwise(list).map(pair -> fn.apply(pair.first(), pair.second()));
  }

  public static <U> Stream<Pair<U, U>> pairwise(List<U> list) {
    return list.size() < 2 ? Stream.empty() : IntStream.range(1, list.size()).mapToObj(i -> Pair.of(list.get(i - 1), list.get(i)));
  }

  /**
   * Iterates through the provided {@link HashedLinkedSequence} in a pairwise fashion.
   */
  public static <T> Stream<Pair<T, T>> pairwise(HashedLinkedSequence<T> sequence) {
    Preconditions.checkArgument(sequence.size() >= 2);
    return sequence.stream().skip(1).map(entry -> Pair.of(sequence.getElementBefore(entry), entry));
  }


  /**
   * Returns a stream based on the input list via combining subsequent elements via the provided {@link BiFunction} using null
   * for elements outside the bounds of the stream.
   * <br>
   * list.size()==0: fn is called once with two null args
   * list.size()==1: fn is called once with list.get(0), null
   * list.size()>1: fn is called pairwise with list.get(i), list.get(i+1)
   */
  public static <U, V> Stream<V> pairwiseWithNulls(List<U> list, BiFunction<U, U, V> fn) {
    IntFunction<U> getOrNull = i -> i >= 0 && i < list.size() ? list.get(i) : null;
    return IntStream.range(0, list.size() + 1).mapToObj(i -> fn.apply(getOrNull.apply(i - 1), getOrNull.apply(i)));
  }

  /**
   * Returns a stream based on the input list via combining triples of elements via the provided {@link TriFunction}.
   */
  public static <U, V> Stream<V> triples(List<U> list, TriFunction<U, U, U, V> fn) {
    Preconditions.checkArgument(list.size() > 2);
    return IntStream.range(1, list.size() - 1).mapToObj(i -> fn.apply(list.get(i - 1), list.get(i), list.get(i + 1)));
  }

  /**
   * Returns a stream based on the input list via combining triples of elements via the provided {@link TriFunction} using null
   * for elements outside the bound of the stream.
   */
  public static <U, V> Stream<V> triplesWithNulls(List<U> list, TriFunction<U, U, U, V> fn) {
    IntFunction<U> getOrNull = i -> i >= 0 && i < list.size() ? list.get(i) : null;
    return IntStream.range(0, list.size()).mapToObj(i -> fn.apply(getOrNull.apply(i - 1), getOrNull.apply(i), getOrNull.apply(i + 1)));
  }
}
