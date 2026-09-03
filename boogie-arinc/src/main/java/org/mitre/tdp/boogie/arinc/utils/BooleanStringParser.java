package org.mitre.tdp.boogie.arinc.utils;

import java.util.Set;
import java.util.function.Predicate;

public final class BooleanStringParser implements Predicate<String> {

  public static final BooleanStringParser INSTANCE = new BooleanStringParser();
  private static final Set<String> TRUE_VALUES = Set.of("Y", "y", "TRUE", "True", "true", "YES", "Yes", "yes");

  @Override
  public boolean test(String fieldValue) {
    return TRUE_VALUES.contains(fieldValue.trim());
  }
}
