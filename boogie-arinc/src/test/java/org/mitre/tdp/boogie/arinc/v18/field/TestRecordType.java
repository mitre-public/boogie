package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestRecordType {

  @Test
  void testParseGoodRecordType() {
    assertEquals(Optional.of(RecordType.T), RecordType.SPEC.apply("T"));
  }

  @Test
  void testEmptyOnBadRecordType() {
    assertEquals(Optional.empty(), RecordType.SPEC.apply("A"));
  }
}
