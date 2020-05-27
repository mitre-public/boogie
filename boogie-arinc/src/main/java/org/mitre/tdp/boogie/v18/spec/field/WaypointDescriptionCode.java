package org.mitre.tdp.boogie.v18.spec.field;

import static org.mitre.tdp.boogie.utils.ArincStrings.toEnumValue;
import static org.mitre.tdp.boogie.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.FieldSpec;

/**
 * Fixes are located at positions significant to navigation in the Enroute, Terminal Area and Approach Procedure path definitions.
 * The “Waypoint Description Code” field enables that significance or function of a fix at a specific location in a route to be
 * identified. The field provides information on the type of fix. As a single fix can be used in different route structures and
 * multiple times within a given structure, the field provides the function for each occurrence of a fix.
 */
public class WaypointDescriptionCode implements FieldSpec<String> {

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.17";
  }

  @Override
  public String parse(String fieldString) {
    checkSpec(this, fieldString, () -> Column1.SPEC.parse(fieldString));
    checkSpec(this, fieldString, () -> Column2.SPEC.parse(fieldString));
    checkSpec(this, fieldString, () -> Column3.SPEC.parse(fieldString));
    checkSpec(this, fieldString, () -> Column4.SPEC.parse(fieldString));
    return fieldString;
  }

  enum Column1 implements FieldSpec<Column1> {
    SPEC, A, E, F, G, H, N, P, T, V;

    @Override
    public int fieldLength() {
      return 4;
    }

    @Override
    public String fieldCode() {
      return "5.17a";
    }

    @Override
    public Column1 parse(String fieldString) {
      return toEnumValue(fieldString.substring(0, 1), Column1.class);
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
    public Column2 parse(String fieldString) {
      return toEnumValue(fieldString.substring(1, 2), Column2.class);
    }
  }

  enum Column3 implements FieldSpec<Column3> {
    SPEC, A, B, C, G, M, P, S;

    @Override
    public int fieldLength() {
      return 4;
    }

    @Override
    public String fieldCode() {
      return "5.17c";
    }

    @Override
    public Column3 parse(String fieldString) {
      return toEnumValue(fieldString.substring(2, 3), Column3.class);
    }
  }

  enum Column4 implements FieldSpec<Column4> {
    SPEC, A, B, C, D, E, F, H, I, M;

    @Override
    public int fieldLength() {
      return 4;
    }

    @Override
    public String fieldCode() {
      return "5.17d";
    }

    @Override
    public Column4 parse(String fieldString) {
      return toEnumValue(fieldString.substring(3), Column4.class);
    }
  }
}
