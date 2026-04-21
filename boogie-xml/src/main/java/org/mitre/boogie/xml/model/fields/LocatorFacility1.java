package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum LocatorFacility1 {
  NDB,
  SABH,
  MARINE_BEACON;
  public static final Set<String> VALID = Arrays.stream(LocatorFacility1.values()).map(LocatorFacility1::name).collect(Collectors.toUnmodifiableSet());
}
