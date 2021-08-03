package org.mitre.tdp.boogie.alg.chooser;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.alg.LegMergerFactory;
import org.mitre.tdp.boogie.fn.LeftMerger;

class TestLegMergerFactory {

  @Test
  void testSimilarSubsequentLegMerger() {
    LeftMerger<Leg> merger = LegMergerFactory.newSimilarSubsequentLegMerger();

    Leg vaToNowhere = newLeg(null, PathTerminator.VA);
    Leg dfToAdam = newLeg("ADAM", PathTerminator.DF);
    Leg tfToAdam = newLeg("ADAM", PathTerminator.TF);
    Leg rfToAdam = newLeg("ADAM", PathTerminator.RF);

    assertAll(
        () -> assertFalse(merger.mergeable(vaToNowhere, dfToAdam)),
        () -> assertTrue(merger.mergeable(dfToAdam, tfToAdam)),
        () -> assertTrue(merger.mergeable(tfToAdam, tfToAdam)),
        () -> assertFalse(merger.mergeable(tfToAdam, rfToAdam))
    );
  }

  private Leg newLeg(String associatedFixIdentifier, PathTerminator pathTerminator) {
    Fix fix = mock(Fix.class);
    when(fix.fixIdentifier()).thenReturn(associatedFixIdentifier);

    Optional<Fix> o = associatedFixIdentifier == null ? Optional.empty() : Optional.of(fix);

    Leg leg = mock(Leg.class);
    when(leg.associatedFix()).thenReturn((Optional) o);
    when(leg.pathTerminator()).thenReturn(pathTerminator);

    return leg;
  }
}
