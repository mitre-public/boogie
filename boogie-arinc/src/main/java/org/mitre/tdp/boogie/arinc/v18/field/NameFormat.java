package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.ArincStrings.toEnumValue;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The “Name Format Indicator” field is used to describe the format of the “Waypoint Name/Description” field (5.43). This field
 * will be formatted according to the rules described in Chapter 7 of this Specification, Waypoint Naming Conventions.
 */
public final class NameFormat implements FieldSpec<String> {

  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.196";
  }

  @Override
  public Optional<String> apply(String fieldValue) {
    return Optional.of(fieldValue).filter(s -> s.length() == 3)
        .map(s -> Column1.SPEC.apply(s).map(Column1::name).orElse(" ")
            .concat(Column2.SPEC.apply(s).map(Column2::name).orElse(" "))
            .concat(Column3.SPEC.apply(s).map(Column3::name).orElse(" ")));
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
    public Optional<Column1> apply(String fieldValue) {
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
    public Optional<Column2> apply(String fieldValue) {
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
    public Optional<Column3> apply(String fieldValue) {
      return toEnumValue(fieldValue.substring(2, 3), Column3.class);
    }
  }
}
