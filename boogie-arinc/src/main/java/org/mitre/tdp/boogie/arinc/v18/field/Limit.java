package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.AltitudeField;

/**
 * Special Use Airspace is described by both lateral and vertical boundaries. The Lower/Upper Limit fields
 * contain the lower and upper limits of the FIR/UIR or Restrictive Airspace being described.
 * <p>
 * Limits for the special use airspace should be derived from official government sources. The field may contain
 * altitude (all numerics), flight levels (alpha/numerics) or an all alpha entry (see examples). The flight
 * level entry will contain the alpha characters FL followed by the altitude in hundreds of feet. These fields
 * will be entered on the first record only of each FIR/UIR or Restrictive Airspace being described.
 * <p>
 * Examples:
 * All Numeric:
 * 05000, 25000
 * <p>
 * Alpha/Numeric:
 * FL245, FL450
 * <p>
 * All Alpha:
 * NOTSP (for Not Specified)
 * UNLTD (for unlimited)
 * GND (for Ground)
 * MSL (for Mean Sea Level)
 * NOTAM (for Restrictive Airspace only)
 */

public final class Limit extends AltitudeField {
  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.121";
  }
}
