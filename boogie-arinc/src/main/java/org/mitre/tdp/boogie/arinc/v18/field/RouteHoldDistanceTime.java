package org.mitre.tdp.boogie.arinc.v18.field;

import java.time.Duration;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.utils.ArincStrings;
import org.mitre.tdp.boogie.arinc.utils.ValidArincNumeric;

/**
 * In Enroute Airways, “Route Distance From” is the distance in nautical miles from the waypoint identified in the records “Fix Ident”
 * field to the next waypoint of the route. In SID, STAR and Approach Procedure records, the field may contain segment distances/along
 * track distances/excursion distances/DME distances. The actual content is dependent on the Path and Termination. For more information
 * on the content, refer to Table Three, Leg Data Fields, in Attachment 5 of this document.
 */
public final class RouteHoldDistanceTime extends TrimmableString {

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.27";
  }

  /**
   * Utility method for parsing the given {@link RouteHoldDistanceTime} field as a {@link Duration} when the given string is the time
   * version of the field (preceded by a 'T').
   */
  public Optional<Duration> asDuration(String fieldString) {
    return Optional.of(fieldString)
        .filter(fs -> fs.startsWith("T"))
        .filter(fs -> ValidArincNumeric.INSTANCE.test(fs.substring(1)))
        .map(fs -> ArincStrings.parseDoubleWithTenths(fs.substring(1)))
        .map(mins -> Duration.ofSeconds((int) (mins * 60)));
  }

  /**
   * Utility method for parsing the given field string as a double distance in nm.
   */
  public Optional<Double> asDistanceInNm(String fieldString) {
    return Optional.of(fieldString)
        .filter(fs -> !fs.startsWith("T"))
        .filter(ValidArincNumeric.INSTANCE)
        .map(ArincStrings::parseDoubleWithTenths);
  }
}
