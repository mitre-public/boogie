package org.mitre.tdp.boogie.arinc;

import java.util.Optional;

/**
 * Abstract class representing a string field with trimmable contents - if the string can be trimmed resulting in an empty
 * string then the return of the apply method will be {@link Optional#empty()}.
 */
public abstract class TrimmableString implements FieldSpec<String> {

  public Optional<String> apply(String fieldValue) {
    return Optional.of(fieldValue).map(String::trim).filter(s -> !s.isEmpty()).map(String::intern);
  }
}
