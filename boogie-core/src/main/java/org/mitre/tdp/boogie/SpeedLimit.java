package org.mitre.tdp.boogie;

import java.util.Optional;

public interface SpeedLimit extends Constraint {

  Optional<Double> speedLimit();
}
