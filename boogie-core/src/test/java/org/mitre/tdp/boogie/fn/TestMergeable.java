package org.mitre.tdp.boogie.fn;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class TestMergeable {

  @Test
  public void testMerge() {
    List<MergeMe> mergeMes = LongStream.range(0, 10)
        .map(l -> l * 1500L)
        .mapToObj(MergeMe::new)
        .collect(Collectors.toList());

    mergeMes = Mergeable.reduce(mergeMes);
    assertEquals(mergeMes.size(), 10);

    mergeMes = LongStream.range(0, 10)
        .map(l -> l * 501L)
        .mapToObj(MergeMe::new)
        .collect(Collectors.toList());

    mergeMes = Mergeable.reduce(mergeMes);
    assertEquals(mergeMes.size(), 5);
  }
}
