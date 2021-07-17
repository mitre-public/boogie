package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The “File Record Number” is a reference number assigned to the record for housekeeping purposes. Records are numbered consecutively,
 * the first record on the file being assigned the number 00001, the second the number 00002, and so on through the final record on the
 * file. File record numbers are subject to change at each file update.
 * <br>
 * Note - Record numbers can be repeated, once the number hits 99999 the counter resets to 00000.
 */
public final class FileRecordNumber implements NumericInteger {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.31";
  }
}
