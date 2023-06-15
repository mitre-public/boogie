package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The “ICAO Code” field permits records to be categorized geographically within the limits of the categorization performed
 * by the {@link CustomerAreaCode} field.
 * <br>
 * e.g. K1, K7, PA, MM, EG, UT
 * <br>
 * The region code is generally the combination of the country code (e.g. K for USA) with some secondary notation indicating
 * a distinct regions within that area. Note because everyone hates everyone that the single character country code is not the
 * same as the {@link CustomerAreaCode#boundaryCode()} because why would it be.
 * <br>
 * https://upload.wikimedia.org/wikipedia/commons/3/3b/ICAO-countries.png
 */
public final class IcaoRegion implements FieldSpec<String> {

  @Override
  public int fieldLength() {
    return 2;
  }

  @Override
  public String fieldCode() {
    return "5.14";
  }


  @Override
  public Optional<String> apply(String fieldValue) {
    return Optional.of(fieldValue).filter(s -> !s.isBlank()).filter(this::isValid).map(String::trim);
  }

  /**
   * The vast majority of waypoints will have a two-character ICAO region (as is standard) but a few thousand well-named fixes
   * within CIFP <i>don't</i> and instead have single-character ones. In those cases however that is how those fixes are referenced
   * in the other nav datasets (e.g. in procedure legs).
   */
  private boolean isValid(String region) {
    return (alphaNumeric(region, 0) && (alphaNumeric(region, 1) || Character.isWhitespace(region.charAt(1))));
  }

  private boolean alphaNumeric(String s, int offset) {
    char c = s.charAt(offset);
    return Character.isDigit(c) || Character.isAlphabetic(c);
  }
}
