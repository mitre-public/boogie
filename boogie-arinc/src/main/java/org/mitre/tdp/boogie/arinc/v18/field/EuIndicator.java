package org.mitre.tdp.boogie.arinc.v18.field;

import static java.util.Objects.checkFromToIndex;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The “EU Indicator” field is used to identify those Enroute Airway records that have an Airway Restriction record
 * without identifying the restriction.
 * <br>
 * The field will contain the alpha character “Y” when a restriction for the segment is contained in the restriction
 * file or a blank when no restriction record exists.
 */
public final class EuIndicator implements FieldSpec<Boolean> {

  private static final Optional<Boolean> TRUE = Optional.of(true);
  private static final Optional<Boolean> FALSE = Optional.of(false);

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.164";
  }

  @Override
  public Optional<Boolean> parse(String source, int startOffset, int endOffset) {
    checkFromToIndex(startOffset, endOffset, source.length());
    return endOffset - startOffset == 1 && source.regionMatches(true, startOffset, "Y", 0, 1) ? TRUE : FALSE;
  }
}
