package org.mitre.tdp.boogie.alg.enroute.impl;

import java.util.List;

import org.mitre.caasd.commons.TimeWindow;
import org.mitre.tdp.boogie.Constraint;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.alg.enroute.EnrouteModel;

public class SpeedLimitModel implements EnrouteModel {

  private Constraint currentSpeedConstraint;

  @Override
  public TimeWindow predict(Leg previousConcrete, Leg nextConcrete, List<Leg> between) {
    return null;
  }
}
