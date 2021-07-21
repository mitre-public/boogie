package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.ArincStrings.toEnumValue;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The “Waypoint Type” field defines both the “type” and function of IFR waypoints and also define a waypoint as being VFR.
 */
public final class WaypointType implements FieldSpec<String> {

  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.42";
  }

  @Override
  public Optional<String> apply(String fieldValue) {
    return Optional.of(fieldValue).filter(s -> s.length() == 3)
        .map(s -> new StringBuilder()
            .append(Column1.SPEC.apply(s).map(Column1::name).orElse(" "))
            .append(Column2.SPEC.apply(s).map(Column2::name).orElse(" "))
            .append(Column3.SPEC.apply(s).map(Column3::name).orElse(" "))
            .toString());
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
    public Optional<Column1> apply(String fieldValue) {
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
    public Optional<Column2> apply(String fieldValue) {
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
    public Optional<Column3> apply(String fieldValue) {
      return toEnumValue(fieldValue.substring(2, 3), Column3.class);
    }
  }
}
