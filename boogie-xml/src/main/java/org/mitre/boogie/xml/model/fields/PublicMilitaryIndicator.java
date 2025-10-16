package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum PublicMilitaryIndicator {
  PRIVATE,
  CIVIL,
  MILITARY,
  JOINT;
  public static Set<String> VALID = Arrays.stream(PublicMilitaryIndicator.values()).map(PublicMilitaryIndicator::name).collect(Collectors.toSet());
}
