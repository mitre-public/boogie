package org.mitre.tdp.boogie.alg.resolve.element;

import java.util.List;

import org.mitre.tdp.boogie.Infrastructure;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.models.LinkedLegs;

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
      this.legs = buildLegs();
    }
    return legs;
  }
}
