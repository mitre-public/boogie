package org.mitre.tdp.boogie.arinc.assemble;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;

/**
 * This approach to classifying a procedure works by checking to see if the route qualifier 3 is there, if so use it.
 * If not check the route type to see if this is conventional.
 */
public final class NavSpecEquipageClassifier implements Function<ArincProcedureLeg, Optional<RequiredNavigationEquipage>> {
  /**
   * The set of concatenated {subSectionCode, routeType} pairs indicating the procedure has {@link RequiredNavigationEquipage#CONV}.
   */
  private final Set<ArincRouteType> CONV_TYPES;

  private NavSpecEquipageClassifier(Set<ArincRouteType> CONV_TYPES) {
    this.CONV_TYPES = CONV_TYPES;
  }

  public static NavSpecEquipageClassifier from(Set<ArincRouteType> CONV_TYPES) {
    return new NavSpecEquipageClassifier(CONV_TYPES);
  }

  private static Optional<RequiredNavigationEquipage> findIfPBN(ArincProcedureLeg leg) {
    String section = leg.sectionCode().name();
    String subsection = leg.subSectionCode().orElseThrow(() -> new IllegalStateException("Should not have procedure legs without subsection code: ".concat(leg.toString())));
    ProcedureSectionSubsection sectionSubsection = ProcedureSectionSubsection.valueOf(section.concat(subsection));
    return switch (sectionSubsection) {
      case PD -> NavSpecFromSid.INSTANCE.apply(leg).map(NavSpecEquipageClassifier::fromNavSpec);
      case PE -> NavSpecFromStar.INSTANCE.apply(leg).map(NavSpecEquipageClassifier::fromNavSpec);
      case PF -> NavSpecFromApproach.INSTANCE.apply(leg).map(NavSpecEquipageClassifier::fromNavSpec);
    };
  }

  private static Optional<RequiredNavigationEquipage> findIfSidStarIsConventional(ArincProcedureLeg leg) {
    return leg.routeTypeQualifier2()
        .filter(i -> i.equals("G") || ( i.equals("F") && leg.routeTypeQualifier3().isEmpty())) //G is conventional and the other is a corner case due to bad arinc design
        .map(i -> RequiredNavigationEquipage.CONV);
  }

  private static RequiredNavigationEquipage fromNavSpec(PbnNavSpec navSpec) {
    return switch (navSpec) {
      case RNAV_10, RNAV_5, RNAV_2, RNAV_1, B_RNAV, P_RNAV, VOR_DME_RNAV, RNAV_VISUAL, EITHER_RNP1_RNAV1, CONVENTIONAL_IN_RNAV_RNP -> RequiredNavigationEquipage.RNAV;
      case RNP_4, RNP_2, RNP_1, RNP_03, RNP_APCH, RNP_AR, ADVANCED_RNP -> RequiredNavigationEquipage.RNP;
      case NOT_SPECIFIED -> RequiredNavigationEquipage.UNKNOWN;
    };
  }

  @Override
  public Optional<RequiredNavigationEquipage> apply(ArincProcedureLeg leg) {
    return findIfPBN(leg) //see if qual 3 gives us a PBN spec
        .or(() -> findIfSidStarIsConventional(leg)) //sid star just have a value to check
        .or(() -> findIfApproachIsConventional(leg)); //approach we have to sort out which type and then group them as conventional
  }

  private Optional<RequiredNavigationEquipage> findIfApproachIsConventional(ArincProcedureLeg leg) {
    return Optional.of(leg)
        .map(ArincRouteType::from)
        .filter(CONV_TYPES::contains) //the old way still works for approaches because qual2 is different on approach vs sid/star
        .map(i -> RequiredNavigationEquipage.CONV);
  }
}
