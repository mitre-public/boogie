package org.mitre.boogie.xml.v23_4.util;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.fields.ArincFraInfo;
import org.mitre.boogie.xml.v23_4.generated.FraInfo;

/**
 * This class converts from the generated fra info to the internal model
 */
public final class FraConverter implements Function<FraInfo, ArincFraInfo> {
  public static final FraConverter INSTANCE = new FraConverter();
  private FraConverter() {}
  @Override
  public ArincFraInfo apply(FraInfo fraInfo) {
    boolean fraArrivalTransition = Optional.ofNullable(fraInfo.isIsFraArrivalTransitionPoint()).orElse(false);
    boolean fraDepartureTransition = Optional.ofNullable(fraInfo.isIsFraDepartureTransitionPoint()).orElse(false);
    boolean fraIntermediate = Optional.ofNullable(fraInfo.isIsFraIntermediatePoint()).orElse(false);
    boolean fraTerminalHolding = Optional.ofNullable(fraInfo.isIsFraTerminalHoldingPoint()).orElse(false);
    boolean fraEntryPoint = Optional.ofNullable(fraInfo.isIsFraEntryPoint()).orElse(false);
    boolean fraExitPoint = Optional.ofNullable(fraInfo.isIsFraExitPoint()).orElse(false);
    return ArincFraInfo.builder()
        .fraArrivalTransitionPoint(fraArrivalTransition)
        .fraDepartureTransitionPoint(fraDepartureTransition)
        .fraIntermediatePoint(fraIntermediate)
        .fraTerminalHoldingPoint(fraTerminalHolding)
        .fraEntryPoint(fraEntryPoint)
        .fraExitPoint(fraExitPoint)
        .build();
  }
}
