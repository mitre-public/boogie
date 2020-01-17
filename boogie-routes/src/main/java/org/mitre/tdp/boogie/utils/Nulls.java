package org.mitre.tdp.boogie.utils;

/**
 * Utility class for handling null values across various data types.
 */
public class Nulls {

  public static String blankIfNull(String s) {
    return null == s || s.isEmpty() ? " " : s;
  }

  public static <T> boolean nullOrEquals(T u, T v) {
    return null == u || null == v || u.equals(v);
  }

  public static <T> boolean nonNullEquals(T u, T v) {
    return u != null && u.equals(v);
  }

  public static <T> T ifNullThat(T u, T that) {
    return u == null ? that : u;
  }
}
