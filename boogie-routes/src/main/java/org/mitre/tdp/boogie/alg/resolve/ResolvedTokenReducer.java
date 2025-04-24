package org.mitre.tdp.boogie.alg.resolve;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.BiFunction;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.TransitionType;

/**
 * This class takes pairs or resolved tokens and collapses them in rare cases where transition structure results in
 * the wrong token being used by the route choosers down stream.
 * <p>
 * This happens when there are two tokens with the same name, but a transition masked procedure has one with transitions.
 */
public final class ResolvedTokenReducer implements BiFunction<ResolvedTokens, ResolvedTokens, ResolvedTokens>, Serializable {
  public static final ResolvedTokenReducer INSTANCE = new ResolvedTokenReducer();

  /**
   * If the first token is a star with no transitions, then if matches with the inferred section ... don't keep this
   */
  private static final BiFunction<ResolvedTokens, ResolvedTokens, Optional<ResolvedTokens>> POSSIBLY_KEEP_STAR = (first, second) -> {
    Optional<Procedure> resolvedStarNoTransitions = first.resolvedTokens().stream()
        .map(ResolvedTokenVisitor::star)
        .flatMap(Optional::stream)
        .filter(star -> star.transitions().isEmpty())
        .findFirst();

    Optional<Procedure> matchingInferredStar = resolvedStarNoTransitions.flatMap(r -> second.resolvedTokens().stream()
        .map(ResolvedTokenVisitor::star)
        .flatMap(Optional::stream)
        .filter(star -> star.transitions().stream().anyMatch(i -> TransitionType.RUNWAY.equals(i.transitionType())))
        .findFirst()
        .filter(i -> i.procedureIdentifier().equals(r.procedureIdentifier()))
    );

    if (matchingInferredStar.isPresent()) {
      return Optional.empty();
    }

    return Optional.of(first);
  };

  private static final BiFunction<ResolvedTokens, ResolvedTokens, Optional<ResolvedTokens>> POSSIBLY_KEEP_SID = (first, second) -> {
    Optional<Procedure> resolvedSidNoTransitions = second.resolvedTokens().stream()
        .map(ResolvedTokenVisitor::sid)
        .flatMap(Optional::stream)
        .filter(star -> star.transitions().isEmpty())
        .findFirst();

    Optional<Procedure> matchingInferredSid = resolvedSidNoTransitions.flatMap(r -> first.resolvedTokens().stream()
        .map(ResolvedTokenVisitor::sid)
        .flatMap(Optional::stream)
        .filter(star -> star.transitions().stream().anyMatch(i -> TransitionType.RUNWAY.equals(i.transitionType())))
        .findFirst()
        .filter(i -> i.procedureIdentifier().equals(r.procedureIdentifier()))
    );

    if (matchingInferredSid.isPresent()) {
      return Optional.empty();
    }

    return Optional.of(second);
  };

  /**
   * This means that we only have one resolved token or the infrastructure names don't overlap so no worries with this pairing
   */
  private static final BiFunction<ResolvedTokens, ResolvedTokens, Optional<ResolvedTokens>> KEEP_WHEN_NO_OVERLAP = (f, s) -> Optional.of(f)
      .filter(first -> f.resolvedTokens().size() <= 1 || !f.routeToken().infrastructureName().equals(s.routeToken().infrastructureName()));


  private ResolvedTokenReducer(){}

  @Override
  public ResolvedTokens apply(ResolvedTokens f, ResolvedTokens s) {
    return KEEP_WHEN_NO_OVERLAP.apply(f, s)
        .or(() -> POSSIBLY_KEEP_STAR.apply(f, s))
        .or(() -> POSSIBLY_KEEP_SID.apply(f, s))
        .orElse(null);
  }
}
