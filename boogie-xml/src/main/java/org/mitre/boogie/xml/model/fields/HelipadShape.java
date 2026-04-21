package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum HelipadShape {
  CIRCLE,
  SQUARE_OR_RECTANGLE,
  RUNWAY,
  UNDEFINED;
  public static final Set<String> VALID = Arrays.stream(HelipadShape.values()).map(HelipadShape::name).collect(Collectors.toUnmodifiableSet());
}
