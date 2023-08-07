package org.mitre.tdp.boogie.arinc.assemble;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Comparator.comparing;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PD_0;
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
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PE_7;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PE_9;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PE_F;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PE_M;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PE_N;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PE_P;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PE_R;
import static org.mitre.tdp.boogie.arinc.assemble.ArincRouteType.PE_S;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;

/**
 * Classifier function for mapping a collection of {@link ArincProcedureLeg}s known to be part of the same transition to a high
 * level {@link TransitionType}.
 * <br>
 * Note that despite our best efforts upstream of handling ARINC schema versions differently this is a bit of a hodge-podge of
 * rules based on both the V18/19a ARINC versions. Thankfully V19a is primarily <i>extending</i> the V18 classifiers with a bit
 * more specificity so this should remain adequate.
 * <br>
 * Note since by default the missed approach portion of the approach procedure <i>isn't</i> split off from the final (i.e. it's
 * generally coded as ...->F1(CF/TF)->F2(CF/TF)->Runway(CF/TF)->CA->VM->DF->HM - where the CA leg after the runway is the start of
 * the MA) this class relies on the upstream partitioning of {@link ArincProcedureLeg}s by transition to be sensitive to the start
 * of the MA and split out that grouping of legs (hence hiding this class as package-private).
 */
final class ArincTransitionTypeClassifier implements Function<List<ArincProcedureLeg>, TransitionType> {

  private static final SidStarClassifier sidStarClassifier = new SidStarClassifier();

  private static final ApproachClassifier approachClassifier = new ApproachClassifier();

  @Override
  public TransitionType apply(List<ArincProcedureLeg> arincProcedureLegs) {

    List<ArincProcedureLeg> sorted = arincProcedureLegs.stream()
        .sorted(comparing(ArincProcedureLeg::sequenceNumber))
        .collect(Collectors.toList());

    if ("F".equals(sorted.get(0).subSectionCode().orElseThrow(IllegalStateException::new))) {
      return approachClassifier.apply(sorted);
    } else {
      return sidStarClassifier.apply(sorted);
    }
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

      ArincRouteType arincRouteType = ArincRouteType.from(representative);

      if (LABELED_COMMON.contains(arincRouteType)) {
        return TransitionType.COMMON;
      } else if (LABELED_ENROUTE.contains(arincRouteType)) {
        return TransitionType.ENROUTE;
      } else if (LABELED_RUNWAY.contains(arincRouteType)) {
        return TransitionType.RUNWAY;
      } else {
        throw new IllegalArgumentException("Unknown subSection/routeType combination: ".concat(arincRouteType.name()));
      }
    }

    /**
     * The set of concatenated {subSection, routeType} qualifiers indicating a transition is {@link TransitionType#COMMON}.
     */
    private static final Set<ArincRouteType> LABELED_COMMON = newHashSet(PD_2, PD_5, PD_M, PD_0, PE_2, PE_5, PE_M, PD_N, PE_N);

    /**
     * The set of concatenated {subSection, routeType} qualifiers indicating a transition is {@link TransitionType#ENROUTE}.
     */
    private static final Set<ArincRouteType> LABELED_ENROUTE = newHashSet(PD_3, PD_6, PD_S, PD_V, PE_1, PE_4, PE_7, PE_F, PD_P, PE_R);

    /**
     * The set of concatenated {subSection, routeType} qualifiers indicating a transition is {@link TransitionType#RUNWAY}.
     */
    private static final Set<ArincRouteType> LABELED_RUNWAY = newHashSet(PD_1, PD_4, PD_F, PD_T, PE_3, PE_6, PE_9, PE_S, PD_R, PE_P);
  }

  /**
   * Function for labeling a collection of {@link ArincProcedureLeg} records from an APPROACH by {@link TransitionType}.
   * <br>
   * This class uses naming conventions to label transitions as the COMMON portion of a procedure and it uses fix description info
   * in the first leg of the transition to make a decision about whether the transition is the "Missed Approach" part of the final
   * approach transition.
   * <br>
   * If a decision can't be made based on contextual information it classifies the procedure as an APPROACH transition.
   */
  private static final class ApproachClassifier implements Function<List<ArincProcedureLeg>, TransitionType> {

    @Override
    public TransitionType apply(List<ArincProcedureLeg> arincProcedureLegs) {
      checkArgument("F".equals(arincProcedureLegs.get(0).subSectionCode().orElseThrow(IllegalStateException::new)), "Legs must be from an APPROACH procedure.");

      ArincProcedureLeg firstLeg = arincProcedureLegs.get(0);
      String adjustedTransitionName = StandardizedTransitionName.INSTANCE.apply(firstLeg.transitionIdentifier().orElse(null));

      if (IsFirstLegOfMissedApproach.INSTANCE.test(firstLeg)) {
        return TransitionType.MISSED;
      } else if ("ALL".equals(adjustedTransitionName)) {
        return TransitionType.COMMON;
      } else {
        return TransitionType.APPROACH;
      }
    }
  }
}
