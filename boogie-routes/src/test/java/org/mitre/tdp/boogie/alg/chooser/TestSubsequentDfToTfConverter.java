package org.mitre.tdp.boogie.alg.chooser;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.alg.ExpandedRouteLeg;
import org.mitre.tdp.boogie.alg.resolve.ElementType;

class TestSubsequentDfToTfConverter {

  private static final SubsequentDfToTfConverter converter = new SubsequentDfToTfConverter();

  @Test
  void testSectionMatchesFix() {
    assertAll(
        () -> assertTrue(converter.sectionMatchesFix(newExpandedRouteLeg("MARCO", mockLeg("MARCO", PathTerminator.DF))), "Section (MARCO) matches name (MARCO)"),
        () -> assertFalse(converter.sectionMatchesFix(newExpandedRouteLeg("MARCO", mockLeg("DAVID", PathTerminator.DF))), "Section (MARCO) matches name (DAVID)"),
        () -> assertFalse(converter.sectionMatchesFix(newExpandedRouteLeg("DAVID", mockLeg("MARCO", PathTerminator.DF))), "Section (DAVID) matches name (MARCO)")
    );
  }

  @Test
  void testUpdateSequentialDFLegsToTF() {

    PathTerminator convertedTerminator = converter.updateSequentialDFLegsToTF(
        newExpandedRouteLeg("JIIMS2", mockLeg("MARCO", PathTerminator.TF)),
        newExpandedRouteLeg("DAVID", mockLeg("DAVID", PathTerminator.DF))
    ).second().leg().pathTerminator();

    PathTerminator unconvertedTerminator = converter.updateSequentialDFLegsToTF(
        newExpandedRouteLeg("JIIMS2", mockLeg("MARCO", PathTerminator.DF)),
        newExpandedRouteLeg("JIIMS2", mockLeg("DAVID", PathTerminator.DF))
    ).second().leg().pathTerminator();

    assertAll(
        () -> assertEquals(PathTerminator.TF, convertedTerminator, "DF should be converted to TF."),
        () -> assertEquals(PathTerminator.DF, unconvertedTerminator, "DF should not be converted to TF.")
    );
  }

  @Test
  void testApply() {
    List<ExpandedRouteLeg> initialSequence = Arrays.asList(
        newExpandedRouteLeg("JIIMS2", mockLeg("MARCO", PathTerminator.DF)),
        newExpandedRouteLeg("DAVID", mockLeg("DAVID", PathTerminator.DF)),
        newExpandedRouteLeg("ALEX", mockLeg("ALEX", PathTerminator.DF)),
        newExpandedRouteLeg("MACK1", mockLeg("VAN", PathTerminator.DF)),
        newExpandedRouteLeg("MACK1", mockLeg("MATT", PathTerminator.TF))
    );

    List<ExpandedRouteLeg> finalSequence = converter.apply(initialSequence);

    assertAll(
        () -> assertEquals(initialSequence.get(0), finalSequence.get(0), "Initial leg should not be modified"),
        () -> assertEquals(PathTerminator.TF, finalSequence.get(1).leg().pathTerminator(), "Second leg should be modified to TF"),
        () -> assertEquals(PathTerminator.TF, finalSequence.get(2).leg().pathTerminator(), "Third leg should be modified to TF"),
        () -> assertEquals(initialSequence.get(3), finalSequence.get(3), "Fourth leg should not be modified"),
        () -> assertEquals(initialSequence.get(4), finalSequence.get(4), "Fifth leg should not be modified")
    );
  }

  private ExpandedRouteLeg newExpandedRouteLeg(String sectionValue, Leg leg) {
    return new ExpandedRouteLeg(sectionValue, ElementType.FIX, "", leg);
  }

  private Leg mockLeg(String fixName, PathTerminator pathTerminator) {
    Fix fix = mock(Fix.class);
    when(fix.fixIdentifier()).thenReturn(fixName);

    Leg leg = mock(Leg.class);
    when(leg.associatedFix()).thenReturn((Optional) Optional.of(fix));
    when(leg.pathTerminator()).thenReturn(pathTerminator);
    return leg;
  }
}
