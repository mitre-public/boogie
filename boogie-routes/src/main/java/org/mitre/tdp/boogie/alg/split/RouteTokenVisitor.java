package org.mitre.tdp.boogie.alg.split;

import java.util.Optional;

/**
 * Visitor implementation which can be used to generic collect information needed by other collaborator classes about the token
 * without requiring additional methods be added to the {@link RouteToken} interface that may not be needed for many format types.
 *
 * <p>Ideally additional functionality is added to the base token object as needed via visitors as opposed to adding onto the
 * tokens themselves (preventing them from becoming a bag of features, many of which may not be relevant for certain formats).
 *
 * <p>User-defined visitors can also be useful when inspecting the final expanded route to extract any information from the tokens
 * an application may want access to alongside the generated leg sequence.
 */
public interface RouteTokenVisitor {

  /**
   * Holdover for feature parity with the original route expander implementation - this will be removed in a future release.
   */
  @Deprecated
  static String wildcards(RouteToken token) {
    WildcardVisitor visitor = new WildcardVisitor();
    token.accept(visitor);
    return visitor.wildcards();
  }

  /**
   * Returns whether the infrastructure element referred to by this {@link RouteToken} was filed as a "direct to" in the route
   * string (implying different behavior should be considered when generating the leg to what this is referencing).
   *
   * @param token a {@link RouteToken} of some unknown format
   */
  static boolean isDirectTo(RouteToken token) {
    IsDirectTo visitor = new IsDirectTo();
    token.accept(visitor);
    return visitor.isDirectTo();
  }

  /**
   * Returns whether the infrastructure element referred to by this {@link RouteToken} was filed following a truncated portion of
   * the original flightplan (meaning it likely represents "start of route" as opposed to a "fly to this").
   *
   * @param token a {@link RouteToken} of some unknown format
   */
  static boolean isTailoredBefore(RouteToken token) {
    IsTailoredBefore visitor = new IsTailoredBefore();
    token.accept(visitor);
    return visitor.isTailoredBefore();
  }

  static Optional<String> extractEtaEet(RouteToken routeToken) {
    EtaEetVisitor visitor = new EtaEetVisitor();
    routeToken.accept(visitor);
    return Optional.ofNullable(visitor.etaEet);
  }

  void visit(RouteToken.Standard standard);

  void visit(RouteToken.FaaIfr faaIfr);

  void visit(RouteToken.Icao icao);

  final class IsDirectTo implements RouteTokenVisitor {

    private boolean isDirectTo = false;

    private IsDirectTo() {
    }

    @Override
    public void visit(RouteToken.Standard standard) {
    }

    @Override
    public void visit(RouteToken.FaaIfr faaIfr) {
      this.isDirectTo = faaIfr.wildcards().map(Wildcard.DIRECT::test).orElse(isDirectTo);
    }

    @Override
    public void visit(RouteToken.Icao icao) {
      this.isDirectTo = icao.wildcards().map(Wildcard.DIRECT::test).orElse(isDirectTo);
    }

    public boolean isDirectTo() {
      return isDirectTo;
    }
  }

  final class IsTailoredBefore implements RouteTokenVisitor {

    private boolean isTailoredBefore = false;

    private IsTailoredBefore() {
    }

    @Override
    public void visit(RouteToken.Standard standard) {
    }

    @Override
    public void visit(RouteToken.FaaIfr faaIfr) {
      this.isTailoredBefore = faaIfr.wildcards().map(Wildcard.TAILORED::test).orElse(isTailoredBefore);
    }

    @Override
    public void visit(RouteToken.Icao icao) {
      this.isTailoredBefore = icao.wildcards().map(Wildcard.TAILORED::test).orElse(isTailoredBefore);
    }

    public boolean isTailoredBefore() {
      return isTailoredBefore;
    }
  }

  final class WildcardVisitor implements RouteTokenVisitor {

    private String wildcards = "";

    private WildcardVisitor() {
    }

    public String wildcards() {
      return wildcards;
    }

    @Override
    public void visit(RouteToken.Standard standard) {
      // none
    }

    @Override
    public void visit(RouteToken.FaaIfr faaIfr) {
      this.wildcards = faaIfr.wildcards().orElse("");
    }

    @Override
    public void visit(RouteToken.Icao icao) {
      this.wildcards = icao.wildcards().orElse("");
    }
  }

  final class EtaEetVisitor implements RouteTokenVisitor {
    private String etaEet;
    private EtaEetVisitor() {}

    @Override
    public void visit(RouteToken.Standard standard) {
      etaEet = null;
    }

    @Override
    public void visit(RouteToken.FaaIfr faaIfr) {
      etaEet = faaIfr.etaEet().orElse(null);

    }

    @Override
    public void visit(RouteToken.Icao icao) {
      etaEet = icao.etaEet().orElse(null);
    }
  }



}
