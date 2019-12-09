package org.mitre.tdp.boogie;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestLegTypes {

  @Test
  public void testConcreteLegTypes() {
    List<LegType> concrete = Arrays.asList(LegType.IF, LegType.TF, LegType.RF, LegType.CF, LegType.DF, LegType.AF);

    assertTrue(concrete.stream().allMatch(type -> type.concrete()));
    assertTrue(Stream.of(LegType.values()).filter(type -> !concrete.contains(type)).noneMatch(type -> type.concrete()));
  }
}
