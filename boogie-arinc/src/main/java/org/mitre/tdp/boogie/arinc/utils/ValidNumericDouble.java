package org.mitre.tdp.boogie.arinc.utils;

import static org.apache.commons.lang3.StringUtils.isNumeric;

import java.util.function.Predicate;

public final class ValidNumericDouble implements Predicate<String> {

  public static final ValidNumericDouble INSTANCE = new ValidNumericDouble();

  @Override
  public boolean test(String fieldValue) {
    return (!fieldValue.trim().isEmpty())
        && ((fieldValue.startsWith("+") && isNumeric(fieldValue.substring(1)))
        || (fieldValue.startsWith("-") && isNumeric(fieldValue.substring(1)))
        || (fieldValue.startsWith(" ") && isNumeric(fieldValue.substring(1)))
        || isNumeric(fieldValue));
  }
}
