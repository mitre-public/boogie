package org.mitre.tdp.boogie.conformance.alg.assign;

import java.time.Duration;
import java.util.Optional;
import org.mitre.tdp.boogie.AltitudeLimit;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.SpeedLimit;
import org.mitre.tdp.boogie.TurnDirection;

public class UnknownLeg implements Leg {
  @Override
  public Fix pathTerminator() {
    return null;
  }

  @Override
  public Optional<? extends Fix> recommendedNavaid() {
    return Optional.empty();
  }

  @Override
  public Optional<? extends Fix> centerFix() {
    return Optional.empty();
  }

  @Override
  public PathTerm type() {
    return PathTerm.DF;
  }

  @Override
  public Integer sequenceNumber() {
    return null;
  }

  @Override
  public Optional<Double> outboundMagneticCourse() {
    return Optional.empty();
  }

  @Override
  public Optional<Double> rho() {
    return Optional.empty();
  }

  @Override
  public Optional<Double> theta() {
    return Optional.empty();
  }

  @Override
  public Optional<Double> rnp() {
    return Optional.empty();
  }

  @Override
  public Optional<Double> routeDistance() {
    return Optional.empty();
  }

  @Override
  public Optional<Duration> holdTime() {
    return Optional.empty();
  }

  @Override
  public Optional<Double> verticalAngle() {
    return Optional.empty();
  }

  @Override
  public Optional<? extends SpeedLimit> speedConstraint() {
    return Optional.empty();
  }

  @Override
  public Optional<? extends AltitudeLimit> altitudeConstraint() {
    return Optional.empty();
  }

  @Override
  public Optional<? extends TurnDirection> turnDirection() {
    return Optional.empty();
  }

  @Override
  public Optional<Boolean> overfly() {
    return Optional.empty();
  }
}
