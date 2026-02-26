package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum NavaidSynchronization {
  SYNCHRONOUS,
  ASYNCHRONOUS,
  UNKNOWN;
  public static final Set<String> VALID = Arrays.stream(NavaidSynchronization.values()).map(NavaidSynchronization::name).collect(Collectors.toUnmodifiableSet());
}
