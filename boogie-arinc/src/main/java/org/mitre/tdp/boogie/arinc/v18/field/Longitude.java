package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.ArincCoordinate;

/**
 * The Longitude field contains the longitude of the geographic position of the navigational feature identified in the record.
 */
public final class Longitude extends ArincCoordinate {

  public Longitude() {
    super(3, 'E', 'W');
  }

  @Override
  public int fieldLength() {
    return 10;
  }

  @Override
  public String fieldCode() {
    return "5.37";
  }

}
