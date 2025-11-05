package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

/**
 * Contains the date when the file was created. Format is DD-MMM-
 * YYYY. Where DD is the two-digit decimal day of month, MMM
 * is a three-character month abbreviation, and YYYY is the four-digit
 * decimal year. (Example: 12-APR-2002)
 */
public final class CreationDate extends TrimmableString {
  @Override
  public int fieldLength() {
    return 11;
  }

  @Override
  public String fieldCode() {
    return "6.2.1g";
  }
}
