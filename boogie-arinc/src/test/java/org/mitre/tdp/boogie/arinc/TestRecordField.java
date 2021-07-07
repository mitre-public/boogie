package org.mitre.tdp.boogie.arinc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.v18.field.ComponentElevation;

class TestRecordField {

  @Test
  void testRecordFieldToString() {
    RecordField<Double> field = new RecordField<>("myDouble", new ComponentElevation());
    assertEquals("RecordField{fieldName=myDouble,fieldSpec=ComponentElevation}", field.toString(), "Format of RecordField when printed should be readable or logging and errors.");
  }

  @Test
  void testRecordFieldEnforcesEnumSpecNaming() {
    new RecordField<>(GoodEnumSpec.SPEC);
    assertThrows(IllegalArgumentException.class, () -> new RecordField<>(BadEnumSpec.VAL1));
  }

  @Test
  void testRecordFieldExpectsNonNullConstructorArguments() {
    assertAll(
        () -> assertThrows(NullPointerException.class, () -> new RecordField<>(null, GoodEnumSpec.SPEC)),
        () -> assertThrows(NullPointerException.class, () -> new RecordField<>("field1", null))
    );
  }

  enum GoodEnumSpec implements FieldSpec<Void> {
    SPEC,
    VAL1;

    @Override
    public int fieldLength() {
      return 0;
    }

    @Override
    public String fieldCode() {
      return "2.1.5";
    }

    @Override
    public Void parseValue(String fieldValue) {
      return null;
    }
  }

  enum BadEnumSpec implements FieldSpec<Void> {
    VAL1;

    @Override
    public int fieldLength() {
      return 0;
    }

    @Override
    public String fieldCode() {
      return "2.1.5";
    }

    @Override
    public Void parseValue(String fieldValue) {
      return null;
    }
  }
}
