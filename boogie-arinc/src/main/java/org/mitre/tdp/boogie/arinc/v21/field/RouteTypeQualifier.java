package org.mitre.tdp.boogie.arinc.v21.field;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.arinc.SingleCharacterLookup;
import org.mitre.tdp.boogie.arinc.TrimmableField;
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
public final class RouteTypeQualifier extends TrimmableField<String> {
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

  private static final SingleCharacterLookup<String> VALUES = SingleCharacterLookup.of(VALID, Function.identity());

  @Override
  protected Optional<String> parseTrimmed(String source, int startOffset, int endOffset) {
    return VALUES.parse(source, startOffset, endOffset);
  }
}
