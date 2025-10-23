package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

/**
 * Unique part number for database. (Optional - Content TBD)
 */
public final class DatabasePartNumber extends TrimmableString {
  @Override
  public int fieldLength() {
    return 20;
  }

  @Override
  public String fieldCode() {
    return "6.2.1k";
  }
}
