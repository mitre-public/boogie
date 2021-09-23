package org.mitre.tdp.boogie.alg;

import java.util.List;
import java.util.function.UnaryOperator;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.model.BoogieLeg;

import com.google.common.collect.Lists;

public class DirectToFixPostProcessor implements UnaryOperator<ExpandedRoute> {

  public static final DirectToFixPostProcessor INSTANCE = new DirectToFixPostProcessor();

  private DirectToFixPostProcessor() {
  }

  private static boolean containsDirectToFiledFix(ExpandedRoute route) {
    return route.legs().stream().anyMatch(DirectToFixPostProcessor::isDirectToFiledFix);
  }

  private static ExpandedRoute convertAllInstancesInPlan(ExpandedRoute expandedRoute) {
    List<ExpandedRoute.ExpandedRouteLeg> legs = Lists.newArrayList();
    for (ExpandedRoute.ExpandedRouteLeg expandedRouteLeg : expandedRoute.legs()) {
      if (isDirectToFiledFix(expandedRouteLeg)) {
        legs.add(makeExpandedTfRouteLeg(expandedRouteLeg));
      } else {
        legs.add(expandedRouteLeg);
      }
    }
    return ExpandedRoute.toBuilder(expandedRoute).legs(legs).build();
  }

  private static boolean isDirectToFiledFix(ExpandedRoute.ExpandedRouteLeg leg) {
    return leg.elementType().equals(ElementType.FIX) && leg.pathTerminator().equals(PathTerminator.DF);
  }

  private static ExpandedRoute.ExpandedRouteLeg makeExpandedTfRouteLeg(ExpandedRoute.ExpandedRouteLeg expandedRouteLeg) {
    return new ExpandedRoute.ExpandedRouteLeg(expandedRouteLeg.section(), expandedRouteLeg.elementType(), expandedRouteLeg.wildcards(), makeBoogieLeg(expandedRouteLeg.leg()));
  }

  private static BoogieLeg makeBoogieLeg(Leg leg) {
    return new BoogieLeg.Builder()
        .associatedFix(leg.associatedFix().orElse(null))
        .recommendedNavaid(leg.recommendedNavaid().orElse(null))
        .pathTerminator(PathTerminator.TF)
        .sequenceNumber(leg.sequenceNumber())
        .speedConstraint(leg.speedConstraint())
        .altitudeConstraint(leg.altitudeConstraint())
        .outboundMagneticCourse(leg.outboundMagneticCourse().orElse(null))
        .theta(leg.theta().orElse(null))
        .rho(leg.rho().orElse(null))
        .rnp(leg.rnp().orElse(null))
        .routeDistance(leg.routeDistance().orElse(null))
        .holdTime(leg.holdTime().orElse(null))
        .isPublishedHoldingFix(leg.isPublishedHoldingFix())
        .isFlyOverFix(leg.isFlyOverFix())
        .build();
  }

  @Override
  public ExpandedRoute apply(ExpandedRoute expandedRoute) {
    return containsDirectToFiledFix(expandedRoute) ? convertAllInstancesInPlan(expandedRoute) : expandedRoute;
  }
}
