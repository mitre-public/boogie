package org.mitre.tdp.boogie.alg;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.function.BiPredicate;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;

class TestLegMergerFactory {

  @Test
  void testIsLeadingTrailingDF() {
    BiPredicate<Leg, Leg> predicate = LegMergerFactory.isLeadingTrailingDF();

    Leg vaToNowhere = newLeg(null, PathTerminator.VA);
    Leg dfToAdam = newLeg("ADAM", PathTerminator.DF);
    Leg tfToAdam = newLeg("ADAM", PathTerminator.TF);
    Leg rfToAdam = newLeg("ADAM", PathTerminator.RF);

    assertAll(
        () -> assertFalse(predicate.test(vaToNowhere, dfToAdam)),
        () -> assertTrue(predicate.test(dfToAdam, tfToAdam), "Should return true on DF->TF"),
        () -> assertTrue(predicate.test(tfToAdam, dfToAdam), "Should return true on TF->DF"),
        () -> assertFalse(predicate.test(tfToAdam, rfToAdam))
    );
  }

  @Test
  void testIsTrailingInternalIF() {
    BiPredicate<Leg, Leg> predicate = LegMergerFactory.isTrailingInternalIF();

    Leg tfToAdam = newLeg("ADAM", PathTerminator.TF);
    Leg ifToAdam = newLeg("ADAM", PathTerminator.IF);
    Leg rfToAdam = newLeg("ADAM", PathTerminator.RF);

    assertAll(
        () -> assertTrue(predicate.test(tfToAdam, ifToAdam)),
        () -> assertFalse(predicate.test(ifToAdam, tfToAdam)),
        () -> assertTrue(predicate.test(rfToAdam, ifToAdam))
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
