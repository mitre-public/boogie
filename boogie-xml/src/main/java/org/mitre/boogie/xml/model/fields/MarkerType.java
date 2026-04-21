package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum MarkerType {
  IM,
  MM,
  OM,
  BM;
  public static final Set<String> VALID = Arrays.stream(MarkerType.values()).map(MarkerType::name).collect(Collectors.toUnmodifiableSet());
}
