package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ValidArincNumeric;

/**
 * The “Longest Runway” field permits airport to be classified on the basis of the longest operational hard-surface runway.
 * <br>
 * The longest runway will be derived from official government sources and entered in the field in hundreds of feet.
 * <br>
 * e.g. 040, 055, 098, 111
 */
public final class LongestRunway implements FieldSpec<Integer> {

  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.54";
  }

  @Override
  public Optional<Integer> apply(String fieldValue) {
    return Optional.of(fieldValue).map(String::trim).filter(ValidArincNumeric.INSTANCE).map(Integer::parseInt).map(i -> i * 100);
  }
}
