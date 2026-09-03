package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.EastWestVariation;

/**
 * The “Magnetic Variation” field specifies the angular difference between True North and Magnetic North at the location defined in the record.
 * “Dynamic Magnetic Variation” is a computer model derived value and takes location and date into consideration. For the “Station Declination”
 * used in some record types, refer to Section 5.66.
 * <br>
 * e.g. E0140, E0000, T0000
 * <br>
 * As in {@link InboundMagneticCourse} this class filters out variations listed as true north. Use {@link org.mitre.tdp.boogie.Declinations} instead for your work.
 */
public final class MagneticVariation extends EastWestVariation {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.39";
  }

}
