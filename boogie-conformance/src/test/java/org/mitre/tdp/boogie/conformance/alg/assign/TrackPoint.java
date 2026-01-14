package org.mitre.tdp.boogie.conformance.alg.assign;

import java.time.Instant;
import java.util.Optional;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.ConformablePoint;

public record TrackPoint(String key, Instant time, Double lat, Double lon, Double alt, Double course, Double speed, Double climb) implements ConformablePoint {
  public static TrackPoint from(String key, Instant time, Double lat, Double lon, Double alt, Double course, Double speed, Double climb) {
    return new TrackPoint(key, time, lat, lon, alt, course, speed, climb);
  }

  @Override
  public Optional<Double> trueCourse() {
    return Optional.ofNullable(course);
  }

  @Override
  public Optional<Double> pressureAltitude() {
    return Optional.ofNullable(alt);
  }

  @Override
  public Optional<Double> indicatedAirspeed() {
    return Optional.ofNullable(speed);
  }

  @Override
  public Optional<Double> climbrate() {
    return Optional.ofNullable(climb);
  }

  @Override
  public LatLong latLong() {
    return LatLong.of(lat, lon);
  }
}
