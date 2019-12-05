package org.mitre.tdp.boogie.utils;

/** Collection of simple sanity checks for identifiers of various infrastructure types. */
public class IsValid {

  static boolean alphaNumericUpper(String val) {
    for (Character c : val.toCharArray()) {
      if (Character.isAlphabetic(c) && !Character.isUpperCase(c)) {
        return false;
      }
      if (!Character.isAlphabetic(c) && !Character.isDigit(c)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Fix identifiers are expected to be alphanumeric and upper case. Outside of this they can be somewhat non-standard.
   */
  public static boolean fixId(String id) {
    return alphaNumericUpper(id);
  }

  /**
   * Airport identifiers are expected to be alphanumeric and upper case and between 3/4 letters.
   */
  public static boolean airportId(String id) {
    return alphaNumericUpper(id) && (id.length() == 3 || id.length() == 4);
  }

  /**
   * Procedure names are expected to end with a number and be uppercase alphanumeric.
   */
  public static boolean procedure(String id) {
    return alphaNumericUpper(id) && Character.isDigit(id.toCharArray()[id.length() - 1]);
  }

  /**
   * Airway names are expected to start with a character and be uppercase alphanumeric.
   */
  public static boolean airway(String id) {
    return alphaNumericUpper(id) && Character.isAlphabetic(id.toCharArray()[0]);
  }
}
