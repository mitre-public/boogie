package org.mitre.tdp.boogie.v18.spec.field;

import static org.mitre.tdp.boogie.utils.ArincStrings.isBlank;
import static org.mitre.tdp.boogie.utils.ArincStrings.toEnumValue;
import static org.mitre.tdp.boogie.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.FieldSpec;

/**
 * The “Navaid Class” field provides information in coded format on the type of navaid, the use-able range or assigned
 * output power of the navaid, information carried on the navaid signal and collocation of navaids in both an electronic
 * and aeronautical sense. The field is made up of five columns of codes that define this information.
 */
public class NavaidClass implements FieldSpec<String> {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.35";
  }

  @Override
  public String parse(String fieldString) {
    checkSpec(this, fieldString, () -> Type1.SPEC.parse(fieldString));
    checkSpec(this, fieldString, () -> Type2.SPEC.parse(fieldString));
    checkSpec(this, fieldString, () -> RangePower.SPEC.parse(fieldString));
    checkSpec(this, fieldString, () -> AdditionalInfo.SPEC.parse(fieldString));
    checkSpec(this, fieldString, () -> Collocation.SPEC.parse(fieldString));
    return fieldString;
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
    public Type1 parse(String fieldString) {
      return toEnumValue(fieldString.substring(0, 1), Type1.class);
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
    public Type2 parse(String fieldString) {
      return toEnumValue(fieldString.substring(1, 2), Type2.class);
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
    public RangePower parse(String fieldString) {
      String subs = fieldString.substring(2, 3);
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
    public AdditionalInfo parse(String fieldString) {
      String subs = fieldString.substring(3, 4);
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
    public Collocation parse(String fieldString) {
      String subs = fieldString.substring(4);
      return isBlank.test(subs) ? Collocation.BLANK : toEnumValue(subs, Collocation.class);
    }
  }
}
