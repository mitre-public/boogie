package org.mitre.tdp.boogie.arinc.database;

import static java.util.Objects.requireNonNull;

import com.google.common.collect.Multimap;

public final class TerminalAreaDatabase {

  private final Multimap<ArincKey, ?> airportLookup;

  TerminalAreaDatabase(Multimap<ArincKey, ?> airportLookup) {
    this.airportLookup = requireNonNull(airportLookup);
  }
}
