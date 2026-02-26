package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum FreqUnitOfMeasure {
  MEGA_HERTZ,
  KILO_HERTZ;
  public static final Set<String> VALID = Arrays.stream(FreqUnitOfMeasure.values()).map(FreqUnitOfMeasure::name).collect(Collectors.toUnmodifiableSet());
}
