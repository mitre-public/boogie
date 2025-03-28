package org.mitre.tdp.boogie.arinc.assemble;

import java.util.function.BiPredicate;

import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;
import org.mitre.tdp.boogie.arinc.v18.field.SequenceNumber;

public final class ShouldSplitAirway implements BiPredicate<ArincAirwayLeg, ArincAirwayLeg> {
  public static final ShouldSplitAirway INSTANCE = new ShouldSplitAirway();

  /**
   * Splits subsequent singleton airway records when their initial sequence number jumps by at least 1.
   * <br>
   * i.e. most sequence number are 0010, 0020, 1020, 3050 - we want to split on 0010 -> 1020.
   * <br>
   * Context in {@link SequenceNumber}.
   */
  private static final BiPredicate<ArincAirwayLeg, ArincAirwayLeg> SEQ_JUMP = (previous, next) -> !formattedSequenceNumber(previous).startsWith(formattedSequenceNumber(next).substring(0, 1));
  private static final BiPredicate<ArincAirwayLeg, ArincAirwayLeg> SEQ_RESET = (previous, next) -> next.sequenceNumber() <= previous.sequenceNumber();
  private static final BiPredicate<ArincAirwayLeg, ArincAirwayLeg> DIFF_IDENT = (previous, next) -> !previous.routeIdentifier().equals(next.routeIdentifier());


  private ShouldSplitAirway() {
  }

  @Override
  public boolean test(ArincAirwayLeg previous, ArincAirwayLeg next) {
    return SEQ_JUMP.or(SEQ_RESET).or(DIFF_IDENT).test(previous, next);
  }

  private static String formattedSequenceNumber(ArincAirwayLeg arincAirwayLeg) {
    return String.format("%04d", arincAirwayLeg.sequenceNumber());
  }
}
