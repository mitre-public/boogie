package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

/**
 * When used on the Primary or Primary Extension Continuation Record of the possible record types, with the
 * exception of the Airway Restriction Records, the Time Code field is used to indicate that the data
 * contained in the record is either available continuously or not continuously. When Time Code is used
 * in a Time of Operations Continuation Record, other that Airway Restriction Records, the field is used
 * to indicate how to interpret Time of Operations Continuation Records. On Airway Restriction Primary
 * and Continuation Records, the Time Code indicated either a continuous or non-continuous operation,
 * the details of which are contained in the same record.
 *
 * Active times are derived from official government source. The field will contain an alpha character for
 * which an associated description has been defined as indicated in the tables below.
 *
 * Primary Records:
 * C - Active Continuously, including holidays
 * H - Active Continuously, excluding holidays
 * N - Active Non-Continuously, Refer to Continuation Record
 * P - Active times announced by NOTAM
 * U - Active times are not specified in source documentation
 *
 * Continuation Records:
 * H - Active times are provided in Time of Operation format and exclude holidays
 * N - Activation Times are too complex for Time of Operation format and are provided in Note Form
 * T - Active times are provided in Time of Operation format and include holidays
 *
 * Primary and Continuation Records:
 * C - Active Continuously, including holidays
 * H - Active Continuously, excluding holidays
 * S - Active times are provided in Time of Operation format and exclude holidays
 * T - Active times are provided in Time of Operation format and include holidays
 *
 */

public final class TimeCode extends TrimmableString {
  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.131";
  }
}
