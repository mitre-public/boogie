package org.mitre.tdp.boogie.dafif.utils;

import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNumeric;

/**
 * The Dafif specification allows for numeric fields to be included in the string records
 * This class serves the purpose of checking whether any given numeric field in the Dafif spec is represented appropriately.
 * <br>
 * Dafif allows for (+/-/ ) prefixes on all numeric fields which act as modifiers for the actual field value - however all
 * trailing digits must be numeric.
 * <br>
 * If values pass this test they can be handed off to {@link Double#parseDouble(String)} or {@link Integer#parseInt(String)} w/o
 * having to worry about NumberFormatExceptions, etc.
 */
public final class ValidDafifNumeric implements Predicate<String> {

  public static final ValidDafifNumeric INSTANCE = new ValidDafifNumeric();

  @Override
  public boolean test(String fieldValue) {
    String trimmedField = fieldValue.trim();
    return !trimmedField.isEmpty() && nonEmptyFieldIsNumeric(trimmedField);
  }

  boolean nonEmptyFieldIsNumeric(String field) {
    checkArgument(!field.isEmpty());
    String signRemoved = field.startsWith("+") || field.startsWith("-") ? field.substring(1) : field;
    return field.contains(".")
        ? isDouble(signRemoved)
        : isNumeric(signRemoved);
  }

  private boolean isDouble(String str) {
    try {
      double d = Double.parseDouble(str);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }
}
