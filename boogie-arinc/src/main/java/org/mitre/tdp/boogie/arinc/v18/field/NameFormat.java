package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.ArincStrings.toEnumValue;
import static org.mitre.tdp.boogie.arinc.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The “Name Format Indicator” field is used to describe the format of the “Waypoint Name/Description” field (5.43). This field
 * will be formatted according to the rules described in Chapter 7 of this Specification, Waypoint Naming Conventions.
 */
public class NameFormat implements FieldSpec<String>, FilterTrimEmptyInput<String> {

  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.196";
  }

  @Override
  public String parseValue(String fieldValue) {
    checkSpec(this, fieldValue, () -> Column1.SPEC.parse(fieldValue));
    checkSpec(this, fieldValue, () -> Column2.SPEC.parse(fieldValue));
    checkSpec(this, fieldValue, () -> Column3.SPEC.parse(fieldValue));
    return fieldValue;
  }

  enum Column1 implements FieldSpec<Column1> {
    SPEC, A, B, D, F, H, I, L, M, N, P, Q, R, T, U;

    @Override
    public int fieldLength() {
      return 3;
    }

    @Override
    public String fieldCode() {
      return "5.196a";
    }

    @Override
    public boolean filterInput(String fieldValue) {
      return fieldValue.substring(0, 1).trim().isEmpty();
    }

    @Override
    public Column1 parseValue(String fieldValue) {
      return toEnumValue(fieldValue.substring(0, 1), Column1.class);
    }
  }

  enum Column2 implements FieldSpec<Column2> {
    SPEC, O, M;

    @Override
    public int fieldLength() {
      return 3;
    }

    @Override
    public String fieldCode() {
      return "5.196b";
    }

    @Override
    public boolean filterInput(String fieldValue) {
      return fieldValue.substring(1, 2).trim().isEmpty();
    }

    @Override
    public Column2 parseValue(String fieldValue) {
      return toEnumValue(fieldValue.substring(1, 2), Column2.class);
    }
  }

  enum Column3 implements FieldSpec<Column3> {
    SPEC;

    @Override
    public int fieldLength() {
      return 3;
    }

    @Override
    public String fieldCode() {
      return "5.196c";
    }

    @Override
    public boolean filterInput(String fieldValue) {
      return fieldValue.substring(2, 3).trim().isEmpty();
    }

    @Override
    public Column3 parseValue(String fieldValue) {
      return toEnumValue(fieldValue.substring(2, 3), Column3.class);
    }
  }
}
