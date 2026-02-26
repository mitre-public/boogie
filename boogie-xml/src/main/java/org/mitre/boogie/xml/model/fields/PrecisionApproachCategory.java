package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum PrecisionApproachCategory {
  ILS_LOC_ONLY,
  ILS_MLS_GLS_CAT_1,
  ILS_MLS_GLS_CAT_2,
  ILS_MLS_GLS_CAT_3,
  IGS,
  LDA_GLIDESLOPE,
  LDA_NO_GLIDESLOPE,
  SDF_GLIDESLOPE,
  SDF_NO_GLIDE_SLOPE;
  public static final Set<String> VALID = Arrays.stream(PrecisionApproachCategory.values()).map(PrecisionApproachCategory::name).collect(Collectors.toUnmodifiableSet());
}
