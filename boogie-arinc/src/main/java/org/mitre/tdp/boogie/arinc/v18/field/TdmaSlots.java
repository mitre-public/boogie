package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

/**
 * The Time Division Multiple Access (TDMA) identifies the time
 * slot(s) in which the ground station transmits the related approach. The high
 * precision time source available through GPS permits utilization of Time division
 * multiplexing or TDMA (Time Division Multiple Access), allowing multiple ground
 * stations to share a common frequency by dividing it into eight time slots. An
 * individual station may broadcast in one or more of eight slots.
 * <br>
 * The value for this field will be derived from official government
 * sources. The range is 01 to FF. If no source is provided, the default value will be
 * blank.
 */
public final class TdmaSlots extends TrimmableString {
  @Override
  public int fieldLength() {
    return 2;
  }

  @Override
  public String fieldCode() {
    return "5.246";
  }
}
