package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.BooleanStringParser;

/**
 * The “Daylight” Time Indicator” field is used to indicate if the airport observes Daylight or Summer time when such time changes
 * are in effect for the country or state the airport resides in.
 * <br>
 * he field will contain the alpha character “Y” if airport observes Daylight or Summer time. The field will contain the alpha
 * character “N” if the airport does not observe Daylight time or if it is unknown.
 * <br>
 * For most sources we see in the real world this is often not populated... lol. In accordance with the spec though null==unknown
 * and unknown=false, so no it is.
 */
public final class DaylightTimeIndicator implements FieldSpec<Boolean> {

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.179";
  }

  @Override
  public Optional<Boolean> apply(String fieldValue) {
    return Optional.of(fieldValue).map(BooleanStringParser.INSTANCE::test);
  }
}
