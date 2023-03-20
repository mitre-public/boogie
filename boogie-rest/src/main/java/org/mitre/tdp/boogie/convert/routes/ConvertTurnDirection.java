package org.mitre.tdp.boogie.convert.routes;

import java.util.function.Function;

import org.mitre.tdp.boogie.contract.routes.ImmutableTurnDirection;
import org.mitre.tdp.boogie.contract.routes.TurnDirection;

public final class ConvertTurnDirection implements Function<org.mitre.tdp.boogie.TurnDirection, TurnDirection> {
  public static final ConvertTurnDirection INSTANCE = new ConvertTurnDirection();

  private ConvertTurnDirection() {

  }

  @Override
  public TurnDirection apply(org.mitre.tdp.boogie.TurnDirection turnDirection) {
    return ImmutableTurnDirection.builder()
        .isLeft(turnDirection.isLeft())
        .isRight(turnDirection.isRight())
        .build();
  }
}
