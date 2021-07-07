package org.mitre.tdp.boogie.arinc.database;

import static java.util.Objects.requireNonNull;

public final class AirwayDatabase {

  private final ArincDatabase arincDatabase;

  public AirwayDatabase(ArincDatabase arincDatabase) {
    this.arincDatabase = requireNonNull(arincDatabase);
  }


}
