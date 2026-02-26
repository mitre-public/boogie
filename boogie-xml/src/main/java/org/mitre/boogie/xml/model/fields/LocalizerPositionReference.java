package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum LocalizerPositionReference {
  BEYOND_STOP_END,
  BEFORE_APPROACH_END,
  OFF_TO_SIDE;
  public static final Set<String> VALID = Arrays.stream(LocalizerPositionReference.values()).map(LocalizerPositionReference::name).collect(Collectors.toUnmodifiableSet());
}
