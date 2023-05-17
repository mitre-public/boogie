package org.mitre.tdp.boogie.alg.split;

public interface RouteTokenVisitor {

  static String wildcards(RouteToken token) {
    WildcardVisitor visitor = new WildcardVisitor();
    token.accept(visitor);
    return visitor.wildcards();
  }

  void visit(RouteToken.Standard standard);

  void visit(RouteToken.FaaIfr faaIfr);

  void visit(RouteToken.Icao icao);

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

    }
  }
}
