package org.mitre.tdp.boogie.validate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Leg;

class TestEnforceSequentiallyOrderedLegs {

  @Test
  void testEnforceSequentiallyOrderedLegs() {
    Leg n10 = newLeg(10);
    Leg n20 = newLeg(20);

    EnforceSequentiallyOrderedLegs.INSTANCE.accept(Arrays.asList(n10, n20));
    assertThrows(IllegalArgumentException.class, () -> EnforceSequentiallyOrderedLegs.INSTANCE.accept(Arrays.asList(n20, n10)));
  }

  private Leg newLeg(int sequenceNumber) {
    Leg leg = mock(Leg.class);
    when(leg.sequenceNumber()).thenReturn(sequenceNumber);
    return leg;
  }
}
