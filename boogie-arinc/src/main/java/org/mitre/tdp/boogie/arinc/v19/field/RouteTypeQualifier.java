package org.mitre.tdp.boogie.arinc.v19.field;

import static com.google.common.collect.Sets.newHashSet;

import java.util.Optional;
import java.util.Set;

import org.mitre.tdp.boogie.arinc.FieldSpec;

import com.google.common.collect.Sets;

public final class RouteTypeQualifier implements FieldSpec<String> {

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.7a";
  }

  @Override
  public Optional<String> apply(String fieldValue) {
    return Optional.of(fieldValue).map(String::trim).filter(allowedCodes::contains);
  }

  /**
   * The collection of allowed route type qualifier codes.
   */
  private static final Set<String> allowedCodes = Sets.union(
      // V18 codes
      newHashSet("A", "B", "E", "C", "S", "D", "J", "L", "N", "P", "R", "T", "U", "V", "W"),
      // V19a codes
      newHashSet("H", "F", "I")
  );
}
