package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.ArincStrings.toEnumValue;
import static org.mitre.tdp.boogie.arinc.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The “Waypoint Type” field defines both the “type” and function of IFR waypoints and also define a waypoint as being VFR.
 */
public final class WaypointType implements FreeFormString, FilterTrimEmptyInput<String> {

  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.42";
  }

  @Override
  public String parseValue(String fieldValue) {
    checkSpec(this, fieldValue, () -> Column1.SPEC.parse(fieldValue));
    checkSpec(this, fieldValue, () -> Column2.SPEC.parse(fieldValue));
    checkSpec(this, fieldValue, () -> Column3.SPEC.parse(fieldValue));
    return fieldValue;
  }

  enum Column1 implements FieldSpec<Column1> {
    SPEC, C, I, N, R, U, V, W, A, M, O;

    @Override
    public int fieldLength() {
      return 3;
    }

    @Override
    public String fieldCode() {
      return "5.42a";
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
    SPEC, A, B, C, D, E, F, I, K, L, M, N, O, P, S, U, V, W;

    @Override
    public int fieldLength() {
      return 3;
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

  enum Column3 implements FieldSpec<Column3> {
    SPEC, D, E, F, Z;

    @Override
    public int fieldLength() {
      return 3;
    }

    @Override
    public String fieldCode() {
      return "5.42c";
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
