package org.mitre.tdp.boogie;

/**
 * Boundary geometry used by {@link AirspaceSequence}. Except for circles, the geometry applies from the sequence's
 * associated fix to the next sequence's associated fix, with the final sequence returning to the first.
 */
public enum Geometry {
  /**
   * The object is a circle centered around a point.
   */
  CIRCLE,
  /**
   * The shortest path between two points on a sphere.
   */
  GREAT_CIRCLE,
  /**
   * A path crossing meridians of longitude at a constant angle.
   */
  RHUMB_LINE,
  /**
   * A constant-radius arc around the center fix, traversed counterclockwise from the start to the endpoint.
   */
  COUNTER_CLOCKWISE_ARC,
  /**
   * A constant-radius arc around the center fix, traversed clockwise from the start to the endpoint.
   */
  CLOCKWISE_ARC
}
