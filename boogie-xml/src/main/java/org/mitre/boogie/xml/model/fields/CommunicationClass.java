package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum CommunicationClass {
  LIRC,
  LIRI,
  USVC,
  ASVC,
  ATCF,
  GNDF,
  AOTF;
  public static final Set<String> VALID = Arrays.stream(CommunicationClass.values()).map(CommunicationClass::name).collect(Collectors.toUnmodifiableSet());
}
