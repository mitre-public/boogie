package org.mitre.tdp.boogie.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mitre.tdp.boogie.util.Combinatorics.pairwiseCombos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Pair;

public class TestCombinatorics {

  @Test
  public void testPairwiseCombos() {
    List<Integer> ints = IntStream.rangeClosed(0, 10).boxed().collect(Collectors.toList());
    Iterator<Pair<Integer, Integer>> pairs = pairwiseCombos(ints);

    List<Pair<Integer, Integer>> pairList = new ArrayList<>();
    pairs.forEachRemaining(pairList::add);

    assertEquals(pairList.size(), 55);
  }
}
