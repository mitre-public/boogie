package org.mitre.tdp.boogie.arinc.v19.field;

import static com.google.common.collect.Sets.newHashSet;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.mitre.tdp.boogie.arinc.SingleCharacterLookup;
import org.mitre.tdp.boogie.arinc.TrimmableField;

import com.google.common.collect.Sets;

public final class RouteTypeQualifier extends TrimmableField<String> {

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.7a";
  }

  @Override
  protected Optional<String> parseTrimmed(String source, int startOffset, int endOffset) {
    return VALUES.parse(source, startOffset, endOffset);
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

  private static final SingleCharacterLookup<String> VALUES = SingleCharacterLookup.of(allowedCodes, Function.identity());
}
