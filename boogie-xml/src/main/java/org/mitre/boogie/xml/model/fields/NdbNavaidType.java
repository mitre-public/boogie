package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum NdbNavaidType {
  NDB,
  SABH,
  MARINE_BEACON;
  public static final Set<String> VALID = Arrays.stream(NdbNavaidType.values()).map(NdbNavaidType::name).collect(Collectors.toUnmodifiableSet());
}
