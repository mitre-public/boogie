package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.EastWestVariation;

/**
 * For VHF NAVAIDS, the “Station Declination” field contains the angular difference between true north and the zero degree radial of
 * the NAVAID at the time the NAVAID was last site checked. For ILS localizers, the field contains the angular difference between true
 * north and magnetic north at the localizer antenna site at the time the magnetic bearing of the localizer course was established.
 * <br>
 * Valid modifier values for the declination string are:
 * <br>
 * 1. E - Declination is East of True North
 * 2. W - Declination is West of True North
 * 3. T - Station is oriented to True North in an area in which the local variation is not zero.
 * 4. G - Station is oriented to Grid North
 * <br>
 * This parser elides T/G.
 * <br>
 * e.g. E0072, E0000, T0000, G0000
 */
public final class StationDeclination extends EastWestVariation {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.66";
  }

}
