package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum NdbNavaidIfMarkerInfo {
  INNER_MARKER,
  MIDDLE_MARKER,
  OUTER_MARKER,
  BACK_MARKER;
  public static final Set<String> VALID = Arrays.stream(NdbNavaidIfMarkerInfo.values()).map(NdbNavaidIfMarkerInfo::name).collect(Collectors.toUnmodifiableSet());
}
