package org.mitre.tdp.boogie.arinc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.v18.field.BlankSpec;

class ArincRecordParserTest {

  @Test
  void testParserBreaksOnNullInputString() {

    ArincRecordParser parser = ArincRecordParser.standard(
        dummySpec(11, List.of(RecordDiscriminator.prefix("ARINCRECORD")),
            new RecordField<>("field1", new BlankSpec(11)))
    );

    assertThrows(NullPointerException.class, () -> parser.parse(null));
  }

  @Test
  void testParserWithConfiguredSpec() {

    ArincRecordParser parser = ArincRecordParser.standard(
        dummySpec(11, List.of(RecordDiscriminator.prefix("ARINCRECORD")),
            new RecordField<>("field1", new BlankSpec(11)))
    );

    ArincRecord actual = parser.parse("ARINCRECORD").orElseThrow();
    assertEquals("ARINCRECORD", actual.rawField("field1"));
  }

  @Test
  void testParserDecodesFieldsLazilyAndCachesParsedValues() {
    CountingFieldSpec fieldSpec = new CountingFieldSpec(11);
    ArincRecordParser parser = ArincRecordParser.standard(
        dummySpec(11, List.of(RecordDiscriminator.prefix("ARINCRECORD")),
            new RecordField<>("field1", fieldSpec))
    );

    ArincRecord actual = parser.parse("ARINCRECORD").orElseThrow();
    assertEquals(0, fieldSpec.parseCount());
    assertEquals("ARINCRECORD", actual.rawRecord());
    assertEquals("ARINCRECORD", actual.rawField("field1"));
    assertEquals(0, fieldSpec.parseCount());

    assertEquals(Optional.of("ARINCRECORD"), actual.optionalField("field1"));
    assertEquals(Optional.of("ARINCRECORD"), actual.optionalField("field1"));
    assertEquals(1, fieldSpec.parseCount());
  }

  @Test
  void testParserCachesEmptyParsedValues() {
    CountingFieldSpec fieldSpec = new CountingFieldSpec(11, true);
    ArincRecordParser parser = ArincRecordParser.standard(
        dummySpec(11, List.of(RecordDiscriminator.prefix("ARINCRECORD")),
            new RecordField<>("field1", fieldSpec))
    );

    ArincRecord actual = parser.parse("ARINCRECORD").orElseThrow();
    assertEquals(Optional.empty(), actual.optionalField("field1"));
    assertEquals(Optional.empty(), actual.optionalField("field1"));
    assertEquals(1, fieldSpec.parseCount());
  }

  @Test
  void testParserPreservesFirstMatchingSpec() {
    ArincRecordParser parser = ArincRecordParser.standard(
        dummySpec(11, List.of(RecordDiscriminator.prefix("ARINCRECORD")),
            new RecordField<>("first", new BlankSpec(11))),
        dummySpec(11, List.of(RecordDiscriminator.prefix("ARINCRECORD")),
            new RecordField<>("second", new BlankSpec(11)))
    );

    ArincRecord actual = parser.parse("ARINCRECORD").orElseThrow();
    assertEquals("ARINCRECORD", actual.rawField("first"));
    assertFalse(actual.specForField("second").isPresent());
  }

  @Test
  void testParserNormalizesTrailingSpacePadding() {
    ArincRecordParser parser = ArincRecordParser.standard(
        dummySpec(11, List.of(RecordDiscriminator.prefix("ARINCRECORD")),
            new RecordField<>("field1", new BlankSpec(11)))
    );

    assertEquals("ARINCRECORD", parser.parse("ARINCRECORD  ").orElseThrow().rawRecord());
    assertThrows(IllegalArgumentException.class, () -> parser.parse("ARINCRECORD-plus-more"));
    assertThrows(IllegalArgumentException.class, () -> parser.parse("ARINCRECORD\t"));
    assertThrows(IllegalArgumentException.class, () -> parser.parse("SHORT"));
  }

  @Test
  void testParserReturnsEmptyWithNoMatchingSpecs() {

    ArincRecordParser parser = ArincRecordParser.standard(
        dummySpec(11, List.of(RecordDiscriminator.prefix("OTHER")),
            new RecordField<>("field1", new BlankSpec(11)))
    );

    assertEquals(Optional.empty(), parser.parse("ARINCRECORD"));
  }

  @Test
  void testParserBreaksIfInputSpecsDontMatchExpectedSizes() {

    RecordSpec goodSpec = dummySpec(11, List.of(RecordDiscriminator.prefix("ARINCRECORD")),
        new RecordField<>("field1", new BlankSpec(11)));
    RecordSpec badSpec = dummySpec(12, List.of(RecordDiscriminator.prefix("ARINCRECORD")),
        new RecordField<>("field1", new BlankSpec(12)));

    assertThrows(IllegalArgumentException.class, () -> ArincRecordParser.standard(goodSpec, badSpec));
  }

  @Test
  void testParserBreaksIfInputSpecsContainDuplicateFields() {

    RecordSpec badSpec = dummySpec(
        11,
        List.of(RecordDiscriminator.prefix("ARINCRECORD")),
        new RecordField<>("field1", new BlankSpec(5)),
        new RecordField<>("field1", new BlankSpec(6))
    );

    assertThrows(IllegalArgumentException.class, () -> ArincRecordParser.standard(badSpec));
  }

  private static RecordSpec dummySpec(
      int size,
      List<RecordDiscriminator> discriminators,
      RecordField<?>... fields
  ) {
    return new RecordSpec(discriminators) {
      @Override
      public int recordLength() {
        return size;
      }

      @Override
      public List<RecordField<?>> recordFields() {
        return Arrays.asList(fields);
      }
    };
  }

  private static final class CountingFieldSpec implements FieldSpec<String> {

    private final int fieldLength;
    private final boolean returnsEmpty;
    private final AtomicInteger parseCount = new AtomicInteger();

    private CountingFieldSpec(int fieldLength) {
      this(fieldLength, false);
    }

    private CountingFieldSpec(int fieldLength, boolean returnsEmpty) {
      this.fieldLength = fieldLength;
      this.returnsEmpty = returnsEmpty;
    }

    @Override
    public int fieldLength() {
      return fieldLength;
    }

    @Override
    public String fieldCode() {
      return "TEST";
    }

    @Override
    public Optional<String> parse(String source, int startOffset, int endOffset) {
      parseCount.incrementAndGet();
      return returnsEmpty ? Optional.empty() : Optional.of(source.substring(startOffset, endOffset));
    }

    private int parseCount() {
      return parseCount.get();
    }
  }
}
