package org.mitre.tdp.boogie.alg.graph;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.PathTerm;

class TestMultipleExpandedLegMerger {

  private final MultiplyExpandedLegMerger merger = new MultiplyExpandedLegMerger();

  @Test
  void testMatchingOrDfLegTypes() {
    assertAll(
        () -> assertTrue(merger.matchingOrDFLegTypes(PathTerm.DF, PathTerm.AF)),
        () -> assertTrue(merger.matchingOrDFLegTypes(PathTerm.CF, PathTerm.DF)),
        () -> assertTrue(merger.matchingOrDFLegTypes(PathTerm.TF, PathTerm.TF)),
        () -> assertFalse(merger.matchingOrDFLegTypes(PathTerm.VA, PathTerm.VI))
    );
  }

  @Test
  void testMatchingPathTerminators() {
    assertFalse(merger.matchingPathTerminators(null, null));

    Fix f1 = mock(Fix.class);
    Fix f2 = mock(Fix.class);

    assertFalse(merger.matchingPathTerminators(f1, f2));

    when(f1.identifier()).thenReturn("HI");
    when(f2.identifier()).thenReturn("HI");

    assertTrue(merger.matchingPathTerminators(f1, f2));
  }
}
