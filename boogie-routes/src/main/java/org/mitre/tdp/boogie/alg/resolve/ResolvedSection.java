package org.mitre.tdp.boogie.alg.resolve;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
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
   * This is the superset of all potential elements the filed section of the route string
   * could possible be referring to.
   */
  private List<ResolvedElement<?>> elements;
  /**
   * Whether the element was flown to direct.
   */
  private boolean direct;

  public ResolvedSection(SectionSplit split) {
    this.sectionSplit = split;
    this.elements = new ArrayList<>();
    this.direct = false;
  }

  public SectionSplit sectionSplit() {
    return sectionSplit;
  }

  public boolean direct() {
    return direct;
  }

  ResolvedSection labelDirect() {
    this.direct = true;
    return this;
  }

  public List<ResolvedElement<?>> elements() {
    return elements;
  }

  ResolvedSection addElements(List<ResolvedElement<?>> eles) {
    this.elements = eles;
    Comparator<ResolvedElement> compSrc = Comparator.comparing(ele -> ele.reference().source());
    Comparator<ResolvedElement> compType = Comparator.comparing(ResolvedElement::type);
    this.elements.sort(compSrc.thenComparing(compType));
    return this;
  }

  public Set<LinkedLegs> allLegs() {
    return elements.stream()
        // tag the generated legs with the section information on the way out
        .flatMap(e -> e.legs().stream().peek(leg -> {
          leg.source().setSectionSplit(sectionSplit());
          leg.target().setSectionSplit(sectionSplit());
        }))
        .collect(Collectors.toSet());
  }
}
