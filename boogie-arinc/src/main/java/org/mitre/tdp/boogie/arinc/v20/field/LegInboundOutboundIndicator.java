package org.mitre.tdp.boogie.arinc.v20.field;

import static com.google.common.collect.Sets.newHashSet;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The leg Inbound/Outbound Indicator is used to identify the Leg Length or Leg Time Field values
 * (6.64 or 5.65) as being applicable to either the inbound or the outbound leg of a holding pattern or
 * racetrack course reversal.
 * <p>
 * The field will contain I for inbound or O for outbound. On SID/STAR/APPROACH records the field is populated
 * when the Path and Terminator in teh record is HA, HF, or HM only, otherwise it is left blank.
 */
public final class LegInboundOutboundIndicator implements FieldSpec<String> {
  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.298";
  }

  @Override
  public Optional<String> apply(String fieldValue) {
    return Optional.of(fieldValue).map(String::trim).filter(allowedCodes::contains);
  }

  private static final Set<String> allowedCodes = newHashSet("I", "O");
}
