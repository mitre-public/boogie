package org.mitre.boogie.xml.util;

public record MagVar(String ew, Double value) {
  public static MagVar from(String ew, Double value) {
    return new MagVar(ew, value);
  }
}
