package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Objects.requireNonNull;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

/**
 * This class represents a graph node as a {@link SectionSplit} as extracted from the top level route
 * string, a top level resolved infrastructure element associated with that split {@link ResolvedElement}
 * and a leg indicating both what the fix is as well as an indication of how to fly to it.
 *
 * <p>e.g. For airways the expanded legs are {@link PathTerminator#TF} while for ..FIX.. elements the leg type is
 * indicated as {@link PathTerminator#IF} while for procedures they are as declared in the procedure definition.
 */
public final class ResolvedLeg {
  /**
   * The {@link SectionSplit} from the route string used to generate this reference.
   */
  private final SectionSplit sectionSplit;
  /**
   * A pointer to the {@link ResolvedElement} which generated the given leg for the top level split.
   */
  private final ResolvedElement resolvedElement;
  /**
   * The actual resolved leg of the route.
   */
  private final Leg leg;

  public ResolvedLeg(SectionSplit sectionSplit, ResolvedElement resolvedElement, Leg leg) {
    this.sectionSplit = requireNonNull(sectionSplit);
    this.resolvedElement = requireNonNull(resolvedElement);
    this.leg = requireNonNull(leg);
  }

  public SectionSplit split() {
    return sectionSplit;
  }

  public Leg leg() {
    return leg;
  }

  public ResolvedElement sourceElement() {
    return resolvedElement;
  }
}
