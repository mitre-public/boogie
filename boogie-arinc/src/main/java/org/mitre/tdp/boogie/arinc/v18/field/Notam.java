package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

/**
 * Restrictive Airspace areas may not have established active times and are activated by NOTAM or
 * may be active by NOTAM in addition to established times.
 *
 * Active times by NOTAM will be derived from official government source. When used on primary records,
 * the area is active only by NOTAM and there will be no continuation record. When used on continuation
 * records, the area is active by NOTAM in addition to the established times. The field will contain
 * the alpha character N to indicate either condition, otherwise the field will be blank.
 */

public final class Notam extends TrimmableString {
  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.132";
  }
}
