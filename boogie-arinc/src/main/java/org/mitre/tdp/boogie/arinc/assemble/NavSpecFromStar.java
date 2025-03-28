package org.mitre.tdp.boogie.arinc.assemble;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.v22.field.qualifiers.StarQualifier3;

/**
 * This class takes the legs qual3 and finds the PbnNavSpec for this procedure.
 * It currently looks the same as SID, but we can't assume moving forward that will be the case.
 */
public final class NavSpecFromStar implements Function<ArincProcedureLeg, Optional<PbnNavSpec>> {
  public static final NavSpecFromStar INSTANCE = new NavSpecFromStar();
  private NavSpecFromStar() {}

  @Override
  public Optional<PbnNavSpec> apply(ArincProcedureLeg leg) {
      return leg.routeTypeQualifier3()
          .filter(StarQualifier3.VALID::contains)
          .map(StarQualifier3::valueOf)
          .map(NavSpecFromStar::forStarQual3);
    }

    private static PbnNavSpec forStarQual3 (StarQualifier3 qual){
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
