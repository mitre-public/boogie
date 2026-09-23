package org.mitre.tdp.boogie.arinc.v18.field;

import static com.google.common.collect.Sets.newHashSet;

import java.util.HashSet;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.tdp.boogie.arinc.SingleCharacterLookup;
import org.mitre.tdp.boogie.arinc.TrimmableField;

/**
 * The “Route Type” field defines the type of Enroute Airway, Preferred Route, Airport and Heliport SID/STAR/Approach Routes of which
 * the record is an element.
 * <br>
 * For Airport and Heliport Approach Routes, “Route Type” includes a “primary route type”, and up to two “route type qualifiers”.
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

  /**
   * The collection of allowed route type qualifier codes.
   */
  static final HashSet<String> allowedCodes = newHashSet("A", "B", "E", "C", "S", "D", "J", "L", "N", "P", "R", "T", "U", "V", "W");

  private static final SingleCharacterLookup<String> VALUES = SingleCharacterLookup.of(allowedCodes, Function.identity());

  @Override
  protected Optional<String> parseTrimmed(String source, int startOffset, int endOffset) {
    return VALUES.parse(source, startOffset, endOffset);
  }
}
