package org.mitre.tdp.boogie;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class TestLegTypes {

  @Test
  public void testConcreteLegTypes() {
    List<PathTerm> concrete = Arrays.asList(PathTerm.IF, PathTerm.TF, PathTerm.RF, PathTerm.CF, PathTerm.DF, PathTerm.AF, PathTerm.FM);

    assertTrue(concrete.stream().allMatch(type -> type.isConcrete()));
    assertTrue(Stream.of(PathTerm.values()).filter(type -> !concrete.contains(type)).noneMatch(type -> type.isConcrete()));
  }
}
