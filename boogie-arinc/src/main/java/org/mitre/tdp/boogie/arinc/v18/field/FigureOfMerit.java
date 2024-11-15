package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The “Figure of Merit” field is used to specify VHF Navaid facility usable ranges beyond that specified in the Class field.
 * It is also used to specify when a VHF Navaid contained in the database is not available for operational use, i.e., is out
 * of service and is used to flag a VHF Navaid that is not included in a civilian international NOTAM system.
 * <br>
 * 0 - Terminal, generally within 25nm
 * 1 - Low altitude, generally within 40nm
 * 2 - High altitude, generally within 130nm
 * 3 - Extended high altitude, generally beyond 130nm
 * 7 - Not included in a civil international NOTAM system
 * 9 - Out of service
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
