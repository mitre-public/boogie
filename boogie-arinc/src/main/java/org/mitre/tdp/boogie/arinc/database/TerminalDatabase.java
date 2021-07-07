package org.mitre.tdp.boogie.arinc.database;

import static java.util.Objects.requireNonNull;

/**
 * Decorator database on top of an {@link ArincDatabase} - this class provides a secondary index of relevant ARINC data based on
 * record's associated airport definition.
 * <br>
 * This record type provides secondary indices
 */
public final class TerminalDatabase {

  private final ArincDatabase arincDatabase;

  public TerminalDatabase(ArincDatabase arincDatabase) {
    this.arincDatabase = requireNonNull(arincDatabase);
  }


}
