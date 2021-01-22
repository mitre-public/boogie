package org.mitre.tdp.boogie;

import java.util.Arrays;
import java.util.function.Predicate;

import com.google.common.base.Preconditions;

/**
 * Aka Path Terminator.
 *
 * For graphical pictures of what these represent it is recommended to look at attachment 5 of the Arinc Specification Document.
 *
 * Note each leg type is also a predicate over the {@link Leg} data type enforcing the existence of key fields.
 */
public enum PathTerm implements LegType {
  /**
   * Initial Fix.
   *
   * Defines a fix as a point in space.
   */
  IF(l -> l.pathTerminator() != null && l.pathTerminator().latLong() != null),
  /**
   * Track to a Fix.
   *
   * Defines a great circle track over the ground between two known fixes.
   */
  TF(l -> l.pathTerminator() != null && l.pathTerminator().latLong() != null),
  /**
   * Course to a Fix.
   *
   * Defines a specified course to a fix.
   */
  CF(l -> l.pathTerminator() != null && l.pathTerminator().latLong() != null
      && l.recommendedNavaid().isPresent()
      && l.theta().isPresent()
      && l.rho().isPresent()
      && l.outboundMagneticCourse().isPresent()
      && l.routeDistance().isPresent()),
  /**
   * Direct to a Fix.
   *
   * Defines an unspecified track starting from an unspecified location to a fix.
   */
  DF(l -> l.pathTerminator() != null && l.pathTerminator().latLong() != null),
  /**
   * Fix to an Altitude.
   *
   * Defines a specified track over ground from a database fix to a specified altitude at an unspecified position.
   */
  FA(l -> l.pathTerminator() != null && l.pathTerminator().latLong() != null
      && l.recommendedNavaid().isPresent()
      && l.theta().isPresent()
      && l.rho().isPresent()
      && l.outboundMagneticCourse().isPresent()),
  /**
   * Track from a Fix for a Distance.
   *
   * Defines a specific track over ground from a fix for a specific distance.
   */
  FC(l -> l.pathTerminator() != null && l.pathTerminator().latLong() != null
      && l.recommendedNavaid().isPresent()
      && l.theta().isPresent()
      && l.rho().isPresent()
      && l.outboundMagneticCourse().isPresent()),
  /**
   * Track from a Fix to a DMS Distance.
   *
   * Defines a specific track over ground from a fix to a specific DME distance (the DME will be referenced and must exist).
   */
  FD(l -> l.pathTerminator() != null && l.pathTerminator().latLong() != null
      && l.recommendedNavaid().isPresent()
      && l.theta().isPresent()
      && l.rho().isPresent()
      && l.outboundMagneticCourse().isPresent()),
  /**
   * Fix to Manual Termination.
   *
   * Defines a specified track over ground from a fix until manual termination of the leg.
   */
  FM(l -> l.pathTerminator() != null && l.pathTerminator().latLong() != null
      && l.recommendedNavaid().isPresent()
      && l.theta().isPresent()
      && l.rho().isPresent()
      && l.outboundMagneticCourse().isPresent()),
  /**
   * Course to an Altitude.
   *
   * Defines a course to specific altitude at an unspecified location.
   */
  CA(l -> l.outboundMagneticCourse().isPresent()),
  /**
   * Course to a DME Distance.
   *
   * Defines a course to a specific DME distance which corresponds to an existing navaid.
   */
  CD(l -> l.recommendedNavaid().isPresent()
      && l.outboundMagneticCourse().isPresent()
      && l.routeDistance().isPresent()),
  /**
   * Course to an Intercept.
   *
   * Defines a specified course to a subsequent leg.
   */
  CI(l -> l.outboundMagneticCourse().isPresent()),
  /**
   * Course to a Radial Termination.
   *
   * Defines a course to a specified radial from a VOR navaid.
   */
  CR(l -> l.recommendedNavaid().isPresent()
      && l.theta().isPresent()
      && l.outboundMagneticCourse().isPresent()),
  /**
   * Constant Radius Arc.
   *
   * Defines a constant radius turn between two fixes, lines tangent to the arc and a center fix.
   */
  RF(l -> l.pathTerminator() != null && l.pathTerminator().latLong() != null
      && l.turnDirection().isPresent()
      && l.routeDistance().isPresent()
      && l.centerFix().filter(fix -> fix.latLong() != null).isPresent()),
  /**
   * Arc to Fix.
   *
   * Defines a track over ground at a specified constant distance from a DME navaid.
   */
  AF(l -> l.pathTerminator() != null && l.pathTerminator().latLong() != null
      && l.turnDirection().isPresent()
      && l.recommendedNavaid().filter(fix -> fix.latLong() != null).isPresent()
      && l.theta().isPresent()
      && l.rho().isPresent()),
  /**
   * Heading to an Altitude Termination.
   *
   * Defines a heading to a specific altitude termination at an unspecified position.
   */
  VA(l -> l.outboundMagneticCourse().isPresent()
      && l.altitudeConstraint().flatMap(AltitudeLimit::altitudeLimit).isPresent()),
  /**
   * Heading to a DME Distance Termination.
   *
   * Defines a specified heading to terminating at a specified distance from a DME navaid.
   */
  VD(l -> l.recommendedNavaid().isPresent()
      && l.outboundMagneticCourse().isPresent()
      && l.routeDistance().isPresent()),
  /**
   * Heading to an Intercept.
   *
   * Defines a heading to intercept the subsequent leg at an unspecified position.
   */
  VI(l -> l.outboundMagneticCourse().isPresent()),
  /**
   * Heading to a Manual Termination.
   *
   * Defines a specified heading until a manual termination.
   */
  VM(l -> l.outboundMagneticCourse().isPresent()),
  /**
   * Heading to a Radial Termination.
   *
   * Defines a heading to specified radial from a VOR navaid.
   */
  VR(l -> l.recommendedNavaid().isPresent()
      && l.theta().isPresent()
      && l.outboundMagneticCourse().isPresent()),
  /**
   * Procedure Turn.
   *
   * Defines a course reversal starting at the specified fix. Includes an outbound leg followed by a left or right turn and a
   * 180 degree course reversal to intercept the next leg.
   *
   * Note - A maximum excursion time and distance is included as a data field.
   */
  PI(l -> l.pathTerminator() != null && l.pathTerminator().latLong() != null
      && l.turnDirection().isPresent()
      && l.recommendedNavaid().isPresent()
      && l.theta().isPresent()
      && l.rho().isPresent()
      && l.outboundMagneticCourse().isPresent()
      && l.routeDistance().isPresent()),
  /**
   * Holding in Lieu of Procedure Turn.
   *
   * Defines a holding pattern in lieu of a procedure turn course reversal or terminal procedure referenced mandatory holding
   * pattern. Leg time or distance is included.
   *
   * HA = Altitude Termination
   */
  HA(l -> l.pathTerminator() != null && l.pathTerminator().latLong() != null
      && l.turnDirection().isPresent()
      && l.outboundMagneticCourse().isPresent()
      && l.routeDistance().isPresent()),
  /**
   * Holding in Lieu of Procedure Turn.
   *
   * Defines a holding pattern in lieu of a procedure turn course reversal or terminal procedure referenced mandatory holding
   * pattern. Leg time or distance is included.
   *
   * HF = Single circuit terminating at a fix
   */
  HF(l -> l.pathTerminator() != null && l.pathTerminator().latLong() != null
      && l.turnDirection().isPresent()
      && l.outboundMagneticCourse().isPresent()
      && l.routeDistance().isPresent()),
  /**
   * Holding in Lieu of Procedure Turn.
   *
   * Defines a holding pattern in lieu of a procedure turn course reversal or terminal procedure referenced mandatory holding
   * pattern. Leg time or distance is included.
   *
   * HM = Manual Termination
   */
  HM(l -> l.pathTerminator() != null && l.pathTerminator().latLong() != null
      && l.turnDirection().isPresent()
      && l.outboundMagneticCourse().isPresent()
      && l.routeDistance().isPresent()),
  /**
   * Unknown path terminator type - used to represent manually defined legs.
   */
  UNK(l -> true);

  private final Predicate<Leg> valid;

  PathTerm(Predicate<Leg> v) {
    this.valid = v;
  }

  @Override
  public boolean isConcrete() {
    return typesAreConcrete(this);
  }

  @Override
  public boolean isArc() {
    return typesAreArcs(this);
  }

  @Override
  public boolean hasRequiredFields(Leg leg) {
    return valid.test(leg);
  }

  public static boolean typesAreConcrete(PathTerm... types) {
    return Arrays.stream(types).allMatch(CONCRETETYPES);
  }

  public static boolean typesAreArcs(PathTerm... types) {
    return Arrays.stream(types).allMatch(ARCTYPES);
  }

  /**
   * Concrete leg types are those that end with a specified fix identifier.
   */
  public static final Predicate<PathTerm> CONCRETETYPES = leg -> Arrays.asList(IF, TF, CF, AF, DF, RF, FM).contains(leg);

  public static final Predicate<PathTerm> ARCTYPES = leg -> Arrays.asList(AF, RF).contains(leg);
}
