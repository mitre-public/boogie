package org.mitre.tdp.boogie.arinc.assemble;

import static com.google.common.collect.Sets.newHashSet;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteQualifier.A;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteQualifier.C;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteQualifier.F;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteQualifier.J;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteQualifier.P;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteQualifier.R;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteQualifier.S;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteQualifier.U;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteQualifier.V;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PD_1;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PD_2;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PD_3;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PD_4;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PD_5;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PD_6;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PD_F;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PD_M;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PD_N;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PD_P;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PD_R;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PD_S;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PD_T;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PD_V;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PE_1;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PE_2;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PE_3;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PE_4;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PE_5;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PE_6;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PE_F;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PE_M;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PE_N;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PE_P;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PE_R;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PE_S;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PF_B;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PF_D;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PF_G;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PF_H;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PF_I;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PF_J;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PF_L;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PF_M;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PF_N;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PF_P;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PF_Q;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PF_R;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PF_S;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PF_T;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PF_U;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PF_V;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PF_W;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PF_X;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PF_Y;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Multimap;

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
final class ArincRequiredEquipageClassifier implements Function<Multimap<TransitionType, List<ArincProcedureLeg>>, RequiredNavigationEquipage> {

  private static final Logger LOG = LoggerFactory.getLogger(ArincRequiredEquipageClassifier.class);

  @Override
  public RequiredNavigationEquipage apply(Multimap<TransitionType, List<ArincProcedureLeg>> transitionsByType) {
    ArincProcedureLeg representative = representativeProcedureLeg(transitionsByType);

    ArincRouteType arincRouteType = ArincRouteType.from(representative);
    ArincRouteQualifier qualifier1 = representative.routeTypeQualifier1().map(ArincRouteQualifier::valueOf).orElse(null);
    ArincRouteQualifier qualifier2 = representative.routeTypeQualifier2().map(ArincRouteQualifier::valueOf).orElse(null);

    if (CONV_TYPES.contains(arincRouteType)) {
      return RequiredNavigationEquipage.CONV;
    } else if (requiresRnpEquip(arincRouteType, qualifier1, qualifier2)) {
      return RequiredNavigationEquipage.RNP;
    } else if (requiresRnavEquip(arincRouteType, qualifier1, qualifier2)) {
      return RequiredNavigationEquipage.RNAV;
    } else {
      LOG.warn("Unable to classify equipage based on routeType {} qualifier1 {} qualifier2 {}.", arincRouteType, qualifier1, qualifier2);
      throw new IllegalStateException("Unable to classify RequiredNavigationEquipage of leg: ".concat(representative.toString()));
    }
  }

  /**
   * The representative procedure leg (from an equipage perspective) should be one of the {@link TransitionType#COMMON} legs or
   * if there is no such leg (procedure lacks a common portion) it shouldn't matter and the routeType/qualifiers from any of the
   * legs should be usable for the qualification.
   */
  private ArincProcedureLeg representativeProcedureLeg(Multimap<TransitionType, List<ArincProcedureLeg>> transitionsByType) {
    return transitionsByType.get(TransitionType.COMMON).stream().flatMap(Collection::stream).findFirst()
        .orElseGet(() -> transitionsByType.entries().stream().flatMap(e -> e.getValue().stream()).findFirst().orElseThrow(IllegalStateException::new));
  }

  /**
   * The set of concatenated {subSectionCode, routeType} pairs indicating the procedure has {@link RequiredNavigationEquipage#RNP}.
   */
  private static final Set<ArincRouteType> RNP_TYPES = newHashSet(PD_F, PD_M, PD_S, PE_F, PE_M, PE_S, PD_R, PD_N, PD_P, PE_R, PE_N, PE_P, PF_H);

  private static boolean requiresRnpEquip(ArincRouteType rt, @Nullable ArincRouteQualifier q1, @Nullable ArincRouteQualifier q2) {
    return RNP_TYPES.contains(rt)
        || (rt.equals(PF_R) && q1 != null && q2 != null && q1.equals(R) && q2.equals(S))
        || (rt.equals(PF_R) && q1 != null && q2 != null && q1.equals(A) && q2.equals(S))
        || (rt.equals(PF_R) && q1 != null && q2 != null && q1.equals(F) && q2.equals(S));
  }

  /**
   * The set of concatenated {subSectionCode, routeType} pairs indicating the procedure has {@link RequiredNavigationEquipage#RNAV}.
   */
  private static final Set<ArincRouteType> RNAV_TYPES = newHashSet(PD_4, PD_5, PD_6, PE_4, PE_5, PE_6);

  private static boolean requiresRnavEquip(ArincRouteType rt, @Nullable ArincRouteQualifier q1, @Nullable ArincRouteQualifier q2) {
    return RNAV_TYPES.contains(rt)
        || (rt.equals(PF_R) && q1 != null && q2 != null && (q1.equals(J) || q1.equals(U) || q1.equals(V) || q1.equals(P)) && q2.equals(S))
        // C is w/o straight in minimums - this looks like a circling approach - but is for GPS... definitely RNAV - probably not RNP
        || (rt.equals(PF_P) && q1 != null && q2 != null && q1.equals(P) && (q2.equals(S) || q2.equals(C)))
        || (rt.arincRouteCharacter().endsWith("R") && !requiresRnpEquip(rt, q1, q2));
  }

  /**
   * The set of concatenated {subSectionCode, routeType} pairs indicating the procedure has {@link RequiredNavigationEquipage#CONV}.
   */
  private static final Set<ArincRouteType> CONV_TYPES = newHashSet(PD_1, PD_2, PD_3, PD_T, PD_V, PE_1, PE_2, PE_3, PF_T, PF_J, PF_L, PF_G, PF_U, PF_X, PF_N, PF_V, PF_M, PF_Q, PF_W, PF_I, PF_S, PF_B, PF_D, PF_Y);
}
