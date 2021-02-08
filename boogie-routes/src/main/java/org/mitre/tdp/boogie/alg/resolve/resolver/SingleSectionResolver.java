package org.mitre.tdp.boogie.alg.resolve.resolver;

import java.util.List;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

/**
 * A {@link SingleSectionResolver} is an extension of a {@link SectionResolver} which doesn't rely on context from the previous
 * or next sections of the route string to perform resolution.
 */
@FunctionalInterface
public interface SingleSectionResolver extends SectionResolver {

  List<ResolvedElement<?>> resolve(SectionSplit sectionSplit);

  @Override
  default List<ResolvedElement<?>> resolve(@Nullable SectionSplit previous, SectionSplit current, @Nullable SectionSplit next) {
    return resolve(current);
  }
}
