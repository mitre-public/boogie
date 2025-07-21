package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

/**
 * The station type identifies the type of the differential ground
 * station. The first character will be L for LAAS/GLS ground station, C for SCAT-1
 * station. The second and third character will be blank for the moment. They will
 * indicate the interoperability standard to which the station conforms.
 * <br>
 * The value for this field will be derived from official government
 * sources. If LAAS/GLS or SCAT-1 is not specified in source, the default value will be
 * blank.
 */
public final class StationType extends TrimmableString {
  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.247";
  }
}
