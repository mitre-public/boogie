package org.mitre.tdp.boogie.arinc.database;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.arinc.ArincRecord;

public final class TransitionStream<T> {

  private final ArincDatabase arincDatabase;
  private final Function<ArincRecord, T> converter;

  public TransitionStream(ArincDatabase arincDatabase, Function<ArincRecord, T> converter) {
    this.arincDatabase = requireNonNull(arincDatabase);
    this.converter = requireNonNull(converter);
  }

  public Stream<List<T>> streamTransitions() {
    return Stream.empty();
  }
}
