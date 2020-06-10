package org.mitre.tdp.boogie.utils;

import java.util.function.Predicate;
import java.util.stream.IntStream;

import org.mitre.tdp.boogie.FieldSpec;
import org.mitre.tdp.boogie.FieldSpecParseException;

/**
 * Class of common string utilities useful in ARINC string parsing.
 */
public class ArincStrings {

  public static Predicate<String> isBlank = s -> s.trim().isEmpty();

  /**
   * Returns a copy of the given string with all of the internal characters replaced with blanks.
   */
  public static String copyWithBlanks(String s) {
    return IntStream.range(0, s.length()).mapToObj(i -> " ").reduce("", String::concat);
  }

  /**
   * Common utility for parsing doubles encoded as #### where the last number is the tenths.
   *
   * e.g. 0456 -> 45.6, 1245 -> 124.5, 0005 -> 0.5, 0001234 -> 123.4
   */
  public static Double parseDoubleWithTenths(String s) {
    String cp = new String(s);
    if (s.length() == 1) {
      cp = "0".concat(cp);
    }

    String pre = cp.substring(0, cp.length() - 1);
    String post = cp.substring(cp.length() - 1);
    return Double.parseDouble(pre)
        + (Double.parseDouble(post) / 10.0);
  }

  public static Float parseFloatWithTenths(String s) {
    return parseDoubleWithTenths(s).floatValue();
  }

  /**
   * Attempts to convert the given string field to the provided enum class - if the {@link Enum#valueOf(Class, String)} call
   * fails this method throws a {@link FieldSpecParseException} instead of an {@link IllegalArgumentException}.
   */
  public static <E extends Enum<E> & FieldSpec<E>> E toEnumValue(String string, Class<E> clz) {
    return toEnumValue(string, clz, clz);
  }

  public static <E extends Enum<E>> E toEnumValue(String string, Class<E> clz, Class<? extends FieldSpec<?>> spec) {
    try {
      return E.valueOf(clz, string);
    } catch (IllegalArgumentException e) {
      throw new FieldSpecParseException(spec, string, e);
    }
  }
}
