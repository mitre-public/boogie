package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The Controlled Airspace Center field is used to define the navigation element upon which the controlled airspace
 * being defined is predicated, but not necessarily centered. Where the Airspace is not defined then the Region
 * Identifier should be used. In this case, the Controlled Airspace Center will contain the ICAO Identification
 * code for the Controlled Airspace to which the data contained in the record relates.
 *
 * The Controlled Airspace Center will be determined during the construction of the records. As an example the New
 * York Class B Airspace (formerly TCA) is centered on the JFK VOR, the LGA VOR and the Newark airport. The Controlled
 * Airspace Center field could contain the Kennedy Airport identifier KJFK as the key for all records describing the
 * New York Class B Airspace. The field may contain a Navaid, Enroute Waypoint, Heliport or Airport Identifier. A
 * Region Identifier content should be derived from official government source where the controlling authority is
 * published or from ICAO Document 7910, Location Indicators. In cases where no official identifier is published
 * that can be used as the Airspace Center where the controlled airspace is used for more than one airport, the
 * Region Identifier can be used.
 *
 * It should be noted that during construction of a Controlled Airspace Center, no published Navaid, Enroute Waypoint,
 * Airport Identifier or Region Identifier may be found to be suitable. Data suppliers may create a center waypoint for
 * use in the Airspace Center field in such cases.
 */

public final class AirspaceCenter extends TrimmableString {
  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.214";
  }
}
