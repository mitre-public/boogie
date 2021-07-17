package org.mitre.tdp.boogie.arinc.utils;

import static com.google.common.collect.Sets.newHashSet;

import java.util.HashSet;
import java.util.function.Function;

public final class BooleanStringParser implements Function<String, Boolean> {

  public static final BooleanStringParser INSTANCE = new BooleanStringParser();

  @Override
  public Boolean apply(String fieldValue) {
    return trueValues.contains(fieldValue.trim());
  }

  private static final HashSet<String> trueValues = newHashSet("Y", "y", "TRUE", "True", "true", "YES", "Yes", "yes");
}
