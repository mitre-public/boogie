package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.ArincStrings.toEnumValue;
import static org.mitre.tdp.boogie.arinc.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * Fixes are located at positions significant to navigation in the Enroute, Terminal Area and Approach Procedure path definitions.
 * The “Waypoint Description Code” field enables that significance or function of a fix at a specific location in a route to be
 * identified. The field provides information on the type of fix. As a single fix can be used in different route structures and
 * multiple times within a given structure, the field provides the function for each occurrence of a fix.
 */
public final class WaypointDescription implements FieldSpec<String>, FilterTrimEmptyInput<String> {

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.17";
  }

  @Override
  public String parseValue(String fieldValue) {
    checkSpec(this, fieldValue, () -> Column1.SPEC.parse(fieldValue));
    checkSpec(this, fieldValue, () -> Column2.SPEC.parse(fieldValue));
    checkSpec(this, fieldValue, () -> Column3.SPEC.parse(fieldValue));
    checkSpec(this, fieldValue, () -> Column4.SPEC.parse(fieldValue));
    return fieldValue;
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
    public boolean filterInput(String fieldValue) {
      return fieldValue.substring(0, 1).trim().isEmpty();
    }

    @Override
    public Column1 parseValue(String fieldValue) {
      return toEnumValue(fieldValue.substring(0, 1), Column1.class);
    }
  }

  enum Column2 implements FieldSpec<Column2>, FilterTrimEmptyInput<Column2> {
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
    public boolean filterInput(String fieldValue) {
      return fieldValue.substring(1, 2).trim().isEmpty();
    }

    @Override
    public Column2 parseValue(String fieldValue) {
      return toEnumValue(fieldValue.substring(1, 2), Column2.class);
    }
  }

  enum Column3 implements FieldSpec<Column3>, FilterTrimEmptyInput<Column3> {
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
    public boolean filterInput(String fieldValue) {
      return fieldValue.substring(2, 3).trim().isEmpty();
    }

    @Override
    public Column3 parseValue(String fieldValue) {
      return toEnumValue(fieldValue.substring(2, 3), Column3.class);
    }
  }

  enum Column4 implements FieldSpec<Column4>, FilterTrimEmptyInput<Column4> {
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
    public boolean filterInput(String fieldValue) {
      return fieldValue.substring(3).trim().isEmpty();
    }

    @Override
    public Column4 parseValue(String fieldValue) {
      return toEnumValue(fieldValue.substring(3), Column4.class);
    }
  }
}
