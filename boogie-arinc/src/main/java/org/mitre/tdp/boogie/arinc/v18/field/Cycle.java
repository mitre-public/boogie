package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.mitre.tdp.boogie.arinc.TrimmableField;

/**
 * The “Cycle Date” field identifies the calendar period in which the record was added to the file or last revised. A change in
 * any ARINC 424 field, except Dynamic Magnetic V ariation, Frequency Protection, Continuation Record Number and File Record Number,
 * requires a cycle date change. The cycle date will not change if there is no change in the data.
 */
public final class Cycle extends TrimmableField<String> {

  private static final List<Optional<String>> ASCII_CYCLES = IntStream.range(0, 10_000)
      .mapToObj(Cycle::cycleAt)
      .map(Optional::of)
      .toList();

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.31";
  }

  @Override
  protected Optional<String> parseTrimmed(String source, int startOffset, int endOffset) {
    if (endOffset - startOffset != 4) {
      return Optional.empty();
    }

    int asciiCycle = asciiCycle(source, startOffset);
    if (asciiCycle < 0) {
      return Optional.empty();
    }
    return ASCII_CYCLES.get(asciiCycle);
  }

  private static String cycleAt(int cycle) {
    return Integer.toString(cycle + 10_000).substring(1);
  }

  private static int asciiCycle(String source, int startOffset) {
    int cycle = 0;
    for (int index = startOffset; index < startOffset + 4; index++) {
      char character = source.charAt(index);
      if (character < '0' || character > '9') {
        return -1;
      }
      cycle = cycle * 10 + character - '0';
    }
    return cycle;
  }
}
