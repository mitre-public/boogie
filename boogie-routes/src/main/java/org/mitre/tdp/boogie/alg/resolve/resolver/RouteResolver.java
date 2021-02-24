package org.mitre.tdp.boogie.alg.resolve.resolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.mitre.tdp.boogie.alg.graph.RouteLegGraph;
import org.mitre.tdp.boogie.alg.resolve.ResolvedRoute;
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;
import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.models.ExpandedRoute;
import org.mitre.tdp.boogie.util.Iterators;

/**
 * Wrapper class for applying a composite {@link SectionResolver} to a sequence of {@link SectionSplit}s generating a new
 * {@link ResolvedRoute} which can be fed into a {@link RouteLegGraph} to resolved an {@link ExpandedRoute}.
 */
public final class RouteResolver implements Function<List<SectionSplit>, ResolvedRoute> {

  private final SectionResolver sectionResolver;

  public RouteResolver(SectionResolver... sectionResolvers) {
    this.sectionResolver = Arrays.stream(sectionResolvers)
        .reduce((p, c, n) -> new ArrayList<>(), SectionResolver::compose);
  }

  @Override
  public ResolvedRoute apply(List<SectionSplit> splits) {
    List<ResolvedSection> sections = new ArrayList<>();

    Iterators.triples2(splits, (p, c, n) -> {
      List<ResolvedElement<?>> elements = sectionResolver.resolve(p.orElse(null), c, n.orElse(null));
      sections.add(new ResolvedSection(c).setElements(elements));
    });

    return new ResolvedRoute(sections);
  }
}
