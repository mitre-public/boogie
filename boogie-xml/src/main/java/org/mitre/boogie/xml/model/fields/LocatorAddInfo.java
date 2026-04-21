package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum LocatorAddInfo {
  NO_VOICE_ON_FREQUENCY,
  VOICE_ON_FREQUENCY;
  public static final Set<String> VALID = Arrays.stream(LocatorAddInfo.values()).map(LocatorAddInfo::name).collect(Collectors.toUnmodifiableSet());
}
