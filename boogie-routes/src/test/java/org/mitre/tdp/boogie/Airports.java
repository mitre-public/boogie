package org.mitre.tdp.boogie;

import java.util.List;

import org.checkerframework.checker.units.qual.Length;
import org.mitre.caasd.commons.Course;
import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.LatLong;

/**
 * Collection of airport objects and their actual locations for use in testing with one runway as well.
 */
public final class Airports {

  private Airports() {
    throw new IllegalStateException("Unable to instantiate static factory class.");
  }

  public static Airport KDEN() {
    Runway rw16r = Runway.builder()
        .runwayIdentifier("RW16R")
        .course(Course.ofDegrees(180.4617))
        .length(Distance.ofFeet(16000.0))
        .origin(LatLong.of(39.8958, -104.6961))
        .elevation(Distance.ofFeet(5322.0))
        .build();
    Runway rw08 = Runway.builder()
        .runwayIdentifier("RW08")
        .course(Course.ofDegrees(90.5403))
        .length(Distance.ofFeet(12000.0))
        .origin(LatLong.of(39.8776, -104.6622))
        .elevation(Distance.ofFeet(5354.0))
        .build();
    return Airport.builder()
        .airportIdentifier("KDEN")
        .latLong(LatLong.of(39.861666666666665, -104.67316666666667))
        .runways(List.of(rw16r, rw08))
        .build();
  }

  public static Airport KATL() {
    Runway runway27r = Runway.builder()
        .runwayIdentifier("RW27R")
        .course(Course.ofDegrees(270.0108))
        .length(Distance.ofFeet(12390.0))
        .origin(LatLong.of(33.6347, -84.4089))
        .elevation(Distance.ofFeet(978.0))
        .build();
    Runway rw10 = Runway.builder()
        .runwayIdentifier("RW10")
        .course(Course.ofDegrees(089.9918))
        .length(Distance.ofFeet(9000.0))
        .origin(LatLong.of(33.6203, -84.4479))
        .elevation(Distance.ofFeet(1000.0))
        .build();
    return Airport.builder()
        .airportIdentifier("KATL")
        .latLong(LatLong.of(33.6367, -84.4278638888889))
        .runways(List.of(runway27r, rw10))
        .build();
  }

  public static Airport KMCO() {
    Runway rw35l = Runway.builder()
        .runwayIdentifier("RW35L")
        .course(Course.ofDegrees(359.5098))
        .length(Distance.ofFeet(10000.0))
        .origin(LatLong.of(28.4081, -81.2956))
        .elevation(Distance.ofFeet(87.0))
        .build();
    return Airport.builder()
        .airportIdentifier("KMCO")
        .latLong(LatLong.of(28.4293889, -81.3090000))
        .runways(List.of(rw35l))
        .build();
  }

  public static Airport KPHL() {
    Runway rw27L = Runway.builder()
        .runwayIdentifier("RW27L")
        .course(Course.ofDegrees(256.0319))
        .length(Distance.ofFeet(12000.0))
        .origin(LatLong.of(39.8678, -75.2402))
        .elevation(Distance.ofFeet(10.0))
        .build();
    return Airport.builder()
        .airportIdentifier("KPHL")
        .latLong(LatLong.of(39.872084, -75.240663))
        .runways(List.of(rw27L))
        .build();
  }

  public static Airport KSEA() {
    Runway rw34R = Runway.builder()
        .runwayIdentifier("RW34R")
        .course(Course.ofDegrees(0.3590))
        .length(Distance.ofFeet(11901.0))
        .origin(LatLong.of(47.4312, -122.3080))
        .elevation(Distance.ofFeet(347.0))
        .build();
    return Airport.builder()
        .airportIdentifier("KSEA")
        .latLong(LatLong.of(47.449888888888886, -122.31177777777778))
        .runways(List.of(rw34R))
        .build();
  }

  public static Airport GQNO() {
    Runway rw34 = Runway.builder()
        .elevation(Distance.ofFeet(16.0))
        .runwayIdentifier("RW34")
        .course(Course.ofDegrees(329.3184))
        .origin(LatLong.of(18.2898, -15.9574))
        .length(Distance.ofFeet(11155.0))
        .build();
    return Airport.builder()
        .airportIdentifier("GQNO")
        .latLong(LatLong.of(18.31, -15.969722))
        .runways(List.of(rw34))
        .build();
  }

  public static Airport WSSS() {
    Runway runway20C = Runway.builder()
        .elevation(Distance.ofFeet(16))
        .runwayIdentifier("RW20C")
        .course(Course.ofDegrees(202.6827))
        .origin(LatLong.of(1.360, 103.999))
        .length(Distance.ofFeet(11155.0))
        .build();
    return Airport.builder()
        .airportIdentifier("WSSS")
        .latLong(LatLong.of(1.35019, 103.994003))
        .runways(List.of(runway20C))
        .build();
  }
}
