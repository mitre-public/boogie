package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum IlsBackCourse {
  USABLE,
  UNUSABLE,
  RESTRICTED,
  UNDEFINED;
  public static final Set<String> VALID = Arrays.stream(IlsBackCourse.values()).map(IlsBackCourse::name).collect(Collectors.toUnmodifiableSet());
}
