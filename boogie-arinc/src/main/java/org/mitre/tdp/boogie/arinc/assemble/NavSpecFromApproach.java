package org.mitre.tdp.boogie.arinc.assemble;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.v21.field.qualifiers.ApproachQualifier3;

/**
 * The approach has a redundant value making the whole route qualifier nonsense require all this hassle over one class
 */
public final class NavSpecFromApproach implements Function<ArincProcedureLeg, Optional<PbnNavSpec>> {
  public static final NavSpecFromApproach INSTANCE = new NavSpecFromApproach();
  private NavSpecFromApproach() {}
  @Override
  public Optional<PbnNavSpec> apply(ArincProcedureLeg leg) {
    return leg.routeTypeQualifier3()
        .filter(ApproachQualifier3.VALID::contains)
        .map(ApproachQualifier3::valueOf)
        .map(NavSpecFromApproach::fromApproachQual);
  }
  private static PbnNavSpec fromApproachQual(ApproachQualifier3 qual) {
    return switch (qual) {
      case X -> PbnNavSpec.RNAV_1;
      case E -> PbnNavSpec.RNP_1;
      case H -> PbnNavSpec.RNP_APCH;
      case G -> PbnNavSpec.RNP_03;
      case A -> PbnNavSpec.ADVANCED_RNP;
      case F -> PbnNavSpec.RNP_AR;
      case B -> PbnNavSpec.RNAV_VISUAL;
      case SPEC -> throw new IllegalStateException("Can't have this value");
    };
  }
}
