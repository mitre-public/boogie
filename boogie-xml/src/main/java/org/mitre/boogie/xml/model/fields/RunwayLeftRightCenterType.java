package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum RunwayLeftRightCenterType {
  LEFT,
  RIGHT,
  CENTER;
  public static final Set<String> VALID = Arrays.stream(RunwayLeftRightCenterType.values()).map(RunwayLeftRightCenterType::name).collect(Collectors.toUnmodifiableSet());
}
