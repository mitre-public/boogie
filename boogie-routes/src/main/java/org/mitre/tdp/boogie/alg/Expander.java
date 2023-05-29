package org.mitre.tdp.boogie.alg;

import java.util.List;

import org.mitre.tdp.boogie.alg.chooser.RouteChooser;
import org.mitre.tdp.boogie.alg.chooser.graph.LinkingStrategy;
import org.mitre.tdp.boogie.alg.chooser.graph.TokenGrapher;
import org.mitre.tdp.boogie.alg.resolve.ResolvedLeg;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.resolve.RouteTokenResolver;
import org.mitre.tdp.boogie.alg.split.RouteToken;
import org.mitre.tdp.boogie.alg.split.RouteTokenizer;

@FunctionalInterface
public interface Expander {

  /**
   * Standard implementation of an expander which can be configured with different variations of the typical tokenizer, resolver,
   * and chooser implementations.
   */
  static Standard standard() {
    return new Standard();
  }

  /**
   * Converts an input route string and any additional {@link RouteContext} which may be relevant to the expansion into a sequence
   * of {@link ResolvedLeg}s representing the route the aircraft flew.
   *
   * <p>The resultant resolved legs provide hooks to access the {@link RouteToken} and {@link ResolvedToken} which were the source
   * of the returned leg in the expansion and provide context which can be used by clients to convert this result to their own
   * business-format.
   *
   * @param route   the string route of flight
   * @param context contextual outside the normal route string used to inform/configure the expansion
   */
  List<ResolvedLeg> expand(String route, RouteContext context);

  final class Standard {

    private RouteTokenizer tokenizer = RouteTokenizer.faaIfrFormat();

    private RouteTokenResolver resolver = RouteTokenResolver.noop();

    private RouteChooser chooser = RouteChooser.graphical(TokenGrapher.standard(), LinkingStrategy.standard(TokenGrapher.standard()));

    private Standard() {
    }

    public Standard routeTokenizer(RouteTokenizer routeTokenizer) {
      return this;
    }

    public Standard routeTokenResolver(RouteTokenResolver routeTokenResolver) {
      return this;
    }

    public Standard routeChooser(RouteChooser routeChooser) {
      return this;
    }


  }
}
