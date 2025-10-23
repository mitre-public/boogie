package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;

public enum ProductionTestFlag implements FieldSpec<ProductionTestFlag> {
  SPEC,
  /**
   * Production
   */
  P,
  /**
   * Test
   */
  T;

  public static final Set<String> VALID = Arrays.stream(ProductionTestFlag.values())
      .filter(i -> !SPEC.equals(i))
      .map(ProductionTestFlag::name)
      .collect(Collectors.toSet());

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "6.2.1d";
  }

  @Override
  public Optional<ProductionTestFlag> apply(String s) {
    return Optional.ofNullable(s)
        .filter(VALID::contains)
        .map(ProductionTestFlag::valueOf);
  }
}
