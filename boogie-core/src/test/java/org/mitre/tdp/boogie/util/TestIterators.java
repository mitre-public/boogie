package org.mitre.tdp.boogie.util;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.Test;

class TestIterators {

  @Test
  void testSkipEvens() {
    List<Integer> ints = IntStream.range(0, 20).boxed().collect(Collectors.toList());

    List<Integer> odds = new ArrayList<>();

    Iterators.fastslow2(ints, this::odd, (l, h, b) -> {
      b.forEach(e -> assertFalse(odd(e)));
      odds.add(l);
    });

    assertAll(
        () -> assertEquals(9, odds.size()),
        () -> assertEquals(1, odds.get(0).intValue()),
        () -> assertEquals(17, odds.get(8).intValue())
    );
  }

  @Test
  void testTriplesWithNulls() {
    ArrayList<Triple<String, String, String>> result = new ArrayList<>();

    Iterators.triplesWithNulls(singletonList("foo"), (x, y, z) -> result.add(ImmutableTriple.of(x, y, z)));

    List<ImmutableTriple<String, String, String>> expected = Arrays.asList(
        ImmutableTriple.of(null, null, "foo"),
        ImmutableTriple.of(null, "foo", null),
        ImmutableTriple.of("foo", null, null)
    );

    assertEquals(expected, result);
  }

  private boolean odd(Integer i) {
    return i % 2 != 0;
  }
}
