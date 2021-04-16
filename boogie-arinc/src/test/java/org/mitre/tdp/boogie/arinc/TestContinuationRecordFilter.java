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

class TestContinuationRecordFilter {

  private final ContinuationRecordFilter continuationRecordFilter = new ContinuationRecordFilter();

  @Test
  void testRecordNumberLessThanOrEqualTo1IsNotContinuation() {
    assertFalse(continuationRecordFilter.isContinuationRecord("1"));
  }

  @Test
  void testRecordNumberGreaterThanOrEqualTo1IsContinuation() {
    assertTrue(continuationRecordFilter.isContinuationRecord("2"));
  }

  @Test
  void testRecordNumberIsAlphaIsContinuation() {
    assertTrue(continuationRecordFilter.isContinuationRecord("a"));
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

    long total = Stream.of(record1, record2, record3, record4).filter(continuationRecordFilter).count();
    assertEquals(1, total);
  }
}
