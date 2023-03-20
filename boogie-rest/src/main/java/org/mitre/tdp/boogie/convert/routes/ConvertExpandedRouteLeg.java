package org.mitre.tdp.boogie.convert.routes;

import java.util.function.Function;

import org.mitre.tdp.boogie.contract.routes.ExpandedRouteLeg;
import org.mitre.tdp.boogie.contract.routes.ImmutableExpandedRouteLeg;

public class ConvertExpandedRouteLeg implements Function<org.mitre.tdp.boogie.alg.ExpandedRouteLeg, ExpandedRouteLeg> {
  public static final ConvertExpandedRouteLeg INSTANCE = new ConvertExpandedRouteLeg();

  private static final ConvertLeg LEG = ConvertLeg.INSTANCE;

  private ConvertExpandedRouteLeg() {

  }

  @Override
  public ExpandedRouteLeg apply(org.mitre.tdp.boogie.alg.ExpandedRouteLeg expandedRouteLeg) {
    return ImmutableExpandedRouteLeg.builder()
        .section(expandedRouteLeg.section())
        .elementType(expandedRouteLeg.elementType())
        .wildcards(expandedRouteLeg.wildcards())
        .leg(LEG.apply(expandedRouteLeg.leg()))
        .build();
  }
}
