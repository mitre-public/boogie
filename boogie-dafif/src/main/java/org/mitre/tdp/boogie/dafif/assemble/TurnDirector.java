package org.mitre.tdp.boogie.dafif.assemble;

import java.util.function.Function;

import org.mitre.tdp.boogie.TurnDirection;

public final class TurnDirector implements Function<String, TurnDirection> {
  public static final TurnDirector INSTANCE = new TurnDirector();
  private TurnDirector() {}
  @Override
  public TurnDirection apply(String s) {
    return switch (s) {
      case "L" -> TurnDirection.left();
      case "R" -> TurnDirection.right();
      default -> TurnDirection.either();
    };
  }
}
