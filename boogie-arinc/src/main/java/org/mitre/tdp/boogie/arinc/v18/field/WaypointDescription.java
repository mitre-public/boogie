package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.ArincStrings.toEnumValue;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * Fixes are located at positions significant to navigation in the Enroute, Terminal Area and Approach Procedure path definitions.
 * The “Waypoint Description Code” field enables that significance or function of a fix at a specific location in a route to be
 * identified. The field provides information on the type of fix. As a single fix can be used in different route structures and
 * multiple times within a given structure, the field provides the function for each occurrence of a fix.
 */
public final class WaypointDescription implements FieldSpec<String> {

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.17";
  }

  @Override
  public Optional<String> apply(String fieldValue) {
    return Optional.of(fieldValue).filter(s -> s.length() == 4)
        .map(s -> new StringBuilder()
            .append(Column1.SPEC.apply(s).map(Column1::name).orElse(" "))
            .append(Column2.SPEC.apply(s).map(Column2::name).orElse(" "))
            .append(Column3.SPEC.apply(s).map(Column3::name).orElse(" "))
            .append(Column4.SPEC.apply(s).map(Column4::name).orElse(" "))
            .toString());
  }

  enum Column1 implements FieldSpec<Column1> {
    SPEC, A, E, F, G, H, N, P, R, T, V;

    @Override
    public int fieldLength() {
      return 4;
    }

    @Override
    public String fieldCode() {
      return "5.17a";
    }

    @Override
    public Optional<Column1> apply(String fieldValue) {
      return toEnumValue(fieldValue.substring(0, 1), Column1.class);
    }
  }

  enum Column2 implements FieldSpec<Column2> {
    SPEC, B, E, U, Y;

    @Override
    public int fieldLength() {
      return 4;
    }

    @Override
    public String fieldCode() {
      return "5.17b";
    }

    @Override
    public Optional<Column2> apply(String fieldValue) {
      return toEnumValue(fieldValue.substring(1, 2), Column2.class);
    }
  }

  enum Column3 implements FieldSpec<Column3> {
    SPEC, A, B, C, G, M, P, R, S;

    @Override
    public int fieldLength() {
      return 4;
    }

    @Override
    public String fieldCode() {
      return "5.17c";
    }

    @Override
    public Optional<Column3> apply(String fieldValue) {
      return toEnumValue(fieldValue.substring(2, 3), Column3.class);
    }
  }

  enum Column4 implements FieldSpec<Column4> {
    SPEC, A, B, C, D, E, F, G, H, I, M, N;

    @Override
    public int fieldLength() {
      return 4;
    }

    @Override
    public String fieldCode() {
      return "5.17d";
    }

    @Override
    public Optional<Column4> apply(String fieldValue) {
      return toEnumValue(fieldValue.substring(3), Column4.class);
    }
  }
}
