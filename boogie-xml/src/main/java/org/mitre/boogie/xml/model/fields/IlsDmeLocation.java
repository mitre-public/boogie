package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum IlsDmeLocation {
  NOT_COLLOCATED,
  COLLOCATED_LOCALIZER,
  COLLOCATED_GLIDE_SLOPE;
  public static final Set<String> VALID = Arrays.stream(IlsDmeLocation.values()).map(IlsDmeLocation::name).collect(Collectors.toUnmodifiableSet());
}
