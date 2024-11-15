package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * Definition/Description: The “Record Type” field content indicates whether the record data are “standard,” i.e., suitable for
 * universal application, or “tailored,” i.e. included  on  the  master  file  for  a  single  user’s  specific  purpose (Section
 * 1.2 of this Specification refers).
 */
public enum RecordType implements FieldSpec<RecordType> {
  SPEC,
  /**
   * Standard record types used across all aircraft and airlines.
   */
  S,
  /**
   * Tailored records generally for use by a particular airline (company routes, etc.).
   */
  T;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.2";
  }

  private static final Set<String> validNames = Arrays.stream(RecordType.values())
      .filter(d -> !SPEC.equals(d))
      .map(RecordType::name)
      .collect(Collectors.toSet());

  @Override
  public Optional<RecordType> apply(String fieldValue) {
    return Optional.of(fieldValue).filter(validNames::contains).map(RecordType::valueOf);
  }
}
