package org.mitre.tdp.boogie.arinc.v18.field;

import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.mitre.tdp.boogie.util.CoordinateParser.sign;
import static org.mitre.tdp.boogie.arinc.utils.Preconditions.checkSpec;

import java.util.Arrays;
import java.util.List;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ArincStrings;

/**
 * For VHF NAVAIDS, the “Station Declination” field contains the angular difference between true north and the zero degree radial of
 * the NAVAID at the time the NAVAID was last site checked. For ILS localizers, the field contains the angular difference between true
 * north and magnetic north at the localizer antenna site at the time the magnetic bearing of the localizer course was established.
 */
public final class StationDeclination implements FieldSpec<Double>, FilterTrimEmptyInput<Double> {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.66";
  }


  @Override
  public boolean filterInput(String rawInput) {
    return FilterTrimEmptyInput.super.filterInput(rawInput) || rawInput.startsWith("T") || rawInput.startsWith("G");
  }

  @Override
  public Double parseValue(String fieldValue) {
    checkSpec(this, fieldValue, validCharacters().contains(fieldValue.substring(0, 1)));
    checkSpec(this, fieldValue, isNumeric(fieldValue.substring(1)));
    checkSpec(this, fieldValue, !fieldValue.startsWith("T"));
    checkSpec(this, fieldValue, !fieldValue.startsWith("G"));
    return sign(fieldValue.substring(0, 1)) * ArincStrings.parseDoubleWithTenths(fieldValue.substring(1));
  }

  /**
   * The collection of valid declination modifiers.
   */
  public List<String> validCharacters() {
    return Arrays.asList("E", "W", "T", "G");
  }
}
