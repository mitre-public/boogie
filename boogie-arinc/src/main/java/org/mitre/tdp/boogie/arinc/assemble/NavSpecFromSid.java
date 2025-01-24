package org.mitre.tdp.boogie.arinc.assemble;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.v22.field.qualifiers.SidQualifier3;

/**
 * Picks the pbn nav spec from the route qualifier.
 * For now this matches STARs but we can't just assume that will be true.
 */
public final class NavSpecFromSid implements Function<ArincProcedureLeg, Optional<PbnNavSpec>> {
  public static final NavSpecFromSid INSTANCE = new NavSpecFromSid();
  private NavSpecFromSid() {}
  @Override
  public Optional<PbnNavSpec> apply(ArincProcedureLeg leg) {
    return leg.routeTypeQualifier3()
        .filter(SidQualifier3.VALID::contains)
        .map(SidQualifier3::valueOf)
        .map(NavSpecFromSid::fromSidQual3);
  }

  private static PbnNavSpec fromSidQual3(SidQualifier3 qual) {
    return switch (qual) {
      case Z -> PbnNavSpec.RNAV_5;
      case Y -> PbnNavSpec.RNAV_2;
      case X -> PbnNavSpec.RNAV_1;
      case B -> PbnNavSpec.B_RNAV;
      case P -> PbnNavSpec.P_RNAV;
      case D -> PbnNavSpec.RNP_2;
      case E -> PbnNavSpec.RNP_1;
      case F -> PbnNavSpec.RNP_AR;
      case A -> PbnNavSpec.ADVANCED_RNP;
      case G -> PbnNavSpec.RNP_03;
      case M -> PbnNavSpec.EITHER_RNP1_RNAV1;
      case U -> PbnNavSpec.NOT_SPECIFIED;
      case V -> PbnNavSpec.VOR_DME_RNAV;
      case SPEC -> throw new IllegalStateException("Can't have this value");
    };
  }
}
