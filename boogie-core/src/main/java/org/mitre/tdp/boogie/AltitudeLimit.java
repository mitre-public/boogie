package org.mitre.tdp.boogie;

import java.util.Optional;

public interface AltitudeLimit extends Constraint {

  Optional<Double> altitudeLimit();
}
