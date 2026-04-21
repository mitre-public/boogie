package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum HelicopterPerformanceReq {
  MULTI_ENGINE,
  SINGLE_ENGINE_ONLY,
  UNKNOWN;
  public static final Set<String> VALID = Arrays.stream(HelicopterPerformanceReq.values()).map(HelicopterPerformanceReq::name).collect(Collectors.toUnmodifiableSet());
}
