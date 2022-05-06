package org.mitre.tdp.boogie.alg.resolve;

import java.util.Objects;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.alg.split.SectionSplit;

/**
 * This class is decorator for the SectionResolver used inside RouteExpander. Functionally it works the exact same way however,
 * it throws an exception when the SectionResolver doesn't find any matching infrastructure for a section of a route.
 */
public final class SurlySectionResolver implements SectionResolver {

  private final SectionResolver sectionResolver;

  public SurlySectionResolver(SectionResolver sectionResolver) {
    this.sectionResolver = Objects.requireNonNull(sectionResolver, "sectionResolver cannot be null");
  }

  @Override
  public ResolvedSection resolve(@Nullable SectionSplit previous, SectionSplit current, @Nullable SectionSplit next) {
    ResolvedSection resolvedSection = sectionResolver.resolve(previous, current, next);
    if (resolvedSection.elements().isEmpty()) {
      throw new IllegalStateException(missingSectionErrorMessage(current));
    }
    return resolvedSection;
  }

  private String missingSectionErrorMessage(SectionSplit current) {
    return "Section resolver could not find any matching infrastructure for " + current.value();
  }
}