package org.mitre.tdp.boogie.arinc.utils;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNumeric;

import java.util.function.Predicate;

/**
 * The ARINC 424 specification allows for numeric fields to be included in the string records - but elides the decimal place
 * in the raw data, instead stating in the specification the location of the decimal in the string value.
 * <br>
 * This class serves the purpose of checking whether any given numeric field in the ARINC spec is represented appropriately.
 * <br>
 * ARINC allows for (+/-/ ) prefixes on all numeric fields which act as modifiers for the actual field value - however all
 * trailing digits must be numeric.
 */
public final class ValidArincNumeric implements Predicate<String> {

  public static final ValidArincNumeric INSTANCE = new ValidArincNumeric();

  @Override
  public boolean test(String fieldValue) {
    String trimmedField = fieldValue.trim();
    return !trimmedField.isEmpty() && nonEmptyFieldIsNumeric(trimmedField);
  }

  boolean nonEmptyFieldIsNumeric(String field) {
    checkArgument(!field.isEmpty());
    return field.startsWith("+") || field.startsWith("-")
        ? isNumeric(field.substring(1))
        : isNumeric(field);
  }
}
