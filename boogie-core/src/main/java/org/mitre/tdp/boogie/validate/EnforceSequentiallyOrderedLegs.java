package org.mitre.tdp.boogie.validate;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.util.Iterators;

import com.google.common.collect.Ordering;

/**
 * This class enforces that all encountered legs in the provided list occur in sequence number order - the first pair of legs
 * which occur out of order are included in the thrown exception if encountered.
 */
public final class EnforceSequentiallyOrderedLegs implements Consumer<List<? extends Leg>> {

  public static final EnforceSequentiallyOrderedLegs INSTANCE = new EnforceSequentiallyOrderedLegs();

  private EnforceSequentiallyOrderedLegs() {
  }

  @Override
  public void accept(List<? extends Leg> legs) {
    Ordering<Leg> ordering = Ordering.from(Comparator.comparing(Leg::sequenceNumber));
    if (!ordering.isOrdered(legs)) {
      Iterators.pairwise(legs, this::throwExceptionIfOutOfOrder);
    }
  }

  private void throwExceptionIfOutOfOrder(Leg l1, Leg l2) {
    if (l1.sequenceNumber() > l2.sequenceNumber()) {

      String message = "Inputs legs are out of natural sequence number order. First out-of-order pairing was:\n"
          .concat("Previous: ".concat(l1.toString()))
          .concat("Next: ".concat(l2.toString()));

      throw new IllegalArgumentException(message);
    }
  }
}
