package org.mitre.boogie.xml.v23_4.convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.boogie.xml.model.ArincTransition;
import org.mitre.boogie.xml.v23_4.generated.Approach;
import org.mitre.boogie.xml.v23_4.generated.Procedure;
import org.mitre.boogie.xml.v23_4.generated.ProcedureRoute;
import org.mitre.boogie.xml.v23_4.generated.Sid;
import org.mitre.boogie.xml.v23_4.generated.SidStar;
import org.mitre.boogie.xml.v23_4.generated.Star;

/**
 * Extracts subtype-specific fields from a concrete {@link Procedure} into a {@link ProcedureSubtypeFields} record.
 *
 * <p>Dispatches on the three concrete types ({@link Sid}, {@link Star}, {@link Approach}) and collects
 * their child transitions via {@link ArincTransitionConverter}.
 */
final class ProcedureSubtypeFieldsConverter {

  private static final ArincTransitionConverter TRANSITION_CONVERTER = ArincTransitionConverter.INSTANCE;

  private ProcedureSubtypeFieldsConverter() {
  }

  static ProcedureSubtypeFields convert(Procedure procedure) {
    if (procedure instanceof Sid sid) {
      return applySidStarFields(sid)
          .isEngineOut(sid.isIsEngineOut())
          .isVector(sid.isIsVector())
          .isPInS(sid.isIsPInS())
          .isPInSProceedVisually(sid.isIsPInSProceedVisually())
          .isPInSProceedVfr(sid.isIsPInSProceedVfr())
          .transitions(convertSidTransitions(sid))
          .build();
    } else if (procedure instanceof Star star) {
      return applySidStarFields(star)
          .isContinuousDescent(star.isIsContinuousDescent())
          .transitions(convertStarTransitions(star))
          .build();
    } else if (procedure instanceof Approach approach) {
      return ProcedureSubtypeFields.builder()
          .approachRouteType(Optional.ofNullable(approach.getApproachRouteType()).map(Enum::name).orElse(null))
          .gnssFmsIndicator(Optional.ofNullable(approach.getGnssFmsIndicator()).map(Enum::name).orElse(null))
          .approachPointRef(Optional.ofNullable(approach.getApproachPointRef()).map(Object::toString).orElse(null))
          .categoryARadius(approach.getCategoryARadius())
          .categoryBRadius(approach.getCategoryBRadius())
          .categoryCRadius(approach.getCategoryCRadius())
          .categoryDRadius(approach.getCategoryDRadius())
          .isRnavVisual(approach.isIsRnavVisual())
          .isPInSProceedVisually(approach.isIsPInSProceedVisually())
          .isPInSProceedVfr(approach.isIsPInSProceedVfr())
          .isLocalizerBackcourse(approach.isIsLocalizerBackcourse())
          .isPreferredProcedure(approach.isIsPreferredProcedure())
          .transitions(convertApproachTransitions(approach))
          .build();
    }
    return ProcedureSubtypeFields.builder().build();
  }

  private static ProcedureSubtypeFields.Builder applySidStarFields(SidStar sidStar) {
    return ProcedureSubtypeFields.builder()
        .isVorDmeRnav(sidStar.isIsVorDmeRnav())
        .rnavPbnNavSpec(Optional.ofNullable(sidStar.getRnavPbnNavSpec()).map(Enum::name).orElse(null))
        .rnpPbnNavSpec(Optional.ofNullable(sidStar.getRnpPbnNavSpec()).map(Enum::name).orElse(null));
  }

  private static List<ArincTransition> convertSidTransitions(Sid sid) {
    List<ProcedureRoute> routes = new ArrayList<>();
    routes.addAll(sid.getSidRunwayTransition());
    if (sid.getSidCommonRoute() != null) {
      routes.add(sid.getSidCommonRoute());
    }
    routes.addAll(sid.getSidEnrouteTransition());
    return convertRoutes(routes);
  }

  private static List<ArincTransition> convertStarTransitions(Star star) {
    List<ProcedureRoute> routes = new ArrayList<>();
    routes.addAll(star.getStarEnrouteTransition());
    if (star.getStarCommonRoute() != null) {
      routes.add(star.getStarCommonRoute());
    }
    routes.addAll(star.getStarRunwayTransition());
    return convertRoutes(routes);
  }

  private static List<ArincTransition> convertApproachTransitions(Approach approach) {
    Stream<ProcedureRoute> transitions = approach.getApproachTransition().stream().map(ProcedureRoute.class::cast);
    Stream<ProcedureRoute> finalApproach = Stream.ofNullable(approach.getFinalApproach());
    Stream<ProcedureRoute> missed = approach.getMissedApproach().stream().map(ProcedureRoute.class::cast);
    return Stream.of(transitions, finalApproach, missed)
        .flatMap(Function.identity())
        .map(TRANSITION_CONVERTER)
        .flatMap(Optional::stream)
        .collect(Collectors.toList());
  }

  private static List<ArincTransition> convertRoutes(List<ProcedureRoute> routes) {
    return routes.stream()
        .map(TRANSITION_CONVERTER)
        .flatMap(Optional::stream)
        .collect(Collectors.toList());
  }
}
