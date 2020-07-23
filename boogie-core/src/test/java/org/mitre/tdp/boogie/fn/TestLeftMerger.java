package org.mitre.tdp.boogie.fn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.junit.jupiter.api.Test;


public class TestLeftMerger {

  @Test
  public void testMerge() {
    List<MergeMe> mergeMes = LongStream.range(0, 10)
        .map(l -> l * 1500L)
        .mapToObj(MergeMe::new)
        .collect(Collectors.toList());

    BiPredicate<MergeMe, MergeMe> pred = (m1, m2) -> m1.name.equals(m2.name) && Math.abs(m1.val - m2.val) <= 1000L;
    BiFunction<MergeMe, MergeMe, MergeMe> mergeLeft = (m1, m2) -> m1;

    LeftMerger<MergeMe> merger = new LeftMerger<>(pred, mergeLeft);

    mergeMes = merger.reduce(mergeMes);
    assertEquals(mergeMes.size(), 10);

    mergeMes = LongStream.range(0, 10)
        .map(l -> l * 501L)
        .mapToObj(MergeMe::new)
        .collect(Collectors.toList());

    mergeMes = merger.reduce(mergeMes);
    assertEquals(mergeMes.size(), 5);
  }
}
