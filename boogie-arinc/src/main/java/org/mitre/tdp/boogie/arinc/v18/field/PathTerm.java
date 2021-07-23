package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The Path and Termination defines the path geometry for a single record of an ATC terminal procedure.
 */
public final class PathTerm implements FieldSpec<org.mitre.tdp.boogie.PathTerm> {

  @Override
  public int fieldLength() {
    return 2;
  }

  @Override
  public String fieldCode() {
    return "5.21";
  }

  private static final Set<String> allowedPathTerms = Arrays.stream(org.mitre.tdp.boogie.PathTerm.values())
      .map(org.mitre.tdp.boogie.PathTerm::name)
      .collect(Collectors.toSet());

  @Override
  public Optional<org.mitre.tdp.boogie.PathTerm> apply(String fieldValue) {
    return Optional.of(fieldValue).filter(allowedPathTerms::contains).map(org.mitre.tdp.boogie.PathTerm::valueOf);
  }
}
