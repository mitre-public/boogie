package org.mitre.tdp.boogie;

import java.util.Set;

import org.mitre.tdp.boogie.validate.PathTerminatorBasedLegValidator;

/**
 * Indicator for how the leg terminates (when it is finished being flown) - i.e. the Path Terminator.
 * <br>
 * For graphical pictures of what these represent it is recommended to look at attachment 5 of the Arinc Specification Document.
 * <br>
 * For a predicate which can be used to indicate which fields in the {@link Leg} interface are required in order to model how the
 * leg should be flown see the {@link PathTerminatorBasedLegValidator}.
 */
public enum PathTerminator {
  /**
   * Initial Fix.
   * <br>
   * Defines a fix as a point in space.
   */
  IF,
  /**
   * Track to a Fix.
   * <br>
   * Defines a great circle track over the ground between two known fixes.
   */
  TF,
  /**
   * Course to a Fix.
   * <br>
   * Defines a specified course to a fix.
   */
  CF,
  /**
   * Direct to a Fix.
   * <br>
   * Defines an unspecified track starting from an unspecified location to a fix.
   */
  DF,
  /**
   * Fix to an Altitude.
   * <br>
   * Defines a specified track over ground from a database fix to a specified altitude at an unspecified position.
   */
  FA,
  /**
   * Track from a Fix for a Distance.
   * <br>
   * Defines a specific track over ground from a fix for a specific distance.
   */
  FC,
  /**
   * Track from a Fix to a DMS Distance.
   * <br>
   * Defines a specific track over ground from a fix to a specific DME distance (the DME will be referenced and must exist).
   */
  FD,
  /**
   * Fix to Manual Termination.
   * <br>
   * Defines a specified track over ground from a fix until manual termination of the leg.
   */
  FM,
  /**
   * Course to an Altitude.
   * <br>
   * Defines a course to specific altitude at an unspecified location.
   */
  CA,
  /**
   * Course to a DME Distance.
   * <br>
   * Defines a course to a specific DME distance which corresponds to an existing navaid.
   */
  CD,
  /**
   * Course to an Intercept.
   * <br>
   * Defines a specified course to a subsequent leg.
   */
  CI,
  /**
   * Course to a Radial Termination.
   * <br>
   * Defines a course to a specified radial from a VOR navaid.
   */
  CR,
  /**
   * Constant Radius Arc.
   * <br>
   * Defines a constant radius turn between two fixes, lines tangent to the arc and a center fix.
   */
  RF,
  /**
   * Arc to Fix.
   * <br>
   * Defines a track over ground at a specified constant distance from a DME navaid.
   */
  AF,
  /**
   * Heading to an Altitude Termination.
   * <br>
   * Defines a heading to a specific altitude termination at an unspecified position.
   */
  VA,
  /**
   * Heading to a DME Distance Termination.
   * <br>
   * Defines a specified heading to terminating at a specified distance from a DME navaid.
   */
  VD,
  /**
   * Heading to an Intercept.
   * <br>
   * Defines a heading to intercept the subsequent leg at an unspecified position.
   */
  VI,
  /**
   * Heading to a Manual Termination.
   * <br>
   * Defines a specified heading until a manual termination.
   */
  VM,
  /**
   * Heading to a Radial Termination.
   * <br>
   * Defines a heading to specified radial from a VOR navaid.
   */
  VR,
  /**
   * Procedure Turn.
   * <br>
   * Defines a course reversal starting at the specified fix. Includes an outbound leg followed by a left or right turn and a
   * 180 degree course reversal to intercept the next leg.
   * <br>
   * Note - A maximum excursion time and distance is included as a data field.
   */
  PI,
  /**
   * Holding in Lieu of Procedure Turn.
   * <br>
   * Defines a holding pattern in lieu of a procedure turn course reversal or terminal procedure referenced mandatory holding
   * pattern. Leg time or distance is included.
   * <br>
   * HA = Altitude Termination
   */
  HA,
  /**
   * Holding in Lieu of Procedure Turn.
   * <br>
   * Defines a holding pattern in lieu of a procedure turn course reversal or terminal procedure referenced mandatory holding
   * pattern. Leg time or distance is included.
   * <br>
   * HF = Single circuit terminating at a fix
   */
  HF,
  /**
   * Holding in Lieu of Procedure Turn.
   * <br>
   * Defines a holding pattern in lieu of a procedure turn course reversal or terminal procedure referenced mandatory holding
   * pattern. Leg time or distance is included.
   * <br>
   * HM = Manual Termination
   */
  HM;

  public boolean isFixTerminating() {
    return FIX_TERMINATING.contains(this);
  }

  public boolean isManualTerminating() {
    return MANUALLY_TERMINATING.contains(this);
  }

  public boolean isFixOriginating() {
    return FIX_ORIGINATING.contains(this);
  }

  public boolean isHolding() {
    return HOLDING.contains(this);
  }

  public boolean isArc() {
    return ARC.contains(this);
  }

  public boolean containsFix() {
    return FIX_TERMINATING.contains(this) || FIX_ORIGINATING.contains(this);
  }

  public boolean noFix() {
    return !containsFix();
  }

  public boolean isIntercept() {
    return INTERCEPT.contains(this);
  }

  public static final Set<PathTerminator> FIX_TERMINATING = Set.of(AF, CF, DF, RF, TF, IF, HF);
  public static final Set<PathTerminator> MANUALLY_TERMINATING = Set.of(HM, FM, VM);
  public static final Set<PathTerminator> FIX_ORIGINATING = Set.of(FC, FD, HF, IF, PI, FA);
  public static final Set<PathTerminator> HOLDING = Set.of(HF, HA, HM);
  public static final Set<PathTerminator> ARC = Set.of(AF, RF);
  public static final Set<PathTerminator> INTERCEPT = Set.of(CI, VI, PI);
}
