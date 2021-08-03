package org.mitre.tdp.boogie.alg.resolve;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.mitre.tdp.boogie.util.Streams.triplesWithNulls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.alg.split.SectionSplitter;

/**
 * A {@link SectionResolver} exists to resolve infrastructure elements which are considered to be associated with an input
 * {@link SectionSplit} generated from a configured {@link SectionSplitter}.
 * <br>
 * Resolvers allow for the context of neighboring splits to be used in the resolution of the current split - though the previous
 * and next section may be null (if at the start or end of a route string).
 * <br>
 * The {@link SingleSplitSectionResolver} is provided as a sub-interface for sections of route strings which dont require the
 * context of a neighboring section to be resolved appropriately.
 */
@FunctionalInterface
public interface SectionResolver {

  /**
   * Attempts to resolve a list of candidate {@link ResolvedElement}s from the given current {@link SectionSplit} as well as
   * the preceding and following section splits.
   */
  ResolvedSection resolve(@Nullable SectionSplit previous, SectionSplit current, @Nullable SectionSplit next);

  default List<ResolvedSection> applyTo(List<SectionSplit> sectionSplits) {
    return triplesWithNulls(sectionSplits, this::resolve).collect(Collectors.toList());
  }

  /**
   * Composes the given {@link SectionResolver} with the provided {@link SectionResolver} to produce a new resolver which returns
   * the outputs of both calls to {@link SectionResolver#resolve(SectionSplit, SectionSplit, SectionSplit)} as a single list.
   */
  default SectionResolver compose(SectionResolver that) {
    checkNotNull(that, "Input resolver cannot be null.");
    return (p, c, n) -> {
      List<ResolvedElement> allElements = new ArrayList<>();

      ResolvedSection thisSection = this.resolve(p, c, n);
      ResolvedSection thatSection = that.resolve(p, c, n);

      checkArgument(thisSection.sectionSplit().equals(thatSection.sectionSplit()));
      allElements.addAll(thisSection.elements());
      allElements.addAll(thatSection.elements());

      return new ResolvedSection(thisSection.sectionSplit(), allElements);
    };
  }

  /**
   * Returns the composition of all provided {@link SectionResolver}s as a single SectionResolver.
   */
  static SectionResolver composeAll(SectionResolver... sectionResolvers) {
    checkArgument(sectionResolvers.length > 0, "Cannot provide zero resolvers to composeAll().");
    return Arrays.stream(sectionResolvers).reduce(SectionResolver::compose).orElseThrow(IllegalStateException::new);
  }
}
