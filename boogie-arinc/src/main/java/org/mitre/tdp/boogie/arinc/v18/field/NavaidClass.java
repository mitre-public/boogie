package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.ArincStrings.isBlank;
import static org.mitre.tdp.boogie.arinc.utils.ArincStrings.toEnumValue;
import static org.mitre.tdp.boogie.arinc.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The “Navaid Class” field provides information in coded format on the type of navaid, the use-able range or assigned
 * output power of the navaid, information carried on the navaid signal and collocation of navaids in both an electronic
 * and aeronautical sense. The field is made up of five columns of codes that define this information.
 */
public final class NavaidClass implements FieldSpec<String>, FilterTrimEmptyInput<String> {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.35";
  }

  @Override
  public String parseValue(String fieldValue) {
    checkSpec(this, fieldValue, () -> Type1.SPEC.parse(fieldValue));
    checkSpec(this, fieldValue, () -> Type2.SPEC.parse(fieldValue));
    checkSpec(this, fieldValue, () -> RangePower.SPEC.parse(fieldValue));
    checkSpec(this, fieldValue, () -> AdditionalInfo.SPEC.parse(fieldValue));
    checkSpec(this, fieldValue, () -> Collocation.SPEC.parse(fieldValue));
    return fieldValue;
  }

  public enum Type1 implements FieldSpec<Type1> {
    SPEC, V, H, S, M;

    @Override
    public int fieldLength() {
      return 5;
    }

    @Override
    public String fieldCode() {
      return "5.35a";
    }

    @Override
    public Type1 parseValue(String fieldValue) {
      return toEnumValue(fieldValue.substring(0, 1), Type1.class);
    }
  }

  public enum Type2 implements FieldSpec<Type2> {
    SPEC, D, T, I, M, O, C, N, P;

    @Override
    public int fieldLength() {
      return 5;
    }

    @Override
    public String fieldCode() {
      return "5.35b";
    }

    @Override
    public Type2 parseValue(String fieldValue) {
      return toEnumValue(fieldValue.substring(1, 2), Type2.class);
    }
  }

  public enum RangePower implements FieldSpec<RangePower> {
    SPEC, T, U, H, M, L, C, BLANK;

    @Override
    public int fieldLength() {
      return 5;
    }

    @Override
    public String fieldCode() {
      return "5.35c";
    }

    @Override
    public RangePower parseValue(String fieldValue) {
      String subs = fieldValue.substring(2, 3);
      return isBlank.test(subs) ? RangePower.BLANK : toEnumValue(subs, RangePower.class);
    }
  }

  public enum AdditionalInfo implements FieldSpec<AdditionalInfo> {
    SPEC, D, A, B, W, BLANK;

    @Override
    public int fieldLength() {
      return 5;
    }

    @Override
    public String fieldCode() {
      return "5.35d";
    }

    @Override
    public AdditionalInfo parseValue(String fieldValue) {
      String subs = fieldValue.substring(3, 4);
      return isBlank.test(subs) ? AdditionalInfo.BLANK : toEnumValue(subs, AdditionalInfo.class);
    }
  }

  public enum Collocation implements FieldSpec<Collocation> {
    SPEC, B, A, N, BLANK;

    @Override
    public int fieldLength() {
      return 5;
    }

    @Override
    public String fieldCode() {
      return "5.35e";
    }

    @Override
    public Collocation parseValue(String fieldValue) {
      String subs = fieldValue.substring(4);
      return isBlank.test(subs) ? Collocation.BLANK : toEnumValue(subs, Collocation.class);
    }
  }
}
