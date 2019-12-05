package org.mitre.tdp.boogie.utils;

/**
 * Utility class for handling null values across various data types.
 */
public class Nulls {

  public static String blankIfNull(String s) {
    return null == s || s.isEmpty() ? " " : s;
  }
}
