package org.mitre.tdp.boogie.arinc.assemble;

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
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PF_Q;
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

import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Multimap;


/**
 * This class takes a set of procedure legs that were mapped by their transition types.
 * Then seeks to apply the newer route qualifier 3 based logic to categorize the procedure
 * If the new logic does not work then the old route type logic is also attempted.
 * This allows the same classifier to work on current, legacy, and mixed arinc 424 version coding.
 */
final class ArincRequiredEquipageClassifier implements Function<Multimap<TransitionType, List<ArincProcedureLeg>>, RequiredNavigationEquipage> {
  private static final Logger log = LoggerFactory.getLogger(ArincRequiredEquipageClassifier.class);

  /**
   * The set of concatenated {subSectionCode, routeType} pairs indicating the procedure has {@link RequiredNavigationEquipage#RNP}.
   */
  private static final Set<ArincRouteType> RNP_TYPES = Set.of(PD_F, PD_M, PD_S, PE_F, PE_M, PE_S, PD_R, PD_N, PD_P, PE_R, PE_N, PE_P, PF_H);
  /**
   * The set of concatenated {subSectionCode, routeType} pairs indicating the procedure has {@link RequiredNavigationEquipage#RNAV}.
   */
  private static final Set<ArincRouteType> RNAV_TYPES = Set.of(PD_4, PD_5, PD_6, PE_4, PE_5, PE_6);
  /**
   * The set of concatenated {subSectionCode, routeType} pairs indicating the procedure has {@link RequiredNavigationEquipage#CONV}.
   */
  private static final Set<ArincRouteType> CONV_TYPES = Set.of(PD_1, PD_2, PD_3, PD_T, PD_V, PE_1, PE_2, PE_3, PF_T, PF_J, PF_L, PF_G, PF_U, PF_X, PF_N, PF_V, PF_M, PF_Q, PF_W, PF_I, PF_S, PF_B, PF_D, PF_Y);

  private static final NavSpecEquipageClassifier NAV_SPEC = NavSpecEquipageClassifier.from(CONV_TYPES);
  private static final RouteTypeEquipageClassifier ROUTE_TYPE = RouteTypeEquipageClassifier.from(RNP_TYPES, RNAV_TYPES, CONV_TYPES);

  @Override
  public RequiredNavigationEquipage apply(Multimap<TransitionType, List<ArincProcedureLeg>> transitionsByType) {
    ArincProcedureLeg representative = representativeProcedureLeg(transitionsByType);
    return NAV_SPEC.apply(representative)
        .or(() -> ROUTE_TYPE.apply(representative))
        .orElseGet(() -> {
          log.warn("Could not categorize procedure with old or new method {}", representative);
          return RequiredNavigationEquipage.UNKNOWN;
        });
  }

  /**
   * The representative procedure leg (from an equipage perspective) should be one of the {@link TransitionType#COMMON} legs or
   * if there is no such leg (procedure lacks a common portion) it shouldn't matter and the routeType/qualifiers from any of the
   * legs should be usable for the qualification.
   */
  private static ArincProcedureLeg representativeProcedureLeg(Multimap<TransitionType, List<ArincProcedureLeg>> transitionsByType) {
    return transitionsByType.get(TransitionType.COMMON).stream().flatMap(Collection::stream).findFirst()
        .or(() -> transitionsByType.entries().stream().flatMap(e -> e.getValue().stream()).findFirst())
        .orElseThrow(IllegalStateException::new);
  }
}
