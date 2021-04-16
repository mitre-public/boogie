package org.mitre.tdp.boogie.arinc;

import java.util.Arrays;
import java.util.List;

class TestArincParser {

//  @Test
//  void testParserThrowsExceptionWhenRecordLengthDoesntMatchSpecLength() {
//    ArincParser parser = new ArincParser(dummySpec(10, new RecordField<>("", new BlankSpec(10))));
//    assertThrows(IllegalArgumentException.class, () -> parser.apply("0"));
//  }

  private RecordSpec dummySpec(int size, RecordField... fields) {
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
        return true;
      }
    };
  }
}
