package org.mitre.tdp.boogie.alg.resolve;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.models.LinkedLegs;

/**
 * Wrapper class for a section containing context flags from the filed route string itself
 * (e.g. direct/suppressed) as well as a collection of possible infrastructure elements that
 * were determined to be associated with the section id via the {@link SectionResolver}.
 */
public class ResolvedSection {

  /**
   * The split section with wildcards and etc associated with this collection of route
   * elements.
   */
  private SectionSplit sectionSplit;
  /**
   * List of resolved infrastructure elements.
   *
   * <p>This is the superset of all potential elements the filed section of the route string
   * could possible be referring to.
   */
  private List<ResolvedElement<?>> elements;

  public ResolvedSection(SectionSplit split) {
    this.sectionSplit = split;
    this.elements = new ArrayList<>();
  }

  public SectionSplit sectionSplit() {
    return sectionSplit;
  }

  public ResolvedSection setSectionSplit(SectionSplit split) {
    this.sectionSplit = split;
    return this;
  }

  public List<ResolvedElement<?>> elements() {
    return elements;
  }

  public ResolvedSection setElements(List<ResolvedElement<?>> eles) {
    this.elements = eles;
    Comparator<ResolvedElement> compSrc = Comparator.comparing(ele -> ele.reference().source());
    Comparator<ResolvedElement> compType = Comparator.comparing(ResolvedElement::type);
    this.elements.sort(compSrc.thenComparing(compType));
    return this;
  }

  public List<LinkedLegs> allLegs() {
    return elements.stream()
        // tag the generated legs with the section information on the way out
        .flatMap(e -> e.legs().stream().peek(leg -> {
          leg.source().setSplit(sectionSplit());
          leg.target().setSplit(sectionSplit());
        }))
        .collect(Collectors.toList());
  }
}
