package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.ArincDouble;
import org.mitre.tdp.boogie.arinc.utils.ArincDecimalParser;

import java.util.Optional;

/**
 * The Leg Length field specifies the distance of either the
 * inbound leg or the outbound leg of the holding pattern. The determination of inbound
 * or outbound is identified by the content of Section 5.298 of the applicable record.
 * Inbound is defined as the distance between the point at which the aircraft rolls out
 * on the inbound leg of the holding pattern and the fix at which the holding pattern is
 * defined. Outbound is defined as the distance from a point abeam the holding fix to
 * the beginning of the inbound turn (Figure 5-4).
 */
public final class LegLength extends ArincDouble {

  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.64";
  }

  @Override
  public Optional<Double> apply(String fieldValue) {
    return Optional.of(fieldValue)
        .map(String::trim)
        .flatMap(ArincDecimalParser.INSTANCE::parseDoubleWithTenths);
  }
}
