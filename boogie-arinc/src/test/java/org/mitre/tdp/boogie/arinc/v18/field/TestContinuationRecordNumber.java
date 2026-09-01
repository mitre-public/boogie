package org.mitre.tdp.boogie.arinc.v18.field;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TestContinuationRecordNumber {

  private static final ContinuationRecordNumber parser = new ContinuationRecordNumber();

  @Test
  void filtersInvalidInputs() {
    assertAll(
        () -> assertEquals(Optional.empty(), parser.apply("")),
        () -> assertEquals(Optional.empty(), parser.apply("   ")),
        () -> assertEquals(Optional.empty(), parser.apply("@")),
        () -> assertEquals(Optional.empty(), parser.apply("a"))
    );
  }

  @Test
  void parsesValidInputs() {
    assertAll(
        () -> assertEquals(Optional.of("0"), parser.apply("0")),
        () -> assertEquals(Optional.of("9"), parser.apply("9")),
        () -> assertEquals(Optional.of("A"), parser.apply("A")),
        () -> assertEquals(Optional.of("Z"), parser.apply("Z"))
    );
  }

  @Test
  void rejectsValuesThatDoNotMatchTheOneColumnField() {
    assertAll(
        () -> assertEquals(Optional.empty(), parser.apply("   1   ")),
        () -> assertEquals(Optional.empty(), parser.apply("@A?")),
        () -> assertEquals(Optional.empty(), parser.apply("a1!"))
    );
  }

  @Test
  void directAndRangeParsingAreEquivalent() {
    assertAll(
        Stream.of("", " ", "@", "a", "0", "Z", "1")
            .map(field -> () -> {
              String source = "xx" + field + "yy";
              assertEquals(parser.apply(field), parser.parse(source, 2, 2 + field.length()), field);
            })
    );
  }

  @Test
  void cachesEveryValidSingleCharacterResult() {
    assertAll(
        validContinuationNumbers().map(character -> () -> {
          String field = String.valueOf(character);
          Optional<String> direct = parser.apply(field);
          Optional<String> range = parser.parse("xx" + field + "yy", 2, 3);

          assertAll(
              () -> assertEquals(Optional.of(field), direct),
              () -> assertSame(direct, parser.apply(field)),
              () -> assertSame(direct, range),
              () -> assertSame(direct.orElseThrow(), range.orElseThrow())
          );
        })
    );
  }

  private static Stream<Character> validContinuationNumbers() {
    return IntStream.concat(IntStream.rangeClosed('0', '9'), IntStream.rangeClosed('A', 'Z'))
        .mapToObj(value -> (char) value);
  }
}
