package org.mitre.tdp.boogie.convert.routes;

import java.util.function.Function;

import org.mitre.tdp.boogie.contract.routes.ImmutableLeg;
import org.mitre.tdp.boogie.contract.routes.Leg;

public final class ConvertLeg implements Function<org.mitre.tdp.boogie.Leg, Leg> {
  public static final ConvertLeg INSTANCE = new ConvertLeg();

  private static final ConvertFix FIX = ConvertFix.INSTANCE;
  private static final ConvertTurnDirection DIR = ConvertTurnDirection.INSTANCE;

  private ConvertLeg() {

  }

  @Override
  public Leg apply(org.mitre.tdp.boogie.Leg leg) {
    return ImmutableLeg.builder()
        .pathTerminator(leg.pathTerminator())
        .associatedFix(leg.associatedFix().map(FIX))
        .recommendedNavaid(leg.recommendedNavaid().map(FIX))
        .centerFix(leg.centerFix().map(FIX))
        .sequenceNumber(leg.sequenceNumber())
        .outboundMagneticCourse(leg.outboundMagneticCourse())
        .rho(leg.rho())
        .theta(leg.theta())
        .rnp(leg.rnp())
        .routeDistance(leg.routeDistance())
        .holdTime(leg.holdTime())
        .verticalAngle(leg.verticalAngle())
        .speedConstraint(leg.speedConstraint())
        .altitudeConstraint(leg.altitudeConstraint())
        .turnDirection(leg.turnDirection().map(DIR))
        .isFlyOverFix(leg.isFlyOverFix())
        .isPublishedHoldingFix(leg.isPublishedHoldingFix())
        .build();
  }
}
