package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mitre.tdp.boogie.arinc.FieldSpec;

class TestAltitudeFlightLevelFields {

  private static final List<FieldSpec<Double>> PARSERS = List.of(
      new MinimumAltitude(),
      new MaxAltitude(),
      new Limit(),
      new SpeedLimitAltitude()
  );

  @Test
  void testEmbeddedSlicesMatchStringParsing() {
    List<String> values = List.of(
        "     ", "1234A", "FLAVA", "FL050", "05000", "-0012",
        "UNLTD", "UNKNN", "NESTB", "NOTSP", " GND ", " MSL ", "NOTAM",
        "unltd", "unknn", "nestb"
    );

    assertAll(PARSERS.stream()
        .flatMap(parser -> values.stream().map(value -> equivalentParsing(parser, value))));
  }

  private static Executable equivalentParsing(FieldSpec<Double> parser, String fieldValue) {
    return () -> {
      String source = "xx" + fieldValue + "yy";
      assertEquals(parser.apply(fieldValue), parser.parse(source, 2, 2 + fieldValue.length()),
          parser.getClass().getSimpleName() + ": " + fieldValue);
    };
  }

  @Test
  void parsesFeetAndFlightLevels() {
    assertAll(PARSERS.stream()
        .flatMap(parser -> List.<Executable>of(
            () -> assertEquals(Optional.of(5000.0), parser.apply("05000")),
            () -> assertEquals(Optional.of(5000.0), parser.apply("FL050")),
            () -> assertEquals(Optional.of(-12.0), parser.apply("-0012")),
            () -> assertEquals(Optional.of(12.0), parser.apply("+0012")),
            () -> assertEquals(Optional.empty(), parser.apply("FL+12")),
            () -> assertEquals(Optional.empty(), parser.apply("١٢٣٤٥")),
            () -> assertEquals(Optional.empty(), parser.apply("123")),
            () -> assertEquals(
                Double.doubleToRawLongBits(-0.0),
                Double.doubleToRawLongBits(parser.apply("-0000").orElseThrow())),
            () -> assertThrows(IndexOutOfBoundsException.class, () -> parser.parse("12345", -1, 5))
        ).stream()));
  }

  @Test
  void rejectsPublishedNonNumericSentinels() {
    List<String> sentinels = List.of("UNLTD", "UNKNN", "NESTB", "NOTSP", " GND ", " MSL ", "NOTAM");

    assertAll(PARSERS.stream()
        .flatMap(parser -> sentinels.stream()
            .map(sentinel -> () -> assertEquals(Optional.empty(), parser.apply(sentinel),
                parser.getClass().getSimpleName() + ": " + sentinel))));
  }
}
