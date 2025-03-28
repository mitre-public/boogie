package org.mitre.tdp.boogie.arinc.v21.field;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The Surface Type field defines the predominant
 * surface type of the runway/helipad described in the record.
 */
public enum SurfaceType implements FieldSpec<SurfaceType> {
  SPEC,
  /**
   * Asphalt
   */
  ASPH,
  /**
   * Asphalt and grass
   */
  ASGR,
  /**
   * Bituminous tar or asphalt and/or oil or bitumen bound, mix in place surfaces (often called "earth cement")
   */
  BITU,
  /**
   * Brick, laid, or mortared
   */
  BRCK,
  /**
   * Clay
   */
  CLAY,
  /**
   * Concrete
   */
  CONC,
  /**
   * Concrete and asphalt
   */
  COAS,
  /**
   * Concrete and grass
   */
  COGS,
  /**
   * Coral
   */
  CORL,
  /**
   * Dirt
   */
  DIRT,
  /**
   * Grass
   */
  GRAS,
  /**
   * Gravel
   */
  GRVL,
  /**
   * Ice
   */
  ICE,
  /**
   * Laterite - a high iron clay formed in tropical areas
   */
  LATE,
  /**
   * A macadam or tarmac surface consisting of water bound crushed rock
   */
  MACA,
  /**
   * Landing mat portable system usually made of aluminum
   */
  MATS,
  /**
   * A protective laminate usually made of rubber
   */
  MEMB,
  /**
   * Metal - steel, aluminium
   */
  META,
  /**
   * Non-Bituminous Mix
   */
  MIX,
  /**
   * Other
   */
  OTHR,
  /**
   * Paved (generic hard surface)
   */
  PAVD,
  /**
   * Pierced steel planking
   */
  PSP,
  /**
   * Sand
   */
  SAND,
  /**
   * Sealed
   */
  SELD,
  /**
   * Silt
   */
  SILT,
  /**
   * SNOW
   */
  SNOW,
  /**
   * Soil earth in general
   */
  SOIL,
  /**
   * Tarmac
   */
  TARM,
  /**
   * Treated
   */
  TRTD,
  /**
   * Turf
   */
  TURF,
  /**
   * Unkown
   */
  UNKN;


  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.302";
  }

  public static final Set<String> VALID = Arrays.stream(SurfaceType.values())
      .filter(i -> !SPEC.equals(i))
      .map(SurfaceType::name)
      .collect(Collectors.toSet());

  @Override
  public Optional<SurfaceType> apply(String s) {
    return Optional.ofNullable(s)
        .map(String::trim)
        .filter(VALID::contains)
        .map(SurfaceType::valueOf);
  }
}
