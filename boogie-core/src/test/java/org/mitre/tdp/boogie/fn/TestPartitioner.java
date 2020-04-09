package org.mitre.tdp.boogie.fn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.util.EditDistance;

public class TestPartitioner {
  @Test
  public void testPartitioners_List1() {
    List<Integer> ls = Arrays.asList(1, 1, 6, 6, 2, 4);

    List<List<Integer>> res = ls.stream()
        .collect(Partitioner.listByPredicate(i -> i > 5));

    assertEquals(3, res.size());
    assertEquals(1, res.get(0).get(0).intValue());
    assertEquals(6, res.get(1).get(0).intValue());
    assertEquals(2, res.get(2).get(0).intValue());
    assertEquals(4, res.get(2).get(1).intValue());
  }

  @Test
  public void testPartitioners_List2() {
    List<String> ls = Arrays.asList("a", "a", "b", "c", "b", "b");

    List<List<String>> res = ls.stream()
        .collect(Partitioner.listByPredicate(String::equals));

    assertEquals(4, res.size());
    assertEquals("a", res.get(0).get(0));
    assertEquals("b", res.get(1).get(0));
    assertEquals("c", res.get(2).get(0));
    assertEquals("b", res.get(3).get(0));
  }

  @Test
  public void testPartitioners_List3() {
    List<String> ls = Arrays.asList("a", "aa", "aaa", "b", "bb", "bbb");

    List<List<String>> res = ls.stream()
        .collect(Partitioner.listByPredicate((s1, s2) -> EditDistance.similarity(s1, s2) < 2, true));

    assertEquals(1, res.size());
    assertEquals(6, res.get(0).size());
  }

  @Test
  public void testPartitioners_Navigable1() {
    TreeSet<Integer> ts = new TreeSet<>(Arrays.asList(2, 4, 5, 7, 8));

    TreeSet<TreeSet<Integer>> res = ts.stream()
        .collect(Partitioner.navigableByPredicate(i -> i % 2 == 0));

    assertEquals(3, res.size());
    assertEquals(2, res.first().first().intValue());
    assertEquals(4, res.first().last().intValue());
    assertEquals(8, res.last().last().intValue());
  }

  @Test
  public void testPartitioners_Navigable2() {
    TreeSet<Integer> ts = new TreeSet<>(Arrays.asList(1, 2, 4, 5, 8, 9));

    TreeSet<TreeSet<Integer>> res = ts.stream()
        .collect(Partitioner.navigableByPredicate((x, y) -> y - x < 2));

    assertEquals(3, res.size());
    assertEquals(1, res.first().first().intValue());
    assertEquals(2, res.first().last().intValue());
    assertEquals(8, res.last().first().intValue());
  }

  @Test
  public void testPartitioners_Navigable3() {
    TreeSet<Integer> ts = new TreeSet<>(Arrays.asList(1, 2, 5, 6, 7, 9));

    TreeSet<TreeSet<Integer>> res = ts.stream()
        .collect(Partitioner.navigableByPredicate((x, y) -> y - x < 5, true));

    assertEquals(2, res.size());
    assertEquals(1, res.first().first().intValue());
    assertEquals(5, res.first().last().intValue());
    assertEquals(6, res.last().first().intValue());
  }
}
