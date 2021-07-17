package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * Definition/Description: The “Subsection Code” field defines the specific part of the database major section in which the
 * record resides.
 * <br>
 * Additionally, records that reference other records within the database use Section/Subsection Codes to make the reference,
 * together with the record identifier. This is true for “fix” information in Holdings, Enroute Airways, Airport and Heliport
 * SID/STAR/APPROACH, all kinds of Communications, Airport and Heliport MSA, Airport and Heliport TAA, Company Routes, Enroute
 * Airway Restrictions, Preferred Routes and Alternate Records. The Section Code will define the major database section, the
 * Subsection Code permits the exact section (file) to be identified and the “fix” (record) can then be located within this file.
 */
public final class SubSectionCode implements FreeFormString, FilterTrimEmptyInput<String> {

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.5";
  }
}
