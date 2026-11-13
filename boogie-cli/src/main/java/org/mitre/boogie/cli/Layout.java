package org.mitre.boogie.cli;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Formatting shared by the XML and fixed-width text views, so that both formats print the same record the same way.
 */
final class Layout {

  private Layout() {
  }

  /**
   * Appends {@code identifier — name}, omitting a name that is empty or repeats the identifier.
   */
  static void header(StringBuilder result, String identifier, String name) {
    result.append(identifier.isBlank() ? "-" : identifier);
    if (!name.isEmpty() && !name.equals(identifier)) {
      result.append(" — ").append(name);
    }
  }

  /**
   * Appends an indented line, skipping blank text.
   */
  static void line(StringBuilder result, String text) {
    if (!text.isBlank()) {
      result.append(System.lineSeparator()).append("  ").append(text.stripTrailing());
    }
  }

  /**
   * Formats one leg of a procedure or airway: sequence, path terminator and fix in fixed columns, then the present parts.
   */
  static String leg(String sequence, String pathTerminator, String fix, String... parts) {
    return "  " + sequence + " " + (pathTerminator == null ? "" : pad(pathTerminator, 2) + " ") + pad(fix, 5)
        + String.join("", Arrays.stream(parts)
            .filter(part -> !part.isEmpty())
            .map(part -> "  " + part)
            .toList());
  }

  /**
   * Formats a labelled value in a leg summary, e.g. {@code crs 214.7°} or {@code min 3000 ft}.
   */
  static String tag(String tag, String value, String unit) {
    return value.isEmpty() ? "" : tag + " " + value + (unit.isEmpty() || unit.equals("°") ? unit : " " + unit);
  }

  /**
   * Joins the non-empty values.
   */
  static String joined(String separator, String... values) {
    return String.join(separator, Arrays.stream(values).filter(value -> !value.isEmpty()).toList());
  }

  /**
   * Joins the non-empty values with commas, or returns {@code -} if there are none.
   */
  static String list(List<String> values) {
    List<String> present = values.stream().filter(value -> !value.isEmpty()).toList();
    return present.isEmpty() ? "-" : String.join(", ", present);
  }

  static String missing(String value) {
    return value.isEmpty() ? "-" : value;
  }

  static String pad(String value, int width) {
    return String.format(Locale.ROOT, "%-" + width + "s", value.isEmpty() ? "-" : value);
  }

  /**
   * Formats a zero-padded sequence number, keeping an unparseable source value visible.
   */
  static String sequence(String value, int width) {
    try {
      return String.format(Locale.ROOT, "%0" + width + "d", Integer.parseInt(value.strip()));
    } catch (NumberFormatException e) {
      return pad(value.strip(), width);
    }
  }

  /**
   * Formats a number with at most six decimal places and no trailing zeros, e.g. {@code 35} or {@code 214.7}.
   */
  static String number(double value) {
    if (!Double.isFinite(value)) {
      return "invalid";
    }
    return BigDecimal.valueOf(value)
        .setScale(6, RoundingMode.HALF_UP)
        .stripTrailingZeros()
        .toPlainString();
  }

  /**
   * Formats numeric text like {@link #number(double)}, returning other text unchanged.
   */
  static String number(String value) {
    try {
      return number(Double.parseDouble(value));
    } catch (NumberFormatException e) {
      return value;
    }
  }

  /**
   * Formats a magnetic variation or declination, e.g. {@code 15° W}.
   */
  static String variation(double degrees, String hemisphere) {
    return number(Math.abs(degrees)) + "°" + (hemisphere.isEmpty() ? "" : " " + hemisphere);
  }
}
