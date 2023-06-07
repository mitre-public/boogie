package org.mitre.tdp.boogie.arinc;

import static java.util.Objects.requireNonNull;

public final class ArincRecordIterator {

  private final ArincRecordParser parser;

  private ArincRecordIterator(ArincRecordParser parser) {
    this.parser = requireNonNull(parser);
  }
}
