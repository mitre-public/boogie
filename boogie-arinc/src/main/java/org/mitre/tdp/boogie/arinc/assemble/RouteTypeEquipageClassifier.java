package org.mitre.tdp.boogie.arinc.assemble;

import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteQualifier.A;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteQualifier.C;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteQualifier.F;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteQualifier.J;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteQualifier.P;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteQualifier.R;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteQualifier.S;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteQualifier.U;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteQualifier.V;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PF_P;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PF_R;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classifier function for procedures {@link RequiredNavigationEquipage}. This designation relies on two key pieces of information:
 * <br>
 * 1. The RouteType + RouteTypeQualifier{1,2} of the procedures - for SID/STAR RNP designations are explicitly called out here but
 * for approaches (especially in older ARINC versions) there is a bit more of an inference layer on top.
 * 2. The procedure name - in later ARINC424 versions <i>most</i> RNP approaches explicitly start with an 'H' in the procedure name
 * and as such this value can be used to override CONV/RNAV designations when present (CIFP makes this distinction as does LIDO,
 * historical Jeppesen does not).
 * <br>
 * Note that this function has a) a relatively gross signature and 2) relies on the {@link ArincTransitionTypeClassifier} to split
 * the legs into labeled transition groupings - as all aircraft must fly the common portion of procedures the basis of their equip
 * designation is based on the route type/qualifier designation of that transition (the ENROUTE/RUNWAY transitions may have diff
 * designations).
 */
public final class RouteTypeEquipageClassifier implements Function<ArincProcedureLeg, Optional<RequiredNavigationEquipage>> {
  private static final Logger log = LoggerFactory.getLogger(RouteTypeEquipageClassifier.class);

  private boolean requiresRnpEquip(ArincRouteType rt, @Nullable ArincRouteQualifier q1, @Nullable ArincRouteQualifier q2) {
    return RNP_TYPES.contains(rt)
        || (rt.equals(PF_R) && q1 != null && q2 != null && q1.equals(R) && q2.equals(S))
        || (rt.equals(PF_R) && q1 != null && q2 != null && q1.equals(A) && q2.equals(S))
        || (rt.equals(PF_R) && q1 != null && q2 != null && q1.equals(F) && q2.equals(S));
  }

  private boolean requiresRnavEquip(ArincRouteType rt, @Nullable ArincRouteQualifier q1, @Nullable ArincRouteQualifier q2) {
    return RNAV_TYPES.contains(rt)
        || (rt.equals(PF_R) && q1 != null && q2 != null && (q1.equals(J) || q1.equals(U) || q1.equals(V) || q1.equals(P)) && q2.equals(S))
        // C is w/o straight in minimums - this looks like a circling approach - but is for GPS... definitely RNAV - probably not RNP
        || (rt.equals(PF_P) && q1 != null && q2 != null && q1.equals(P) && (q2.equals(S) || q2.equals(C)))
        || (rt.arincRouteCharacter().endsWith("R") && !requiresRnpEquip(rt, q1, q2));
  }

  private final Set<ArincRouteType> RNP_TYPES;
  /**
   * The set of concatenated {subSectionCode, routeType} pairs indicating the procedure has {@link RequiredNavigationEquipage#RNAV}.
   */
  private final Set<ArincRouteType> RNAV_TYPES;
  /**
   * The set of concatenated {subSectionCode, routeType} pairs indicating the procedure has {@link RequiredNavigationEquipage#CONV}.
   */
  private final Set<ArincRouteType> CONV_TYPES;

  private RouteTypeEquipageClassifier(Set<ArincRouteType> RNP_TYPES, Set<ArincRouteType> RNAV_TYPES, Set<ArincRouteType> CONV_TYPES) {
    this.RNP_TYPES = RNP_TYPES;
    this.RNAV_TYPES = RNAV_TYPES;
    this.CONV_TYPES = CONV_TYPES;
  }

  public static RouteTypeEquipageClassifier from(Set<ArincRouteType> RNP_TYPES, Set<ArincRouteType> RNAV_TYPES, Set<ArincRouteType> CONV_TYPES) {
    return new RouteTypeEquipageClassifier(RNP_TYPES, RNAV_TYPES, CONV_TYPES);
  }

  @Override
  public Optional<RequiredNavigationEquipage> apply(ArincProcedureLeg leg) {
    return Optional.of(leg).map(this::routeTypeClassifier);
  }

  private RequiredNavigationEquipage routeTypeClassifier(ArincProcedureLeg representative) {
    ArincRouteType arincRouteType = RouteTypeExtractor.INSTANCE.apply(representative);
    ArincRouteQualifier qualifier1 = representative.routeTypeQualifier1().filter(ArincRouteQualifier.VALID::contains).map(ArincRouteQualifier::valueOf).orElse(null);
    ArincRouteQualifier qualifier2 = representative.routeTypeQualifier2().filter(ArincRouteQualifier.VALID::contains).map(ArincRouteQualifier::valueOf).orElse(null);
    String qual3 = representative.routeTypeQualifier3().orElse(null);

    if (CONV_TYPES.contains(arincRouteType)) {
      return RequiredNavigationEquipage.CONV;
    } else if (requiresRnpEquip(arincRouteType, qualifier1, qualifier2)) {
      return RequiredNavigationEquipage.RNP;
    } else if (requiresRnavEquip(arincRouteType, qualifier1, qualifier2)) {
      return RequiredNavigationEquipage.RNAV;
    } else {
      // we could throw a hard exception here - but our tests quantify how often this can happen and it should be uncommon enough as to not matter
      log.warn("Unable to classify equipage based on routeType {} qualifier1 {} qualifier2 {} qualifier3 {}.", arincRouteType, qualifier1, qualifier2, qual3);
      return RequiredNavigationEquipage.UNKNOWN;
    }
  }
}

