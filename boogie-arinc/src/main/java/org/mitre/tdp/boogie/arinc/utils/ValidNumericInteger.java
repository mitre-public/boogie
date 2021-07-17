package org.mitre.tdp.boogie.arinc.utils;

import static org.apache.commons.lang3.StringUtils.isNumeric;

import java.util.function.Predicate;

public final class ValidNumericInteger implements Predicate<String> {

  public static final ValidNumericInteger INSTANCE = new ValidNumericInteger();

  @Override
  public boolean test(String fieldValue) {
    return !fieldValue.trim().isEmpty()
        && ((fieldValue.startsWith("-") && isNumeric(fieldValue.substring(1))) || isNumeric(fieldValue));
  }
}
