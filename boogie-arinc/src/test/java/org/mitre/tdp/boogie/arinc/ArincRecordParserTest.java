package org.mitre.tdp.boogie.arinc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
}
