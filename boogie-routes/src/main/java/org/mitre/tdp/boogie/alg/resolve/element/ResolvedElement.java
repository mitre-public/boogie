package org.mitre.tdp.boogie.alg.resolve.element;

import java.util.List;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.Infrastructure;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.alg.graph.LinkedLegs;

/**
 * Abstract representation of an object which was resolved from a {@link SectionSplit} of a route string.
 *
 * Extensions of this class maintain traceability through this classes fieldset to the the source object and override the
 * {@link #buildLegs()} method which is used to generate legs which will be fed into the final route expansion graph.
 */
public abstract class ResolvedElement<I extends Infrastructure> {
  final ElementType type;
  final I reference;

  List<LinkedLegs> legs;

  ResolvedElement(ElementType t, I ref) {
    this.type = t;
    this.reference = ref;
  }

  public ElementType type() {
    return type;
  }

  public I reference() {
    return reference;
  }

  protected abstract List<LinkedLegs> buildLegs();

  /**
   * Converts the internal template element into a collection of legs which
   * can be used in the section graph.
   */
  public List<LinkedLegs> legs() {
    if (legs == null) {
      this.legs = buildLegs().stream()
          .peek(leg -> {
            leg.source().setSourceElement(this);
            leg.target().setSourceElement(this);
          }).collect(Collectors.toList());
    }
    return legs;
  }
}
