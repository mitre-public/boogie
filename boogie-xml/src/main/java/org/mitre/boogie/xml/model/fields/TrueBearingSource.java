package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum TrueBearingSource {
  OFFICIAL,
  OTHER;
  public static final Set<String> VALID = Arrays.stream(TrueBearingSource.values()).map(TrueBearingSource::name).collect(Collectors.toUnmodifiableSet());
}
