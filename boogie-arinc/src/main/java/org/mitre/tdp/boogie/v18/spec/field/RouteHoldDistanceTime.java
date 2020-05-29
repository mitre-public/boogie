package org.mitre.tdp.boogie.v18.spec.field;

import static org.mitre.tdp.boogie.utils.Preconditions.checkSpec;

import java.time.Duration;

import org.mitre.tdp.boogie.FieldSpec;
import org.mitre.tdp.boogie.utils.ArincStrings;
import org.mitre.tdp.boogie.v18.spec.common.FilterTrimEmptyInput;

/**
 * In Enroute Airways, “Route Distance From” is the distance in nautical miles from the waypoint identified in the records “Fix Ident”
 * field to the next waypoint of the route. In SID, STAR and Approach Procedure records, the field may contain segment distances/along
 * track distances/excursion distances/DME distances. The actual content is dependent on the Path and Termination. For more information
 * on the content, refer to Table Three, Leg Data Fields, in Attachment 5 of this document.
 */
public class RouteHoldDistanceTime implements FieldSpec<String>, FilterTrimEmptyInput<String> {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.27";
  }

  @Override
  public String parseValue(String fieldValue) {
    return fieldValue;
  }

  /**
   * Utility method for parsing the given {@link RouteHoldDistanceTime} field as a {@link Duration} when the given string is the time
   * version of the field (preceded by a 'T').
   */
  public Duration asDuration(String fieldString) {
    checkSpec(this, fieldString, fieldString.startsWith("T"));
    String pre = fieldString.substring(1, 3);
    String post = fieldString.substring(3);
    long postLong = (Long.parseLong(post) * 60) / 10;
    return Duration.ofSeconds(Long.parseLong(pre) * 60 + postLong);
  }

  /**
   * Utility method for parsing the given field string as a double distance in nm.
   */
  public Double asDistanceInNm(String fieldString) {
    checkSpec(this, fieldString, !fieldString.startsWith("T"));
    return ArincStrings.parseDoubleWithTenths(fieldString);
  }
}
