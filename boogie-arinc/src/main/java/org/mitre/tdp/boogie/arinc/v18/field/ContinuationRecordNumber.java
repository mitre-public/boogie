package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * When it is not possible to store all the information needed on a record within the 132 columns of the record itself, the
 * so-called Primary Record; one or more continuation records may be used.
 * <br>
 * e.g. [0-9][A-Z]
 */
public final class ContinuationRecordNumber implements FieldSpec<String> {

  private static final Predicate<String> numberRegex = Pattern.compile("[0-9A-Z]").asPredicate();

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.16";
  }

  @Override
  public Optional<String> apply(String fieldValue) {
    return Optional.of(fieldValue).map(String::trim).filter(numberRegex);
  }
}
