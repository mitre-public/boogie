package org.mitre.tdp.boogie.dafif.model;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum DafifFileType {
  ADD_RUNWAY,
  AIRPORT,
  ATS_SERVICE,
  ILS,
  NAVAID,
  RUNWAY,
  TERMINAL_PARENT,
  TERMINAL_SEGMENT,
  WAYPOINT;

  public static final Set<String> VALID = Arrays.stream(DafifFileType.values()).map(DafifFileType::name).collect(Collectors.toSet());
}
