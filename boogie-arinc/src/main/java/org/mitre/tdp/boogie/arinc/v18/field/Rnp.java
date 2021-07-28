package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ValidArincNumeric;

/**
 * Required Navigation Performance (RNP) is a statement of the Navigation Performance necessary for operation within a defined
 * airspace in accordance with ICAO Annex 15 and/or State published rules.
 * <br>
 * When used on Enroute Airway segments, RNP shall apply inbound to the fix when viewed in increasing sequence number order.
 * The RNP applies only to the airway leg on which it is specified. If no RNP values is coded on a segment, there is not a
 * database specified RNP for that segment.
 * <br>
 * When used on a SID, STAR and Approach Procedure records, the RNP shall apply to the segment on which it is coded. RNP will
 * be coded on every segment where it is specified by source. Lack of a RNP value on a segment indicates no source supplied
 * RNP value was available for that segment.
 * <br>
 * e.g. 990 (equal to 99.0NM), 120 (equal to 12.0NM), 013 (equal to 0.001NM)
 */
public final class Rnp implements FieldSpec<Double> {

  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.211";
  }

  /**
   * The required navigational precision in nm.
   */
  @Override
  public Optional<Double> apply(String fieldValue) {
    return Optional.of(fieldValue).filter(ValidArincNumeric.INSTANCE)
        .filter(s -> s.length() > 2)
        .map(s -> {
          int value = Integer.parseInt(fieldValue.substring(0, 2));
          int exp = -Integer.parseInt(fieldValue.substring(2));
          return value * Math.pow(10., exp);
        });
  }
}
