package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

import com.google.common.collect.ImmutableSet;

/**
 * The Localizer/MLS/GLS Performance Categories have established operating minimums and are listed as Category I, II, and III.
 * The level of Performance Category does not imply that permission exists to use the facility for landing guidance to that level
 * and does not limit minimal using designated classification. This field is also used to define the classification, non-ILS/MLS/GLS,
 * and localizer installation such as IGS, LDA, or SDF. As used in the runway record, there are two fields, one labeled Localizer/MLS/GLS
 * Category/Classification and the other labeled Second Localizer/MLS/GLS Category/Classification.
 */
public final class IlsMlsGlsCategory implements FieldSpec<String> {

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.80";
  }

  static final ImmutableSet<String> allowedCategories = ImmutableSet.of("0", "1", "2", "3", "I", "L", "A", "S", "F");

  @Override
  public Optional<String> apply(String fieldValue) {
    return Optional.of(fieldValue).map(String::trim).filter(allowedCategories::contains);
  }
}
