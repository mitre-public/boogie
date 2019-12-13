package org.mitre.tdp.boogie;

import java.util.Arrays;
import java.util.function.Predicate;

import com.google.common.base.Preconditions;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Aka Path Terminator.
 *
 * For graphical pictures of what these represent it is recommended to look at attachment 5 of the
 * Arinc Specification Document.
 *
 * Note each leg type is also a predicate over the {@link Leg} data type enforcing the existence
 * of key fields.
 */
public enum LegType {
  /**
   * Initial Fix.
   *
   * Defines a fix as a point in space.
   */
  IF(l -> {
    checkNotNull(l.pathTerminator());
    return true;
  }),
  /**
   * Track to a Fix.
   *
   * Defines a great circle track over the ground between two
   * known fixes.
   */
  TF(l -> {
    checkNotNull(l.pathTerminator());
    return true;
  }),
  /**
   * Course to a Fix.
   *
   * Defines a specified course to a fix.
   */
  CF(l -> {
    checkNotNull(l.pathTerminator());
    checkArgument(l.recommendedNavaid().isPresent());
    checkArgument(l.theta().isPresent());
    checkArgument(l.rho().isPresent());
    checkArgument(l.outboundMagneticCourse().isPresent());
    checkArgument(l.distance().isPresent());
    return true;
  }),
  /**
   * Direct to a Fix.
   *
   * Defines an unspecified track starting from an unspecified
   * location to a fix.
   */
  DF(l -> {
    checkNotNull(l.pathTerminator());
    return true;
  }),
  /**
   * Fix to an Altitude.
   *
   * Defines a specified track over ground from a database fix
   * to a specified altitude at an unspecified position.
   */
  FA(l -> {
    checkNotNull(l.pathTerminator());
    checkArgument(l.recommendedNavaid().isPresent());
    checkArgument(l.theta().isPresent());
    checkArgument(l.rho().isPresent());
    checkArgument(l.outboundMagneticCourse().isPresent());
//    checkNotNull(l.targetAltitude());
    return true;
  }),
  /**
   * Track from a Fix for a Distance.
   *
   * Defines a specific track over ground from a fix for a specific
   * distance.
   */
  FC(l -> {
    checkNotNull(l.pathTerminator());
    checkArgument(l.recommendedNavaid().isPresent());
    checkArgument(l.theta().isPresent());
    checkArgument(l.rho().isPresent());
    checkArgument(l.outboundMagneticCourse().isPresent());
    return true;
  }),
  /**
   * Track from a Fix to a DMS Distance.
   *
   * Defines a specific track over ground from a fix to a specific DME
   * distance (the DME will be referenced and must exist).
   */
  FD(l -> {
    checkNotNull(l.pathTerminator());
    checkArgument(l.recommendedNavaid().isPresent());
    checkArgument(l.theta().isPresent());
    checkArgument(l.rho().isPresent());
    checkArgument(l.outboundMagneticCourse().isPresent());
    return true;
  }),
  /**
   * Fix to Manual Termination.
   *
   * Defines a specified track over ground from a fix until manual
   * termination of the leg.
   */
  FM(l -> {
    checkNotNull(l.pathTerminator());
    checkArgument(l.recommendedNavaid().isPresent());
    checkArgument(l.theta().isPresent());
    checkArgument(l.rho().isPresent());
    checkArgument(l.outboundMagneticCourse().isPresent());
    return true;
  }),
  /**
   * Course to an Altitude.
   *
   * Defines a course to specific altitude at an unspecified location.
   */
  CA(l -> {
    checkArgument(l.outboundMagneticCourse().isPresent());
//    checkNotNull(l.targetAltitude());
    return true;
  }),
  /**
   * Course to a DME Distance.
   *
   * Defines a course to a specific DME distance which corresponds to
   * an existing navaid.
   */
  CD(l -> {
    checkArgument(l.recommendedNavaid().isPresent());
    checkArgument(l.outboundMagneticCourse().isPresent());
    checkArgument(l.distance().isPresent());
    return true;
  }),
  /**
   * Course to an Intercept.
   *
   * Defines a specified course to a subsequent leg.
   */
  CI(l -> {
    checkArgument(l.outboundMagneticCourse().isPresent());
    return true;
  }),
  /**
   * Course to a Radial Termination.
   *
   * Defines a course to a specified radial from a VOR navaid.
   */
  CR(l -> {
    checkArgument(l.recommendedNavaid().isPresent());
    checkArgument(l.theta().isPresent());
    checkArgument(l.outboundMagneticCourse().isPresent());
    return true;
  }),
  /**
   * Constant Radius Arc.
   *
   * Defines a constant radius turn between two fixes, lines tangent
   * to the arc and a center fix.
   */
  RF(l -> {
    checkNotNull(l.pathTerminator());
    checkArgument(l.turnDirection().isPresent());
    checkArgument(l.distance().isPresent());
    checkArgument(l.centerFix().isPresent());
    return true;
  }),
  /**
   * Arc to Fix.
   *
   * Defines a track over ground at a specified constant distance from
   * a DME navaid.
   */
  AF(l -> {
    checkNotNull(l.pathTerminator());
    checkArgument(l.turnDirection().isPresent());
    checkArgument(l.recommendedNavaid().isPresent());
    checkArgument(l.theta().isPresent());
    checkArgument(l.rho().isPresent());
    return true;
  }),
  /**
   * Heading to an Altitude Termination.
   *
   * Defines a heading to a specific altitude termination at an unspecified
   * position.
   */
  VA(l -> {
    checkArgument(l.outboundMagneticCourse().isPresent());
//    checkNotNull(l.targetAltitude());
    return true;
  }),
  /**
   * Heading to a DME Distance Termination.
   *
   * Defines a specified heading to terminating at a specified distance from
   * a DME navaid.
   */
  VD(l -> {
    checkArgument(l.recommendedNavaid().isPresent());
    checkArgument(l.outboundMagneticCourse().isPresent());
    checkArgument(l.distance().isPresent());
    return true;
  }),
  /**
   * Heading to an Intercept.
   *
   * Defines a heading to intercept the subsequent leg at an unspecified position.
   */
  VI(l -> {
    checkArgument(l.outboundMagneticCourse().isPresent());
    return true;
  }),
  /**
   * Heading to a Manual Termination.
   *
   * Defines a specified heading until a manual termination.
   */
  VM(l -> {
    checkArgument(l.outboundMagneticCourse().isPresent());
    return true;
  }),
  /**
   * Heading to a Radial Termination.
   *
   * Defines a heading to specified radial from a VOR navaid.
   */
  VR(l -> {
    checkArgument(l.recommendedNavaid().isPresent());
    checkArgument(l.theta().isPresent());
    checkArgument(l.outboundMagneticCourse().isPresent());
    return true;
  }),
  /**
   * Procedure Turn.
   *
   * Defines a course reversal starting at the specified fix. Includes an outbound
   * leg followed by a left or right turn and a 180 degree course reversal to intercept
   * the next leg.
   *
   * Note - A maximum excursion time and distance is included as a data field.
   */
  PI(l -> {
    checkNotNull(l.pathTerminator());
    checkArgument(l.turnDirection().isPresent());
    checkArgument(l.recommendedNavaid().isPresent());
    checkArgument(l.theta().isPresent());
    checkArgument(l.rho().isPresent());
    checkArgument(l.outboundMagneticCourse().isPresent());
    checkArgument(l.distance().isPresent());
//    checkNotNull(l.targetAltitude());
    return true;
  }),
  /**
   * Holding in Lieu of Procedure Turn.
   *
   * Defines a holding pattern in lieu of a procedure turn course reversal or terminal
   * procedure referenced mandatory holding pattern. Leg time or distance is included.
   *
   * HA = Altitude Termination
   */
  HA(l -> {
    checkNotNull(l.pathTerminator());
    checkArgument(l.turnDirection().isPresent());
    checkArgument(l.outboundMagneticCourse().isPresent());
    checkArgument(l.distance().isPresent());
//    checkNotNull(l.targetAltitude());
    return true;
  }),
  /**
   * Holding in Lieu of Procedure Turn.
   *
   * Defines a holding pattern in lieu of a procedure turn course reversal or terminal
   * procedure referenced mandatory holding pattern. Leg time or distance is included.
   *
   * HF = Single circuit terminating at a fix
   */
  HF(l -> {
    checkNotNull(l.pathTerminator());
    checkArgument(l.turnDirection().isPresent());
    checkArgument(l.outboundMagneticCourse().isPresent());
    checkArgument(l.distance().isPresent());
    return true;
  }),
  /**
   * Holding in Lieu of Procedure Turn.
   *
   * Defines a holding pattern in lieu of a procedure turn course reversal or terminal
   * procedure referenced mandatory holding pattern. Leg time or distance is included.
   *
   * HM = Manual Termination
   */
  HM(l -> {
    checkNotNull(l.pathTerminator());
    checkArgument(l.turnDirection().isPresent());
    checkArgument(l.outboundMagneticCourse().isPresent());
    checkArgument(l.distance().isPresent());
    return true;
  });

  private final Predicate<Leg> valid;

  LegType(Predicate<Leg> v) {
    this.valid = v;
  }

  public boolean concrete() {
    return concrete(this);
  }

  public boolean arc() {
    return arc(this);
  }

  public boolean valid(Leg leg) {
    Preconditions.checkArgument(leg.type().equals(this));
    return valid.test(leg);
  }

  /**
   * Concrete leg types are those that end with a specified fix identifier.
   */
  public static Predicate<LegType> CONCRETE = leg -> Arrays.asList(IF, TF, CF, AF, DF, RF).contains(leg);

  public static Predicate<LegType> ARC = leg -> Arrays.asList(AF, RF).contains(leg);

  public static boolean concrete(LegType... types) {
    return Arrays.stream(types).allMatch(CONCRETE);
  }

  public static boolean arc(LegType... types) {
    return Arrays.stream(types).allMatch(ARC);
  }
}
