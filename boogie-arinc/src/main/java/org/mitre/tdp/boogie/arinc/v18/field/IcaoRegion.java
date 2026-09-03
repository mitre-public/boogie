package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.FieldSpec;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static java.util.Objects.checkFromToIndex;

/**
 * The “ICAO Code” field permits records to be categorized geographically within the limits of the categorization performed
 * by the {@link CustomerAreaCode} field.
 * <p>
 * e.g. K1, K7, PA, MM, EG, UT, J
 * <p>
 * The region code is generally the combination of the country code (e.g. K for USA) with some secondary notation indicating
 * distinct regions within that area. Note because everyone hates everyone that the single character country code is not the
 * same as the {@link CustomerAreaCode#boundaryCode()} because why would it be.
 * <p>
 * A region may contain either one or two characters; two-column records commonly pad a one-character region with a space.
 * <p>
 * <a href="https://upload.wikimedia.org/wikipedia/commons/3/3b/ICAO-countries.png">...</a>
 */
public final class IcaoRegion implements FieldSpec<String> {

  private static final String VALID_CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final int SINGLE_CHARACTER_REGION = VALID_CHARACTERS.length();
  private static final int SECOND_CHARACTER_COUNT = VALID_CHARACTERS.length() + 1;
  private static final List<Optional<String>> REGIONS = IntStream.range(0, VALID_CHARACTERS.length() * SECOND_CHARACTER_COUNT)
      .mapToObj(IcaoRegion::regionAt)
      .map(Optional::of)
      .toList();

  @Override
  public int fieldLength() {
    return 2;
  }

  @Override
  public String fieldCode() {
    return "5.14";
  }

  @Override
  public Optional<String> parse(String source, int startOffset, int endOffset) {
    checkFromToIndex(startOffset, endOffset, source.length());
    int length = endOffset - startOffset;
    if (length < 1 || length > fieldLength()) {
      return Optional.empty();
    }

    int firstIndex = asciiAlphaNumericIndex(source.charAt(startOffset));
    if (firstIndex < 0) {
      return Optional.empty();
    }

    int secondIndex = length == 1 ? -1 : asciiAlphaNumericIndex(source.charAt(startOffset + 1));
    if (length == 2 && secondIndex < 0 && source.charAt(startOffset + 1) != ' ') {
      return Optional.empty();
    }

    int normalizedSecondIndex = secondIndex >= 0 ? secondIndex : SINGLE_CHARACTER_REGION;
    int regionIndex = firstIndex * SECOND_CHARACTER_COUNT + normalizedSecondIndex;
    return REGIONS.get(regionIndex);
  }

  private static String regionAt(int regionIndex) {
    int firstIndex = regionIndex / SECOND_CHARACTER_COUNT;
    int secondIndex = regionIndex % SECOND_CHARACTER_COUNT;
    char first = VALID_CHARACTERS.charAt(firstIndex);
    return secondIndex == SINGLE_CHARACTER_REGION ? Character.toString(first) : new String(new char[]{first, VALID_CHARACTERS.charAt(secondIndex)});
  }

  private static int asciiAlphaNumericIndex(char character) {
    if (character >= '0' && character <= '9') {
      return character - '0';
    }
    return character >= 'A' && character <= 'Z' ? character - 'A' + 10 : -1;
  }
}
