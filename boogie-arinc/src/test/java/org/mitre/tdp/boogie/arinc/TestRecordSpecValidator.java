package org.mitre.tdp.boogie.arinc;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.v18.field.BlankSpec;

class TestRecordSpecValidator {

  @Test
  void testPassesGoodRecordSpec() {
    RecordSpec goodSpec = dummySpec(10, x -> true, new RecordField<>("field1", new BlankSpec(10)));
    RecordSpecValidator.INSTANCE.accept(goodSpec);
  }

  @Test
  void testFailsSpecWithExpectedRecordLengthBelowZero() {
    RecordSpec badSpec = dummySpec(-1, x -> true, new RecordField<>("field1", new BlankSpec(10)));
    assertThrows(IllegalArgumentException.class, () -> RecordSpecValidator.INSTANCE.accept(badSpec));
  }

  @Test
  void testFailsSpecWithMismatchedRecordAndFieldLengths() {
    RecordSpec badSpec = dummySpec(1, x -> true, new RecordField<>("field1", new BlankSpec(10)));
    assertThrows(IllegalArgumentException.class, () -> RecordSpecValidator.INSTANCE.accept(badSpec));
  }

  @Test
  void testFailsSpecWithDuplicateFieldNames() {
    RecordSpec badSpec = dummySpec(
        11,
        x -> true,
        new RecordField<>("field1", new BlankSpec(5)),
        new RecordField<>("field1", new BlankSpec(6))
    );

    assertThrows(IllegalArgumentException.class, () -> RecordSpecValidator.INSTANCE.accept(badSpec));
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
