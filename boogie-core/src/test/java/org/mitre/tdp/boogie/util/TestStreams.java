package org.mitre.tdp.boogie.util;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.fn.TriFunction;

class TestStreams {

  @Test
  void testPairwise() {
    int actual = Streams.pairwise(ints, biSum).mapToInt(i -> i).sum();
    assertEquals(16, actual);
  }

  @Test
  void testPairwiseWithNullsLessThanTwoElements() {
    int actual = Streams.pairwiseWithNulls(singletonList(5), biSum).mapToInt(i -> i).sum();
    assertEquals(10, actual);
  }

  @Test
  void testPairwiseWithNulls() {
    int actual = Streams.pairwiseWithNulls(ints, biSum).mapToInt(i -> i).sum();
    assertEquals(20, actual);
  }

  @Test
  void testTriplesWithNullsWithLessThanThreeElements() {
    int actual = Streams.triplesWithNulls(Arrays.asList(3, 4), triSum).mapToInt(i -> i).sum();
    assertEquals(14, actual);
  }

  @Test
  void testTriples() {
    int actual = Streams.triples(ints, triSum).mapToInt(i -> i).sum();
    assertEquals(18, actual);
  }

  @Test
  void testCircularTriplesRetainTraversalOrderAcrossTheClosure() {
    assertEquals(List.of(List.of(1, 2, 3), List.of(2, 3, 4), List.of(3, 4, 1), List.of(4, 1, 2)),
        Streams.triplesCircular(List.of(1, 2, 3, 4), List::of).toList());
    assertEquals(List.of(List.of(1, 2, 3), List.of(2, 3, 1), List.of(3, 1, 2)),
        Streams.triplesCircular(List.of(1, 2, 3), List::of).toList());
  }

  @Test
  void testCircularTriplesWithoutThreeElementsNeverInvokeTheMapper() {
    for (List<Integer> input : List.of(List.<Integer>of(), List.of(1), List.of(1, 2))) {
      assertEquals(List.of(), Streams.triplesCircular(input,
          (first, second, third) -> fail("A triple requires three source elements")).toList());
    }
  }

  @Test
  void testTriplesWithNulls() {
    int actual = Streams.triplesWithNulls(ints, triSum).mapToInt(i -> i).sum();
    assertEquals(26, actual);
  }

  private static final List<Integer> ints = IntStream.range(0, 5).boxed().collect(Collectors.toList());

  private static final BiFunction<Integer, Integer, Integer> biSum = (p, c) -> (p == null ? 0 : p) + (c == null ? 0 : c);

  private static final TriFunction<Integer, Integer, Integer, Integer> triSum = (p, c, n) -> (p == null ? 0 : p) + (c == null ? 0 : c) + (n == null ? 0 : n);
}
