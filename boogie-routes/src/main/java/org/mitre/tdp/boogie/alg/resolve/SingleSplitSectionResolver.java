package org.mitre.tdp.boogie.alg.resolve;

import java.util.List;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.alg.split.SectionSplit;

/**
 * A {@link SingleSplitSectionResolver} is an extension of a {@link SectionResolver} which doesn't rely on context from the previous
 * or next sections of the route string to perform resolution.
 */
@FunctionalInterface
public interface SingleSplitSectionResolver extends SectionResolver {

  List<ResolvedElement> resolve(SectionSplit sectionSplit);

  @Override
  default ResolvedSection resolve(@Nullable SectionSplit previous, SectionSplit current, @Nullable SectionSplit next) {
    return new ResolvedSection(current, resolve(current));
  }
}
