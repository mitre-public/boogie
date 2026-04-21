package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum TchValueIndicator {
  TCH_OF_ELECTRIC_GLIDE_SLOPE,
  TCH_RNAV_TO_RUNWAY,
  TCH_VGSI,
  TCH_DEFAULTED;
  public static final Set<String> VALID = Arrays.stream(TchValueIndicator.values()).map(TchValueIndicator::name).collect(Collectors.toUnmodifiableSet());
}
