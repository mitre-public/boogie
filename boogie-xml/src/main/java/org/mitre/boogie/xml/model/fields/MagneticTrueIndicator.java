package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum MagneticTrueIndicator {
  BOTH,
  TRUE,
  MAGNETIC;

  public static final Set<String> VALID = Arrays.stream(MagneticTrueIndicator.values()).map(MagneticTrueIndicator::name).collect(Collectors.toSet());
}
