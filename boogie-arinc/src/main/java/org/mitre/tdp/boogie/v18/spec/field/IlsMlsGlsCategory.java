package org.mitre.tdp.boogie.v18.spec.field;

import static org.mitre.tdp.boogie.utils.Preconditions.checkSpec;

import java.util.Arrays;
import java.util.List;

import org.mitre.tdp.boogie.v18.spec.common.FilterTrimEmptyInput;
import org.mitre.tdp.boogie.v18.spec.common.FreeFormString;

/**
 * The Localizer/MLS/GLS Performance Categories have established operating minimums and are listed as Category I, II, and III.
 * The level of Performance Category does not imply that permission exists to use the facility for landing guidance to that level
 * and does not limit minimal using designated classification. This field is also used to define the classification, non-ILS/MLS/GLS,
 * and localizer installation such as IGS, LDA, or SDF. As used in the runway record, there are two fields, one labeled Localizer/MLS/GLS
 * Category/Classification and the other labeled Second Localizer/MLS/GLS Category/Classification.
 */
public class IlsMlsGlsCategory implements FreeFormString, FilterTrimEmptyInput<String> {
  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.80";
  }

  @Override
  public String parseValue(String fieldString) {
    checkSpec(this, fieldString, allowedCategories().contains(fieldString));
    return fieldString;
  }

  /**
   * The collection of all allowed categories for ILS systems.
   */
  public List<String> allowedCategories() {
    return Arrays.asList("0", "1", "2", "3", "I", "L", "A", "S", "F");
  }
}
