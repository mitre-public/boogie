package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

/**
 * This field contains the 32 bit CRC value for the ARINC data
 * file (including data and header records).
 * ARINC Report 665, Loadable Software Standards, Section 4.0
 * defines the use of CRC codes.
 * <p>
 * The CRC Polynomial used to calculate the CRC of the ARINC
 * 424 data file shall be the 32-bit CRC (0x04C11DB7), calculated
 * as described in ARINC Report 665
 * <p>
 * For purposes of calculating a CRC value, Header record 1, Columns
 * 125 through 132, shall be considered to contain zeros.
 */
public final class FileCrc extends TrimmableString {
  @Override
  public int fieldLength() {
    return 8;
  }

  @Override
  public String fieldCode() {
    return "6.2.1l";
  }
}
