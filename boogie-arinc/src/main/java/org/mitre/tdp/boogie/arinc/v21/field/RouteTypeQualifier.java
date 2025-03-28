package org.mitre.tdp.boogie.arinc.v21.field;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.v21.field.qualifiers.ApproachQualifier1;
import org.mitre.tdp.boogie.arinc.v21.field.qualifiers.ApproachQualifier2;
import org.mitre.tdp.boogie.arinc.v21.field.qualifiers.ApproachQualifier3;
import org.mitre.tdp.boogie.arinc.v21.field.qualifiers.SidQualifier1;
import org.mitre.tdp.boogie.arinc.v21.field.qualifiers.SidQualifier2;
import org.mitre.tdp.boogie.arinc.v21.field.qualifiers.SidQualifier3;
import org.mitre.tdp.boogie.arinc.v21.field.qualifiers.StarQualifier1;
import org.mitre.tdp.boogie.arinc.v21.field.qualifiers.StarQualifier2;
import org.mitre.tdp.boogie.arinc.v21.field.qualifiers.StarQualifier3;

/**
 * Starting in supplement 22 the route qualifiers changed to capture the PBN nav spec concept.
 */
public final class RouteTypeQualifier implements FieldSpec<String> {
  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.7a";
  }

  private static final Set<String> QUAL1 = Stream.of(SidQualifier1.VALID, StarQualifier1.VALID, ApproachQualifier1.VALID)
      .flatMap(Collection::stream)
      .collect(Collectors.toSet());
  private static final Set<String> QUAL2 = Stream.of(SidQualifier2.VALID, StarQualifier2.VALID, ApproachQualifier2.VALID)
      .flatMap(Collection::stream)
      .collect(Collectors.toSet());
  private static final Set<String> QUAL3 = Stream.of(SidQualifier3.VALID, StarQualifier3.VALID, ApproachQualifier3.VALID)
      .flatMap(Collection::stream)
      .collect(Collectors.toSet());

  private static final Set<String> VALID = Stream.of(QUAL1, QUAL2, QUAL3).flatMap(Collection::stream).collect(Collectors.toSet());

  @Override
  public Optional<String> apply(String s) {
    return Optional.of(s).map(String::trim).filter(VALID::contains);
  }
}
