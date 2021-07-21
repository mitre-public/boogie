package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.ArincStrings.toEnumValue;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The “Navaid Class” field provides information in coded format on the type of navaid, the use-able range or assigned
 * output power of the navaid, information carried on the navaid signal and collocation of navaids in both an electronic
 * and aeronautical sense. The field is made up of five columns of codes that define this information.
 */
public final class NavaidClass implements FieldSpec<String> {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.35";
  }

  @Override
  public Optional<String> apply(String fieldValue) {
    return Optional.of(fieldValue).filter(s -> s.length() == 5)
        .map(s -> new StringBuilder()
            .append(Type1.SPEC.apply(s).map(Type1::name).orElse(" "))
            .append(Type2.SPEC.apply(s).map(Type2::name).orElse(" "))
            .append(RangePower.SPEC.apply(s).map(RangePower::name).orElse(" "))
            .append(AdditionalInfo.SPEC.apply(s).map(AdditionalInfo::name).orElse(" "))
            .append(Collocation.SPEC.apply(s).map(Collocation::name).orElse(" "))
            .toString());
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
    public Optional<Type1> apply(String fieldValue) {
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
    public Optional<Type2> apply(String fieldValue) {
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
    public Optional<RangePower> apply(String fieldValue) {
      String subs = fieldValue.substring(2, 3);
      return Optional.of(subs).flatMap(s -> toEnumValue(s, RangePower.class));
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
    public Optional<AdditionalInfo> apply(String fieldValue) {
      String subs = fieldValue.substring(3, 4);
      return Optional.of(subs).flatMap(s -> toEnumValue(s, AdditionalInfo.class));
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
    public Optional<Collocation> apply(String fieldValue) {
      String subs = fieldValue.substring(4);
      return Optional.of(subs).flatMap(s -> toEnumValue(s, Collocation.class));
    }
  }
}
