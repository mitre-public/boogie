package org.mitre.tdp.boogie.arinc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.utils.PrimaryRecord;

class TestContinuationRecordFilter {

  private final ContinuationRecordFilter continuationRecordFilter = new ContinuationRecordFilter();
  private final PrimaryRecord isPrimary = PrimaryRecord.INSTANCE;

  @Test
  void testRecordNumberLessThanOrEqualTo1IsNotContinuation() {
    assertTrue(isPrimary.test("1"));
  }

  @Test
  void testRecordNumberGreaterThanOrEqualTo1IsContinuation() {
    assertFalse(isPrimary.test("2"));
  }

  @Test
  void testRecordNumberIsAlphaIsContinuation() {
    assertFalse(isPrimary.test("a"));
  }

  @Test
  void testFilterToNonContinuationRecords() {
    ArincRecord record1 = mock(ArincRecord.class);
    when(record1.optionalField(matches("continuationRecordNumber"))).thenReturn(Optional.of("1"));

    ArincRecord record2 = mock(ArincRecord.class);
    when(record2.optionalField(matches("continuationRecordNumber"))).thenReturn(Optional.of("2"));

    ArincRecord record3 = mock(ArincRecord.class);
    when(record3.optionalField(matches("continuationRecordNumber"))).thenReturn(Optional.of("a"));

    ArincRecord record4 = mock(ArincRecord.class);
    when(record4.optionalField(matches("continuationRecordNumber"))).thenReturn(Optional.empty());

    ArincRecord record5 = mock(ArincRecord.class);
    when(record4.optionalField(matches("continuationRecordNumber"))).thenReturn(Optional.of("0"));

    long total = Stream.of(record1, record2, record3, record4, record5).filter(continuationRecordFilter).count();
    assertEquals(2, total, "zero, and 1 are primary records");
  }
}
