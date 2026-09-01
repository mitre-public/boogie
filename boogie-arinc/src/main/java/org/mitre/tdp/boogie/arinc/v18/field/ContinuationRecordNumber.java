package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.FieldSpec;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.checkFromToIndex;

/**
 * When it is not possible to store all the information needed on a record within the 132 columns of the record itself, the
 * so-called Primary Record; one or more continuation records may be used.
 * <br>
 * e.g. [0-9][A-Z]
 */
public final class ContinuationRecordNumber implements FieldSpec<String> {

  private static final String VALID_VALUES = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final List<Optional<String>> PARSED_VALUES = VALID_VALUES.chars()
      .mapToObj(character -> Optional.of(Character.toString(character)))
      .toList();

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.16";
  }

  @Override
  public Optional<String> parse(String source, int startOffset, int endOffset) {
    checkFromToIndex(startOffset, endOffset, source.length());
    if (endOffset - startOffset != fieldLength()) {
      return Optional.empty();
    }

    int index = continuationIndex(source.charAt(startOffset));
    return index < 0 ? Optional.empty() : PARSED_VALUES.get(index);
  }

  private static int continuationIndex(char character) {
    if (character >= '0' && character <= '9') {
      return character - '0';
    }
    return character >= 'A' && character <= 'Z' ? character - 'A' + 10 : -1;
  }
}
