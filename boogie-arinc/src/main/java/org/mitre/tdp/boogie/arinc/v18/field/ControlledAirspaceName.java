package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The Controlled Airspace Name field will contain the name of the controlled airspace when assigned.
 *
 * Names will be derived from official government sources. The name, if assigned, will be entered in the
 * first record only. If source does not assign a name, the field may be blank.
 */

public final class ControlledAirspaceName extends TrimmableString {
  @Override
  public int fieldLength() {
    return 30;
  }

  @Override
  public String fieldCode() {
    return "5.216";
  }
}
