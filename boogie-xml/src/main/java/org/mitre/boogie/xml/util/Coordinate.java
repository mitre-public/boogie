package org.mitre.boogie.xml.util;

public record Coordinate(String nsew, Integer deg, Integer min, Integer sec, Integer hsec) {
  public static Coordinate from(String nsew, Integer deg, Integer min, Integer sec, Integer hsec) {
    return new Coordinate(nsew, deg, min, sec, hsec);
  }
}
