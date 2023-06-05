package org.mitre.tdp.boogie.alg;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Comparator.comparingDouble;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.mitre.caasd.commons.collect.HashedLinkedSequence.newHashedLinkedSequence;

import java.util.List;
import java.util.function.UnaryOperator;

import org.mitre.caasd.commons.collect.HashedLinkedSequence;
import org.mitre.tdp.boogie.alg.chooser.RouteChooser;
import org.mitre.tdp.boogie.alg.chooser.graph.LinkingStrategy;
import org.mitre.tdp.boogie.alg.chooser.graph.TokenGrapher;
import org.mitre.tdp.boogie.alg.chooser.graph.TokenMapper;
import org.mitre.tdp.boogie.alg.facade.FluentRouteExpander;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokens;
import org.mitre.tdp.boogie.alg.resolve.RouteTokenResolver;
import org.mitre.tdp.boogie.alg.resolve.infer.SectionInferrer;
import org.mitre.tdp.boogie.alg.split.RouteToken;
import org.mitre.tdp.boogie.alg.split.RouteTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Core interface for the functional definition of a route expander in Boogie.
 *
 * <p>This interface is provided as a facade over the three core abstractions provided by the library for route expansion:
 * <ol>
 *   <li>{@link RouteTokenizer} - for converting a route string to its constituent elements</li>
 *   <li>{@link RouteTokenResolver} - for associating those elements with physical infrastructure</li>
 *   <li>{@link RouteChooser} - for deciding the appropriate path through that infrastructure given multiplicity</li>
 * </ol>
 *
 * <p>It's implemented as an interface to allow clients to more easily decorate/mock/etc. versions of it in their own applications.
 *
 * <p>The {@link #standard()} implementation is a default version which works well across many contexts. Most applications though
 * will leverage this interface through the more data-driven facade: {@link FluentRouteExpander}.
 */
@FunctionalInterface
public interface RouteExpander {

  /**
   * Returns a new route expander which performs no operation on the incoming route string and context, returning an empty list
   * and is suitable for basic mocked testing, etc.
   */
  static RouteExpander noop() {
    return (route, context) -> List.of();
  }

  /**
   * Standard implementation of an expander which can be configured with different variations of the typical tokenizer, resolver,
   * and chooser implementations.
   *
   * @return a builder for the standard route expander implementation, as the expander requires some basic configuration
   */
  static Standard.Builder standard() {
    return new Standard.Builder();
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
   * @param context contextual outside the normal route string used to inform/configure the expansion, typically used via the data
   *                contract version {@link RouteContext#standard()}.
   */
  List<ResolvedLeg> expand(String route, RouteContext context);

  final class Standard implements RouteExpander {

    private static final Logger LOG = LoggerFactory.getLogger(Standard.class);

    private final RouteTokenizer routeTokenizer;

    private final RouteTokenResolver routeTokenResolver;

    private final RouteChooser routeChooser;

    private Standard(Builder builder) {
      this.routeTokenizer = requireNonNull(builder.routeTokenizer);
      this.routeTokenResolver = requireNonNull(builder.routeTokenResolver);
      this.routeChooser = requireNonNull(builder.routeChooser);
    }

    @Override
    public List<ResolvedLeg> expand(String route, RouteContext context) {

      checkArgument(route != null && !route.isEmpty(), "Route cannot be null or empty.");

      List<RouteToken> sectionSplits = routeTokenizer.tokenize(route);
      LOG.info("Generated {} SectionSplits from route {}.", sectionSplits.size(), route);

      HashedLinkedSequence<ResolvedTokens> initial = newHashedLinkedSequence(routeTokenResolver.applyTo(sectionSplits));
      LOG.info("Resolved {} Elements across {} Sections.", initial.stream().mapToInt(section -> section.resolvedTokens().size()).sum(), initial.size());

      for (SectionInferrer inferrer : context.inferrers()) {
        appendInferredSections(initial, inferrer);
      }

      LOG.info("Resolved {} Elements across {} Sections.", initial.stream().mapToInt(section -> section.resolvedTokens().size()).sum(), initial.size());

      List<ResolvedTokens> sortedByIndex = initial.stream()
          .sorted(comparingDouble(r -> r.routeToken().index()))
          .collect(toList());

      return routeChooser.chooseRoute(sortedByIndex);
    }

    private void appendInferredSections(HashedLinkedSequence<ResolvedTokens> sequence, SectionInferrer inferrer) {
      inferrer.inferAcross(sequence).forEach((start, inferred) -> {
        ResolvedTokens previous = start;

        for (ResolvedTokens section : inferred) {
          sequence.insertAfter(section, previous);
          previous = section;
        }
      });
    }

    public static final class Builder {

      private RouteTokenizer routeTokenizer = RouteTokenizer.faaIfrFormat();

      private RouteTokenResolver routeTokenResolver = RouteTokenResolver.noop();

      private RouteChooser routeChooser = RouteChooser.graphical(TokenMapper.standard());

      private Builder() {
      }

      /**
       * Methodology for tokenizing the input route string such that each token can be resolved with a section resolver.
       *
       * <p>The pre-defined set of implementations live in the {@link RouteTokenizer} interface.
       *
       * <p>Default: {@link RouteTokenizer#faaIfrFormat()}
       */
      public Builder routeTokenizer(RouteTokenizer routeTokenizer) {
        this.routeTokenizer = requireNonNull(routeTokenizer);
        return this;
      }

      /**
       * The section resolver implementation to use when resolving tokenized sections of the input route string to infrastructure
       * elements.
       *
       * <p>There is no default value for this field as a variety of infrastructure lookup services need to be provided to power the
       * section resolver.
       *
       * @param routeTokenResolver the token resolver implementation to use, default
       *                           {@link RouteTokenResolver#standard(LookupService, LookupService, LookupService, LookupService)}
       */
      public Builder routeTokenResolver(RouteTokenResolver routeTokenResolver) {
        this.routeTokenResolver = requireNonNull(routeTokenResolver);
        return this;
      }

      /**
       * Allows for the customization of an already-configured section resolver implementation (will throw an exception if one isn't
       * already present). This is provided mainly to allow simple decoration of a default-configured resolver (e.g. make it surly).
       *
       * @param routeTokenResolverConfigurer transform operator to decorate an already-configured resolver in additional functionality
       */
      public Builder routeTokenResolver(UnaryOperator<RouteTokenResolver> routeTokenResolverConfigurer) {
        requireNonNull(routeTokenResolver, "There should already be a SectionResolver configured we want to transform.");
        requireNonNull(routeTokenResolverConfigurer, "The configuration function should be non-null.");

        this.routeTokenResolver = routeTokenResolverConfigurer.apply(routeTokenResolver);
        return this;
      }

      /**
       * Defines how the expander chooses the appropriate flyable route through the resolved infrastructure candidates across all
       * tokens in the route string.
       *
       * @param routeChooser the route chooser implementation to use, default {@link RouteChooser#graphical(TokenGrapher, LinkingStrategy)}
       *                     + {@link LinkingStrategy#standard(TokenGrapher)}
       */
      public Builder routeChooser(RouteChooser routeChooser) {
        this.routeChooser = requireNonNull(routeChooser);
        return this;
      }

      public Standard build() {
        return new Standard(this);
      }
    }
  }
}
