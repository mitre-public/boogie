package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The “Figure of Merit” field is used to specify VHF Navaid facility usable ranges beyond that specified in the Class field.
 * It is also used to specify when a VHF Navaid contained in the database is not available for operational use, i.e., is out
 * of service and is used to flag a VHF Navaid that is not included in a civilian international NOTAM system.
 */
public final class FigureOfMerit extends ArincInteger {

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.149";
  }
}
