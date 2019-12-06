package org.mitre.tdp.boogie.alg.resolve;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A set of static heuristic rules for identifying section types by name.
 */
public class SectionHeuristics {

  public static Pattern latLon() {
    return Pattern.compile("[0-9]{4}[NS]{1}/[01]{1}[0-9]{4}[EW]{1}$");
  }

  public static Pattern airway() {
    return Pattern.compile("[VJTQ]{1}[0-9]{1,3}$");
  }

  public static Pattern waypoint() {
    return Pattern.compile("[A-Z]{2,5}$");
  }

  public static Pattern tailored() {
    return Pattern.compile("[A-Z]{2,5}[0-9]{6}$");
  }

  public static Pattern airport() {
    return Pattern.compile("[A-Z]{4}$");
  }

  public static boolean matches(String s, Pattern p) {
    Matcher m = p.matcher(s);
    return m.find();
  }
}
