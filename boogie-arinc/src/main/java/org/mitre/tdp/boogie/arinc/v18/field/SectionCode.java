package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

import com.google.common.collect.ImmutableBiMap;

/**
 * Definition/Description: The “Section Code” field defines the major section of the navigation system data base in which the
 * record resides.
 */
public enum SectionCode implements FieldSpec<SectionCode> {
  SPEC,
  /**
   * Grid MORA.
   */
  A,
  /**
   * Navaid.
   */
  D,
  /**
   * Enroute.
   */
  E,
  /**
   * Heliport.
   */
  H,
  /**
   * Tables.
   */
  T,
  /**
   * Company Routes.
   */
  R,
  /**
   * Airport.
   */
  P,
  /**
   * Airspace.
   */
  U;

  private static final ImmutableBiMap<String, SectionCode> lookup = new ImmutableBiMap.Builder<String, SectionCode>()
      .put(SectionCode.A.name(), SectionCode.A)
      .put(SectionCode.D.name(), SectionCode.D)
      .put(SectionCode.E.name(), SectionCode.E)
      .put(SectionCode.H.name(), SectionCode.H)
      .put(SectionCode.T.name(), SectionCode.T)
      .put(SectionCode.R.name(), SectionCode.R)
      .put(SectionCode.P.name(), SectionCode.P)
      .put(SectionCode.U.name(), SectionCode.U)
      .build();

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.4";
  }

  @Override
  public Optional<SectionCode> apply(String fieldValue) {
    return Optional.ofNullable(lookup.get(fieldValue));
  }
}
