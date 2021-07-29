package org.mitre.tdp.boogie.arinc.assemble;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Comparator.comparing;
import static org.mitre.tdp.boogie.util.Preconditions.allMatch;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;

/**
 * Classifier function for mapping a collection of {@link ArincProcedureLeg}s known
 * <br>
 * Note that despite our best efforts upstream of handling ARINC schema versions differently this is a bit of a hodge-podge of
 * rules based on both the V18/19a ARINC versions. Thankfully V19a is primarily <i>extending</i> the V18 classifiers with a bit
 * more specificity so this should remain adequate.
 */
public final class ArincTransitionTypeClassifier implements Function<List<ArincProcedureLeg>, TransitionType> {

  private static final SidStarClassifier sidStarClassifier = new SidStarClassifier();

  private static final ApproachClassifier approachClassifier = new ApproachClassifier();

  @Override
  public TransitionType apply(List<ArincProcedureLeg> arincProcedureLegs) {
    checkArgument(allFromSameTransition(arincProcedureLegs), "Legs must be from the same logical transition.");

    List<ArincProcedureLeg> sorted = arincProcedureLegs.stream()
        .sorted(comparing(ArincProcedureLeg::sequenceNumber))
        .collect(Collectors.toList());

    if ("F".equals(sorted.get(0).subSectionCode())) {
      return approachClassifier.apply(sorted);
    } else {
      return sidStarClassifier.apply(sorted);
    }
  }

  private boolean allFromSameTransition(List<ArincProcedureLeg> arincProcedureLegs) {
    return allMatch(arincProcedureLegs, ArincProcedureLeg::sidStarIdentifier)
        && allMatch(arincProcedureLegs, ArincProcedureLeg::airportIdentifier)
        && allMatch(arincProcedureLegs, ArincProcedureLeg::airportIcaoRegion)
        && allMatch(arincProcedureLegs, leg -> adjustedTransitionName(leg.transitionIdentifier().orElse(null)))
        && allMatch(arincProcedureLegs, ArincProcedureLeg::subSectionCode);
  }

  private static String adjustedTransitionName(@Nullable String transitionName) {
    return Optional.ofNullable(transitionName).map(String::trim).filter(s -> !s.isEmpty()).orElse("ALL");
  }

  /**
   * Function for labeling a collection of {@link ArincProcedureLeg} records from a SI D/STAR by {@link TransitionType}.
   * <br>
   * The route type field + the subSection code contain the explicit breakdown of COMMON/ENROUTE/RUNWAY transitions in the ARINC
   * specification for SID/STAR records - approaches are another matter.
   */
  private static final class SidStarClassifier implements Function<List<ArincProcedureLeg>, TransitionType> {

    @Override
    public TransitionType apply(List<ArincProcedureLeg> arincProcedureLegs) {
      ArincProcedureLeg representative = arincProcedureLegs.get(0);

      String subSectionRouteType = representative.subSectionCode().concat(representative.routeType());

      if (LABELED_COMMON.contains(subSectionRouteType)) {
        return TransitionType.COMMON;
      } else if (LABELED_ENROUTE.contains(subSectionRouteType)) {
        return TransitionType.ENROUTE;
      } else if (LABELED_RUNWAY.contains(subSectionRouteType)) {
        return TransitionType.RUNWAY;
      } else {
        throw new IllegalArgumentException("Unknown subSection/routeType combination: ".concat(subSectionRouteType));
      }
    }

    /**
     * The set of concatenated {subSection, routeType} qualifiers indicating a transition is {@link TransitionType#COMMON}.
     */
    private static final Set<String> LABELED_COMMON = newHashSet("D2", "D5", "DM", "DN", "E2", "E5", "EM", "EN");

    /**
     * The set of concatenated {subSection, routeType} qualifiers indicating a transition is {@link TransitionType#ENROUTE}.
     */
    private static final Set<String> LABELED_ENROUTE = newHashSet("D3", "D6", "DS", "DV", "DP", "E1", "E4", "E7", "EF", "ER");

    /**
     * The set of concatenated {subSection, routeType} qualifiers indicating a transition is {@link TransitionType#RUNWAY}.
     */
    private static final Set<String> LABELED_RUNWAY = newHashSet("D1", "D4", "DF", "DT", "DR", "E3", "E6", "E9", "ES", "EP");
  }

  /**
   * Function for labeling a collection of {@link ArincProcedureLeg} records from an APPROACH by {@link TransitionType}.
   */
  private static final class ApproachClassifier implements Function<List<ArincProcedureLeg>, TransitionType> {

    @Override
    public TransitionType apply(List<ArincProcedureLeg> arincProcedureLegs) {
      checkArgument("F".equals(arincProcedureLegs.get(0).subSectionCode()), "Legs must be from an APPROACH procedure.");

      ArincProcedureLeg firstLeg = arincProcedureLegs.get(0);
      String adjustedTransitionName = adjustedTransitionName(firstLeg.transitionIdentifier().orElse(null));

      if (firstLegIndicatesMissedApproach(firstLeg)) {
        return TransitionType.MISSED;
      } else if ("ALL".equals(adjustedTransitionName)) {
        return TransitionType.COMMON;
      } else {
        return TransitionType.APPROACH;
      }
    }

    /**
     * Per the 424 specification a 'M' in the second character of the fix description of the first leg of a transition indicates
     * a {@link TransitionType#MISSED} transition.
     */
    private boolean firstLegIndicatesMissedApproach(ArincProcedureLeg arincProcedureLeg) {
      return arincProcedureLeg.waypointDescription().filter(description -> description.charAt(2) == 'M').isPresent();
    }
  }
}
