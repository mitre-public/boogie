package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.ArincInteger;

/**
 * Contains 3 decimal numbers to uniquely identify revision of this
 * file. Initially set to 001, but will be incremented if the file is created
 * more than once in the same cycle.
 */
public final class VersionNumber extends ArincInteger {
  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "6.2.1c";
  }
}
