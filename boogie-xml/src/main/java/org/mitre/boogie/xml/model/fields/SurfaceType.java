package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum SurfaceType {
  ASPHALT,
  ASPHALT_AND_GRASS,
  BITUMINOUS_TAR_OR_ASPHALT,
  BRICK_IS_LAID_OR_MORTARED,
  CLAY,
  CONCRETE,
  CONCRETE_AND_ASPHALT,
  CONCRETE_AND_GRASS,
  CORAL,
  DIRT,
  GRASS,
  GRAVEL,
  ICE,
  LATERITE,
  MACADAM,
  LANDING_MAT,
  PROTECTIVE_LAMINATE,
  METAL,
  MIX,
  OTHER,
  PAVED,
  PIERCED_STEEL_PLANKING,
  SAND,
  SEALED,
  SILT,
  SNOW,
  SOIL,
  STONE,
  TARMAC,
  TREATED,
  TURF,
  UNKNOWN,
  UNPAVED,
  WATER;
  public static final Set<String> VALID = Arrays.stream(SurfaceType.values()).map(SurfaceType::name).collect(Collectors.toUnmodifiableSet());
}
