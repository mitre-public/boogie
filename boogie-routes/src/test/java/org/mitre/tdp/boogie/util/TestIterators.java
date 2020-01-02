package org.mitre.tdp.boogie.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.utils.Iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


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
}
