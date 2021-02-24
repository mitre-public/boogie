package org.mitre.tdp.boogie.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Pair;

public class TestCollections {

  @Test
  public void testSortSorts() {
    List<Integer> ints = Arrays.asList(1, 5, 3, 9, 7);
    List<Integer> sorted = Collections.sort(ints, Integer::compare);

    assertEquals(1, sorted.get(0));
    assertEquals(3, sorted.get(1));
    assertEquals(5, sorted.get(2));
    assertEquals(7, sorted.get(3));
    assertEquals(9, sorted.get(4));
  }

  @Test
  public void testSortSavesSameElements() {
    List<Integer> ints = Arrays.asList(1, 5, 5, 3, 7);
    List<Integer> sorted = Collections.sort(ints, Integer::compare);

    assertEquals(1, sorted.get(0));
    assertEquals(3, sorted.get(1));
    assertEquals(5, sorted.get(2));
    assertEquals(5, sorted.get(3));
    assertEquals(7, sorted.get(4));
  }

  @Test
  public void testZipByKeyAllOverlap() {
    List<Integer> odds = Arrays.asList(1, 3, 5, 7, 9);
    List<Integer> evens = Arrays.asList(0, 2, 4, 6, 8);

    Map<Integer, Pair<Integer, Integer>> byLowestEven = Collections.zipByKey(odds, evens, i -> i - (i % 2));

    assertEquals(5, byLowestEven.keySet().size());
    assertTrue(byLowestEven.keySet().containsAll(evens));
    assertTrue(byLowestEven.values().stream().allMatch(p -> p.first() != null && p.second() != null));
  }

  @Test
  public void testZipByKeySomeMissing() {
    List<Integer> odds = Arrays.asList(1, 3, 7, 9);
    List<Integer> evens = Arrays.asList(0, 2, 4, 6, 8);

    Map<Integer, Pair<Integer, Integer>> byLowestEven = Collections.zipByKey(odds, evens, i -> i - (i % 2));

    assertEquals(5, byLowestEven.keySet().size());
    assertTrue(byLowestEven.keySet().containsAll(evens));
    assertNull(byLowestEven.get(4).first());
  }
}
