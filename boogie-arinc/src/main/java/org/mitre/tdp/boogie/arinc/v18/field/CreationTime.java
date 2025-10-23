package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

/**
 * Contains the UTC time when the file was created. The format is two
 * decimal digits each for hours, minutes, and seconds, separated by
 * colons. (Example: 13:12:02 = 1:12:02 p.m.)
 */
public final class CreationTime extends TrimmableString {
  @Override
  public int fieldLength() {
    return 8;
  }

  @Override
  public String fieldCode() {
    return "6.2.1h";
  }
}
