package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.AltitudeField;

/**
 * The “Maximum Altitude” field is used to indicate the maximum altitude allowed.
 * <br>
 * Maximum altitudes should be derived from official government publications describing the upper limit of the airway in feet
 * or flight level.
 * <br>
 * e.g. 17999, 08000, FL100, FL450, UNLTD
 * <br>
 * TDP returns all MaxAltitude values converted to feet. TDP also filters UNLTD values (for now) we could put in a placeholder
 * but we'll wait to see if anyone cares.
 */
public final class MaxAltitude extends AltitudeField {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.127";
  }
}
