package org.mitre.tdp.boogie.arinc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.v18.field.BlankSpec;

class ArincRecordParserTest {

  @Test
  void testParserBreaksOnNullInputString() {

    ArincRecordParser parser = ArincRecordParser.standard(
        dummySpec(11, x -> true, new RecordField<>("field1", new BlankSpec(11)))
    );

    assertThrows(NullPointerException.class, () -> parser.parse(null));
  }

  @Test
  void testParserWithConfiguredSpec() {

    ArincRecordParser parser = ArincRecordParser.standard(
        dummySpec(11, x -> true, new RecordField<>("field1", new BlankSpec(11)))
    );

    ArincRecord actual = parser.parse("ARINCRECORD").orElseThrow();
    assertEquals("ARINCRECORD", actual.rawField("field1"));
  }

  @Test
  void testParserDecodesFieldsLazilyAndCachesParsedValues() {
    CountingFieldSpec fieldSpec = new CountingFieldSpec(11);
    ArincRecordParser parser = ArincRecordParser.standard(
        dummySpec(11, x -> true, new RecordField<>("field1", fieldSpec))
    );

    ArincRecord actual = parser.parse("ARINCRECORD").orElseThrow();
    assertEquals(0, fieldSpec.applyCount());
    assertEquals("ARINCRECORD", actual.rawRecord());
    assertEquals("ARINCRECORD", actual.rawField("field1"));
    assertEquals(0, fieldSpec.applyCount());

    assertEquals(Optional.of("ARINCRECORD"), actual.optionalField("field1"));
    assertEquals(Optional.of("ARINCRECORD"), actual.optionalField("field1"));
    assertEquals(1, fieldSpec.applyCount());
  }

  @Test
  void testParserPreservesFirstMatchingSpec() {
    ArincRecordParser parser = ArincRecordParser.standard(
        dummySpec(11, x -> true, new RecordField<>("first", new BlankSpec(11))),
        dummySpec(11, x -> true, new RecordField<>("second", new BlankSpec(11)))
    );

    ArincRecord actual = parser.parse("ARINCRECORD").orElseThrow();
    assertEquals("ARINCRECORD", actual.rawField("first"));
    assertFalse(actual.specForField("second").isPresent());
  }

  @Test
  void testParserPreservesRecordLengthBehavior() {
    ArincRecordParser parser = ArincRecordParser.standard(
        dummySpec(11, x -> true, new RecordField<>("field1", new BlankSpec(11)))
    );

    assertEquals("ARINCRECORD", parser.parse("ARINCRECORD-plus-more").orElseThrow().rawRecord());
    assertThrows(StringIndexOutOfBoundsException.class, () -> parser.parse("SHORT"));
  }

  @Test
  void testParserReturnsEmptyWithNoMatchingSpecs() {

    ArincRecordParser parser = ArincRecordParser.standard(
        dummySpec(11, x -> false, new RecordField<>("field1", new BlankSpec(11)))
    );

    assertEquals(Optional.empty(), parser.parse("ARINCRECORD"));
  }

  @Test
  void testParserBreaksIfInputSpecsDontMatchExpectedSizes() {

    RecordSpec goodSpec = dummySpec(11, x -> true, new RecordField<>("field1", new BlankSpec(11)));
    RecordSpec badSpec = dummySpec(12, x -> true, new RecordField<>("field1", new BlankSpec(11)));

    assertThrows(IllegalArgumentException.class, () -> ArincRecordParser.standard(goodSpec, badSpec));
  }

  @Test
  void testParserBreaksIfInputSpecsContainDuplicateFields() {

    RecordSpec badSpec = dummySpec(
        11,
        x -> true,
        new RecordField<>("field1", new BlankSpec(5)),
        new RecordField<>("field1", new BlankSpec(6))
    );

    assertThrows(IllegalArgumentException.class, () -> ArincRecordParser.standard(badSpec));
  }

  private RecordSpec dummySpec(int size, Predicate<String> matcher, RecordField<?>... fields) {
    return new RecordSpec() {
      @Override
      public int recordLength() {
        return size;
      }

      @Override
      public List<RecordField<?>> recordFields() {
        return Arrays.asList(fields);
      }

      @Override
      public boolean matchesRecord(String arincRecord) {
        return matcher.test(arincRecord);
      }
    };
  }

  private static final class CountingFieldSpec implements FieldSpec<String> {

    private final int fieldLength;
    private final AtomicInteger applyCount = new AtomicInteger();

    private CountingFieldSpec(int fieldLength) {
      this.fieldLength = fieldLength;
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
    public Optional<String> apply(String value) {
      applyCount.incrementAndGet();
      return Optional.of(value);
    }

    private int applyCount() {
      return applyCount.get();
    }
  }
}
