package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum VorRangePower {
  NM_25_FEET_12000,
  NM_40_FEET_18000,
  NM_130_FEET_60000_LEGACY,
  NM_70_FEET_18000,
  NM_130_FEET_60000_VOR_MON;
  public static final Set<String> VALID = Arrays.stream(VorRangePower.values()).map(VorRangePower::name).collect(Collectors.toUnmodifiableSet());
}
