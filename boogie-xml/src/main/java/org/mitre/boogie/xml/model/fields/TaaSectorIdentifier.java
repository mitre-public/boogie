package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum TaaSectorIdentifier {
  STRAIGHT_IN_OR_CENTER_FIX,
  LEFT_BASE,
  RIGHT_BASE;

  public static final Set<String> VALID = Arrays.stream(TaaSectorIdentifier.values()).map(TaaSectorIdentifier::name).collect(Collectors.toSet());
}
