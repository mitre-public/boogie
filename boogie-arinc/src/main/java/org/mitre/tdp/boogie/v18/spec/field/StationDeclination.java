package org.mitre.tdp.boogie.v18.spec.field;

import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.mitre.tdp.boogie.utils.Preconditions.checkSpec;

import java.util.Arrays;
import java.util.List;

import org.mitre.tdp.boogie.v18.spec.common.FilterTrimEmptyInput;
import org.mitre.tdp.boogie.v18.spec.common.FreeFormString;

/**
 * For VHF NAVAIDS, the “Station Declination” field contains the angular difference between true north and the zero degree radial of
 * the NAVAID at the time the NAVAID was last site checked. For ILS localizers, the field contains the angular difference between true
 * north and magnetic north at the localizer antenna site at the time the magnetic bearing of the localizer course was established.
 */
public class StationDeclination implements FreeFormString, FilterTrimEmptyInput<String> {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.66";
  }

  @Override
  public String parseValue(String fieldValue) {
    checkSpec(this, fieldValue, validCharacters().contains(fieldValue.substring(0, 1)));
    checkSpec(this, fieldValue, isNumeric(fieldValue.substring(1)));
    return fieldValue;
  }

  /**
   * The collection of valid declination modifiers.
   */
  public List<String> validCharacters() {
    return Arrays.asList("E", "W", "T", "G");
  }
}
