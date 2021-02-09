package org.mitre.tdp.boogie.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.utils.Iterators;


public class TestIterators {

  private boolean odd(Integer i) {
    return i % 2 != 0;
  }

  @Test
  public void testSkipEvens() {
    List<Integer> ints = IntStream.range(0, 20).boxed().collect(Collectors.toList());

    List<Integer> odds = new ArrayList<>();

    Iterators.fastslow2(ints, this::odd, (l, h, b) -> {
      b.forEach(e -> assertFalse(odd(e)));
      odds.add(l);
    });

    assertEquals(odds.size(), 9);
    assertEquals(odds.get(0).intValue(), 1);
    assertEquals(odds.get(8).intValue(), 17);
  }

  @Test
  void testTriplesWithNulls() {
    ArrayList<Triple<String, String, String>> result = new ArrayList<>();

    Iterators.triplesWithNulls(Arrays.asList("foo"), (x, y, z) -> result.add(ImmutableTriple.of(x, y, z)));

    List<ImmutableTriple<String, String, String>> expected = Arrays.asList(ImmutableTriple.of(null, null, "foo"), ImmutableTriple.of(null, "foo", null), ImmutableTriple.of("foo", null, null));

    assertEquals(expected, result);
  }
}
