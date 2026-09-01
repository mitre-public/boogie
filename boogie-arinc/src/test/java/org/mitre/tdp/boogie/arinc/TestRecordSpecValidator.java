package org.mitre.tdp.boogie.arinc;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.v18.field.BlankSpec;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

  @Test
  void testFailsSpecWithoutUsableDiscriminators() {
    RecordField<?> field = new RecordField<>("field1", new BlankSpec(10));

    assertAll(
        () -> assertThrows(IllegalArgumentException.class,
            () -> RecordSpecValidator.INSTANCE.accept(dummySpec(10, x -> true, List.of(), field))),
        () -> assertThrows(IllegalArgumentException.class,
            () -> RecordSpecValidator.INSTANCE.accept(
                dummySpec(10, x -> true, (List<RecordDiscriminator>) null, field))),
        () -> assertThrows(IllegalArgumentException.class,
            () -> RecordSpecValidator.INSTANCE.accept(dummySpec(10, x -> true, Arrays.asList(RecordDiscriminator.prefix("X"), null), field)))
    );
  }

  @Test
  void testFailsSpecWithDiscriminatorOutsideRecordWidth() {
    RecordField<?> field = new RecordField<>("field1", new BlankSpec(5));

    assertAll(
        () -> assertThrows(IllegalArgumentException.class,
            () -> RecordSpecValidator.INSTANCE.accept(dummySpec(5, x -> true, List.of(RecordDiscriminator.column6('E', 'R')), field))),
        () -> assertThrows(IllegalArgumentException.class,
            () -> RecordSpecValidator.INSTANCE.accept(dummySpec(5, x -> true, List.of(RecordDiscriminator.column13('P', 'C')), field))),
        () -> assertThrows(IllegalArgumentException.class,
            () -> RecordSpecValidator.INSTANCE.accept(dummySpec(5, x -> true, List.of(RecordDiscriminator.prefix("TOO-LONG")), field)))
    );
  }

  private static RecordSpec dummySpec(int size, Predicate<String> matcher, RecordField<?>... fields) {
    return dummySpec(size, matcher, List.of(RecordDiscriminator.prefix("X")), fields);
  }

  private static RecordSpec dummySpec(
      int size,
      Predicate<String> matcher,
      List<RecordDiscriminator> discriminators,
      RecordField<?>... fields
  ) {
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
      public List<RecordDiscriminator> recordDiscriminators() {
        return discriminators;
      }

      @Override
      public boolean matchesRecord(String arincRecord) {
        return matcher.test(arincRecord);
      }
    };
  }
}
