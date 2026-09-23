package org.mitre.tdp.boogie.arinc.assemble;

import java.util.function.BiPredicate;

import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;
import org.mitre.tdp.boogie.arinc.v18.field.SequenceNumber;

public final class ShouldSplitAirway implements BiPredicate<ArincAirwayLeg, ArincAirwayLeg> {
  public static final ShouldSplitAirway INSTANCE = new ShouldSplitAirway();

  private ShouldSplitAirway() {
  }

  /**
   * Splits when the leading digit of the four-character {@link SequenceNumber} changes, the sequence resets, or the route
   * identifier changes. For example, 0010 to 0020 stays together, while 0010 to 1020 starts another airway.
   */
  @Override
  public boolean test(ArincAirwayLeg previous, ArincAirwayLeg next) {
    int previousSequence = previous.sequenceNumber();
    int nextSequence = next.sequenceNumber();
    return sequencePrefix(previousSequence) != sequencePrefix(nextSequence)
        || nextSequence <= previousSequence
        || !previous.routeIdentifier().equals(next.routeIdentifier());
  }

  private static int sequencePrefix(int sequenceNumber) {
    // Match the leading sign/digit of %04d even for values outside the normal four-digit field range.
    if (sequenceNumber < 0) {
      return -1;
    }
    while (sequenceNumber >= 10_000) {
      sequenceNumber /= 10;
    }
    return sequenceNumber / 1_000;
  }
}
