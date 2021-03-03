package org.mitre.tdp.boogie.alg.graph;

import static org.mitre.tdp.boogie.utils.Nulls.nonNullEquals;

import org.mitre.tdp.boogie.alg.resolve.GraphableLeg;

public final class LinkedLegs {
  /**
   * This is the default weight we use when a linked pair of legs come from the same reference section.
   * <p>
   * A non-zero weight is used to keep things deterministic.
   */
  public static final double MATCH_WEIGHT = 0.00001;

  private final GraphableLeg source;
  private final GraphableLeg target;

  public LinkedLegs(GraphableLeg s, GraphableLeg t) {
    this.source = s;
    this.target = t;
  }

  public GraphableLeg source() {
    return source;
  }

  public GraphableLeg target() {
    return target;
  }

  public Double linkWeight() {
    return nonNullEquals(source.split(), target.split())
        || source.leg().pathTerminator() == null
        || target.leg().pathTerminator() == null
        ? MATCH_WEIGHT
        : source.leg().pathTerminator().latLong().distanceInNM(target.leg().pathTerminator().latLong());
  }
}
