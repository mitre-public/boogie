package org.mitre.tdp.boogie.alg.resolve;

import java.util.Objects;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

/**
 * This class represents a graph node as a {@link SectionSplit} as extracted from the top level route
 * string, a top level resolved infrastructure element associated with that split {@link ResolvedElement}
 * and a leg indicating both what the fix is as well as an indication of how to fly to it.
 *
 * <p>e.g. For airways the expanded legs are {@link PathTerm#TF} while for ..FIX.. elements the leg type is
 * indicated as {@link PathTerm#IF} while for procedures they are as declared in the procedure definition.
 */
public class GraphableLeg {
  /**
   * The {@link SectionSplit} from the route string used to generate this reference.
   */
  private SectionSplit split;
  /**
   * The generated leg.
   */
  private Leg leg;
  /**
   * A pointer to the {@link ResolvedElement} which generated the given leg for the top level split.
   */
  private ResolvedElement<?> sourceElement;

  public GraphableLeg(Leg leg) {
    this.leg = leg;
  }

  public SectionSplit split() {
    return split;
  }

  public GraphableLeg setSplit(SectionSplit split) {
    this.split = split;
    return this;
  }

  public Leg leg() {
    return leg;
  }

  public ResolvedElement<?> sourceElement() {
    return sourceElement;
  }

  public GraphableLeg setSourceElement(ResolvedElement<?> sourceElement) {
    this.sourceElement = sourceElement;
    return this;
  }

  @Override
  public int hashCode() {
    return Objects.hash(split, leg);
  }

  @Override
  public boolean equals(Object that) {
    if (that == null) {
      return false;
    }
    if (that instanceof GraphableLeg) {
      GraphableLeg ssl = (GraphableLeg) that;
      return Objects.equals(split, ssl.split())
          && Objects.equals(leg, ssl.leg());
    }
    return false;
  }
}
