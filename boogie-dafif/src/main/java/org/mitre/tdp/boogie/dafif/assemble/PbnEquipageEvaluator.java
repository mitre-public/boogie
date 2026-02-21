package org.mitre.tdp.boogie.dafif.assemble;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.dafif.model.DafifTerminalSegment;

/**
 * This class sorts through the terminal segment and finds its pbn equipage requirements.
 */
public final class PbnEquipageEvaluator implements Function<DafifTerminalSegment, RequiredNavigationEquipage> {
  public static final PbnEquipageEvaluator INSTANCE = new PbnEquipageEvaluator();
  private PbnEquipageEvaluator() {}

  @Override
  public RequiredNavigationEquipage apply(DafifTerminalSegment dafifTerminalSegment) {
    return switch (dafifTerminalSegment.terminalProcedureType()) {
      case 1, 2 -> sidStar(dafifTerminalSegment);
      case 3 -> approach(dafifTerminalSegment);
      default -> throw new IllegalArgumentException("Unknown procedure type: " + dafifTerminalSegment.terminalProcedureType());
    };
  }

  private RequiredNavigationEquipage sidStar(DafifTerminalSegment dafifTerminalSegment) {
    return switch (dafifTerminalSegment.terminalApproachType()) {
      case "1", "2", "3" -> RequiredNavigationEquipage.CONV;
      case "4", "5", "6" -> RequiredNavigationEquipage.RNAV;
      default -> RequiredNavigationEquipage.UNKNOWN;
    };
  }

  private RequiredNavigationEquipage approach(DafifTerminalSegment dafifTerminalSegment) {
    String swappedForApproachTransition = Optional.of(dafifTerminalSegment).map(DafifTerminalSegment::terminalApproachType)
        .filter("A"::equals).map(i -> dafifTerminalSegment.terminalIdentifier().substring(0, 1))
        .orElse(dafifTerminalSegment.terminalApproachType());
    return switch (swappedForApproachTransition) {
      case "B", "C", "D", "E", "F", "H", "I", "L", "M", "N", "P", "Q", "S", "T", "U", "V", "W", "X", "Y", "Z", "2", "3" -> RequiredNavigationEquipage.CONV;
      case "G", "K", "O", "1" -> RequiredNavigationEquipage.RNAV;
      case "R" -> RequiredNavigationEquipage.RNP;
      case "A" -> throw new IllegalArgumentException("Tried to get rid of that one, so should not be here now: " + dafifTerminalSegment.terminalApproachType());
      default ->  RequiredNavigationEquipage.UNKNOWN;
    };
  }
}
