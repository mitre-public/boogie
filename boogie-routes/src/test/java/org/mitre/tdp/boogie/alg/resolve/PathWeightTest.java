package org.mitre.tdp.boogie.alg.resolve;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * AKELA-1874 This test checks to see that when these various rare procedure combinations happen,
 * where the legs overlap (aka are the same waypoints) that there is a penalty imposed by the visit
 * methods
 */
class PathWeightTest {

  //The fake procedures all have just 2 legs, that all overlap with the PLMRR2
  private final AirwayToken airway = new AirwayToken(Airways.J000());
  private final SidToken sid = new SidToken(PLMMR2.INSTANCE);
  private final StarToken star = new StarToken(STAR_FAKE.INSTANCE);
  private final ApproachToken approach = new ApproachToken(KATL_FAKED.INSTANCE);
  @Test
  void sidAirway() {
    sid.visit(airway).forEach(i -> assertEquals(.00101, i.linkWeight(), "should be a penalty to go from an airway into a sid"));
  }

  @Test
  void sidStar() {
    sid.visit(star).forEach(i -> assertEquals(.00101, i.linkWeight(), "should be a penalty to go from star into a sid"));
  }

  @Test
  void sidApproach() {
    sid.visit(approach).forEach(i -> assertEquals(.00101, i.linkWeight(), "should be a penalty to go from approach into a sid"));
  }

  @Test
  void airwayStar() {
    airway.visit(star).forEach(i -> assertEquals(.00101, i.linkWeight(), "should be a penalty to go from star into airway"));
  }

  @Test
  void airwayApproach() {
    airway.visit(approach).forEach(i -> assertEquals(.00101, i.linkWeight(), "should be a penalty to go from approach into airway"));
  }
}
