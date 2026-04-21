package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum RadarService {
  PRIMARY_OR_SECONDARY,
  NO,
  UNKNOWN;
  public static final Set<String> VALID = Arrays.stream(RadarService.values()).map(RadarService::name).collect(Collectors.toUnmodifiableSet());
}
