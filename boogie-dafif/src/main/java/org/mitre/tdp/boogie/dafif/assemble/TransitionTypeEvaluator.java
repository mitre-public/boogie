package org.mitre.tdp.boogie.dafif.assemble;

import java.util.function.Function;

import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.dafif.model.DafifTerminalSegment;

/**
 * This class sorts through the terminal segment and finds its transition type.
 */
public final class TransitionTypeEvaluator implements Function<DafifTerminalSegment, TransitionType> {
  public static final  TransitionTypeEvaluator INSTANCE = new TransitionTypeEvaluator();
  private TransitionTypeEvaluator() {
  }

  @Override
  public TransitionType apply(DafifTerminalSegment dafifTerminalSegment) {
    return switch (dafifTerminalSegment.terminalProcedureType()) {
      case 1 -> star(dafifTerminalSegment);
      case 2 -> sid(dafifTerminalSegment);
      case 3 -> approach(dafifTerminalSegment);
      default -> throw new IllegalArgumentException("Unknown terminal procedure type: " + dafifTerminalSegment.terminalProcedureType());
    };
  }

  private TransitionType sid(DafifTerminalSegment dafifTerminalSegment) {
    return switch (dafifTerminalSegment.terminalApproachType()) {
      case "1", "4" -> TransitionType.RUNWAY;
      case "2", "5" -> TransitionType.COMMON;
      case "3", "6" -> TransitionType.ENROUTE;
      default -> throw new IllegalArgumentException("Unknown transition type: " + dafifTerminalSegment.terminalApproachType());
    };
  }

  private TransitionType star(DafifTerminalSegment dafifTerminalSegment) {
    return switch (dafifTerminalSegment.terminalApproachType()) {
      case "1", "4" -> TransitionType.ENROUTE;
      case "2", "5" -> TransitionType.COMMON;
      case "3", "6" -> TransitionType.RUNWAY;
      default -> throw new IllegalArgumentException("Unknown transition type: " + dafifTerminalSegment.terminalApproachType());
    };
  }

  private TransitionType approach(DafifTerminalSegment dafifTerminalSegment) {
    if (dafifTerminalSegment.terminalWaypointDescriptionCode3().map("M"::equals).orElse(false)) {
      return TransitionType.MISSED;
    }
    if (dafifTerminalSegment.terminalApproachType().equals("A")) {
      return TransitionType.APPROACH;
    }
    return TransitionType.COMMON;
  }
}
