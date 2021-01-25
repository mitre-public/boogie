package org.mitre.tdp.boogie.alg.resolve.resolver;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

@FunctionalInterface
public interface SectionResolver {

  /**
   * Attempts to resolve a list of candidate {@link ResolvedElement}s from the given current {@link SectionSplit} as well as
   * the preceding and following section splits.
   */
  List<ResolvedElement<?>> resolve(@Nullable SectionSplit previous, SectionSplit current, @Nullable SectionSplit next);

  /**
   * Composes the given {@link SectionResolver} with the provided {@link SectionResolver} to produce a new resolver which
   * returns the outputs of both's calls to {@link SectionResolver#resolve(SectionSplit, SectionSplit, SectionSplit)} as
   * a single list.
   */
  default SectionResolver compose(SectionResolver resolver) {
    checkNotNull(resolver, "Input resolver cannot be null.");
    return (p, c, n) -> {
      List<ResolvedElement<?>> resolved = new ArrayList<>();
      resolved.addAll(this.resolve(p, c, n));
      resolved.addAll(resolver.resolve(p, c, n));
      return resolved;
    };
  }
}
