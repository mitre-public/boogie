package org.mitre.tdp.boogie.arinc.v18.field;

import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.TrimmableString;

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
   * Interprets a time-form {@link RouteHoldDistanceTime} value, identified by its {@code T} prefix.
   */
  public Optional<Duration> asDuration(String fieldString) {
    requireNonNull(fieldString);
    if (!fieldString.startsWith("T")) {
      return Optional.empty();
    }

    int tenthsOfAMinute = parseTenths(fieldString, 1);
    return tenthsOfAMinute < 0 ? Optional.empty() : Optional.of(Duration.ofSeconds(tenthsOfAMinute * 6L));
  }

  /**
   * Interprets a distance-form value as nautical miles.
   */
  public Optional<Double> asDistanceInNm(String fieldString) {
    requireNonNull(fieldString);
    if (fieldString.startsWith("T")) {
      return Optional.empty();
    }

    int tenthsOfANauticalMile = parseTenths(fieldString, 0);
    return tenthsOfANauticalMile < 0
        ? Optional.empty()
        : Optional.of(tenthsOfANauticalMile / 10.0);
  }

  private static int parseTenths(String fieldString, int startOffset) {
    if (startOffset == fieldString.length()) {
      return -1;
    }

    int value = 0;
    for (int index = startOffset; index < fieldString.length(); index++) {
      char character = fieldString.charAt(index);
      if (character < '0' || character > '9') {
        return -1;
      }
      value = value * 10 + character - '0';
    }
    return value;
  }
}
