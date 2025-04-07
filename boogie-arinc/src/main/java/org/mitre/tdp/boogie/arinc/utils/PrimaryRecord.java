package org.mitre.tdp.boogie.arinc.utils;

import java.util.function.Predicate;

/**
 * This predicate is used to determine if raw arinc is a continuation record
 * or this predicate is used by the continuation record filter to determine if an ArincRecord is a
 * primary record.
 * Primary records have either a "0" or a "1" for this value.
 */
public final class PrimaryRecord implements Predicate<String> {
  public static final PrimaryRecord INSTANCE = new PrimaryRecord();

  private PrimaryRecord() {
  }

  /**
   * The trick is that it does not matter what the value is other than these two string values.
   * After that you need to check application type to tell what type of continuation it is.
   * The numbers/letters thing just helps sort the text file nicely, which we don't care about after parsing.
   * @param continuationNumber the string that is found in the records continuation record field.
   * @return true if its a primary record
   */
  @Override
  public boolean test(String continuationNumber) {
    return "0".equals(continuationNumber) || "1".equals(continuationNumber);
  }
}
