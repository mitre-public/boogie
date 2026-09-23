package org.mitre.tdp.boogie.arinc.v18.field;

import static java.util.Objects.checkFromToIndex;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.SingleCharacterLookup;

/**
 * Routes of flight frequently cross geographical boundaries. The “Boundary Code” field identifies the area into, or from which
 * a continuous route passes when such a crossing occurs.
 */
public final class BoundaryCode implements FieldSpec<CustomerAreaCode> {

  private static final SingleCharacterLookup<CustomerAreaCode> PARSED_VALUES =
      SingleCharacterLookup.of(CustomerAreaCode.lookup.keySet(), code -> CustomerAreaCode.valueOf(CustomerAreaCode.lookup.get(code)));

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.18";
  }

  @Override
  public Optional<CustomerAreaCode> parse(String source, int startOffset, int endOffset) {
    checkFromToIndex(startOffset, endOffset, source.length());
    while (startOffset < endOffset && source.charAt(startOffset) <= ' ') {
      startOffset++;
    }
    while (startOffset < endOffset && source.charAt(endOffset - 1) <= ' ') {
      endOffset--;
    }
    return PARSED_VALUES.parse(source, startOffset, endOffset);
  }
}
