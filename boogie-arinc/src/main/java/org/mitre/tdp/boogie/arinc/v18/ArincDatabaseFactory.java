package org.mitre.tdp.boogie.arinc.v18;

import java.io.File;

/**
 * Factory class for instantiating {@link ArincDatabase}(s) from a variety of sources. The most common instantiation pattern
 * involves loading the database from an ARINC-424 flat file.
 */
public final class ArincDatabaseFactory {

  private ArincDatabaseFactory() {
    throw new IllegalStateException("Cannot instantiate static factory class.");
  }

  /**
   * Reads, parses and indexes a flat file containing ARINC424 data into a new {@link ArincDatabase} record.
   */
  public static ArincDatabase newDatabaseFromFile(File file) {
    return null;
  }
}
