package org.mitre.tdp.boogie;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.v18.spec.field.BlankSpec;

public class TestArincParser {

  private RecordSpec dummySpec(int size, ArincField... fields) {
    return new RecordSpec() {
      @Override
      public int recordLength() {
        return size;
      }

      @Override
      public List<ArincField<?>> recordFields() {
        return Arrays.asList(fields);
      }

      @Override
      public boolean matchesRecord(String arincRecord) {
        return true;
      }
    };
  }

  @Test
  public void testParserThrowsExceptionWhenRecordLengthDoesntMatchSpecLength() {
    ArincSpec spec = () -> Collections.singletonList(dummySpec(10, ArincField.newField("", new BlankSpec(10))));
    ArincParser parser = () -> spec;
    assertThrows(IllegalArgumentException.class, () -> parser.parse("0"));
  }
}
