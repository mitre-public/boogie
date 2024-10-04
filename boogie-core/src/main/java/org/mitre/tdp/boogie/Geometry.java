package org.mitre.tdp.boogie;

/**
 * This class represents a generic geometry concept.
 */
public enum Geometry {
  /**
   * The object is a circle centered around a point.
   */
  CIRCLE,
  /**
   * The shortest distance between two points on a sphere
   */
  GREAT_CIRCLE,
  /**
   * An arc crossing all meridians of longitude at the same angle
   */
  RHUMB_LINE,
  /**
   * An arc that starts/ends centered on a point in the counter-clockwise direction
   */
  COUNTER_CLOCKWISE_ARC,
  /**
   * An arc that starts/ends centered on a point in teh clockwise direction
   */
  CLOCKWISE_ARC
}
