package org.mitre.tdp.boogie.arinc.assemble;

public enum ArincRouteQualifier {
  // Type 1 Qualifiers
  /**
   * DME Required for Procedure
   */
  D,
  /**
   * GPS (GNSS) required. DME/DME to RNP x.xx not authorized.
   */
  J,
  /**
   * GBAS Procedure
   */
  L,
  /**
   * DME Not Required
   */
  N,
  /**
   * GNSS Required
   */
  P,
  /**
   * GPS (GNSS) or DME/DME to RNP x.xx required.
   */
  R,
  /**
   * DME/DME Required
   */
  T,
  /**
   * RNAV, Sensor Unspecified
   */
  U,
  /**
   * VOR/DME RNAV
   */
  V,
  /**
   * RNAV Procedure requiring FAS Data Block
   */
  W,
  /**
   * Advanced (RNAV RNP, SAAAR/AR not required) - as a Qualifier 1
   * <br>
   * Primary Missed Approach - as a Qualifier 2
   */
  A,
  /**
   * SAAAR/AR Procedure (RNAV RNP)
   */
  F,

  // Type 2 Qualifiers
  /**
   * Secondary Missed Approach
   */
  B,
  /**
   * Engine Out Missed Approach
   */
  E,
  /**
   * Procedure without Straight-in Minimums
   */
  C,
  /**
   * Procedure with Straight-in Minimums
   */
  S,
  /**
   * Used for copter approaches to helipads as Qualifier 2
   */
  H
}
