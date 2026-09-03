package org.mitre.tdp.boogie.arinc;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.v18.field.BlankSpec;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestRecordSpecValidator {

  @Test
  void testPassesGoodRecordSpec() {
    RecordSpec goodSpec = dummySpec(10, new RecordField<>("field1", new BlankSpec(10)));
    RecordSpecValidator.INSTANCE.accept(goodSpec);
  }

  @Test
  void testFailsSpecWithExpectedRecordLengthBelowZero() {
    RecordSpec badSpec = dummySpec(-1, new RecordField<>("field1", new BlankSpec(10)));
    assertThrows(IllegalArgumentException.class, () -> RecordSpecValidator.INSTANCE.accept(badSpec));
  }

  @Test
  void testFailsSpecWithMismatchedRecordAndFieldLengths() {
    RecordSpec badSpec = dummySpec(1, new RecordField<>("field1", new BlankSpec(10)));
    assertThrows(IllegalArgumentException.class, () -> RecordSpecValidator.INSTANCE.accept(badSpec));
  }

  @Test
  void testFailsSpecWithDuplicateFieldNames() {
    RecordSpec badSpec = dummySpec(
        11,
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
            () -> RecordSpecValidator.INSTANCE.accept(dummySpec(10, List.of(), field))),
        () -> assertThrows(NullPointerException.class,
            () -> dummySpec(10, (List<RecordDiscriminator>) null, field)),
        () -> assertThrows(NullPointerException.class,
            () -> dummySpec(10, Arrays.asList(RecordDiscriminator.prefix("X"), null), field))
    );
  }

  @Test
  void testFailsSpecWithDiscriminatorOutsideRecordWidth() {
    RecordField<?> fiveCharacterField = new RecordField<>("field1", new BlankSpec(5));
    RecordField<?> thirteenCharacterField = new RecordField<>("field1", new BlankSpec(13));

    assertAll(
        () -> assertThrows(IllegalArgumentException.class,
            () -> RecordSpecValidator.INSTANCE.accept(dummySpec(
                5, List.of(RecordDiscriminator.primaryColumn6('E', 'R', 0)), fiveCharacterField))),
        () -> assertThrows(IllegalArgumentException.class,
            () -> RecordSpecValidator.INSTANCE.accept(dummySpec(
                5, List.of(RecordDiscriminator.primaryColumn13('P', 'C', 0)), fiveCharacterField))),
        () -> assertThrows(IllegalArgumentException.class,
            () -> RecordSpecValidator.INSTANCE.accept(dummySpec(5, List.of(RecordDiscriminator.prefix("TOO-LONG")), fiveCharacterField))),
        () -> assertThrows(IllegalArgumentException.class,
            () -> RecordSpecValidator.INSTANCE.accept(dummySpec(
                13, List.of(RecordDiscriminator.primaryColumn6('E', 'R', 13)), thirteenCharacterField))),
        () -> assertThrows(IllegalArgumentException.class,
            () -> RecordSpecValidator.INSTANCE.accept(dummySpec(
                13,
                List.of(RecordDiscriminator.continuationColumn6('E', 'R', 12, 'E')),
                thirteenCharacterField)))
    );
  }

  private static RecordSpec dummySpec(int size, RecordField<?>... fields) {
    return dummySpec(size, List.of(RecordDiscriminator.prefix("X")), fields);
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
}
