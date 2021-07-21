package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

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
 */
public final class IcaoRegion implements FieldSpec<String> {

  private static final Predicate<String> matcher = Pattern.compile("^([0-9A-Z]{2})$").asPredicate();

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
    return Optional.of(fieldValue).map(String::trim).filter(matcher);
  }
}
