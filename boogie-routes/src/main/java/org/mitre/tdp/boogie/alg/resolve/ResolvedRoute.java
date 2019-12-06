package org.mitre.tdp.boogie.alg.resolve;

import java.util.List;

/**
 * A collection of {@link ResolvedSection}s tied to all of the splits in the filed route string.
 */
public class ResolvedRoute {

  private final List<ResolvedSection> sections;

  public ResolvedRoute(List<ResolvedSection> sections) {
    this.sections = sections;
  }

  public List<ResolvedSection> sections() {
    return sections;
  }
}
