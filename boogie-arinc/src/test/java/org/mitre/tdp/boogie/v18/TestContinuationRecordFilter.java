package org.mitre.tdp.boogie.v18;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.ArincRecord;
import org.mitre.tdp.boogie.ArincRecordDecorator;

public class TestContinuationRecordFilter {

  @Test
  public void testRecordNumberLessThanOrEqualTo1IsNotContinuation() {
    assertFalse(ContinuationRecordFilter.newInstance().isContinuationRecord("1"));
  }

  @Test
  public void testRecordNumberGreaterThanOrEqualTo1IsContinuation() {
    assertTrue(ContinuationRecordFilter.newInstance().isContinuationRecord("2"));
  }

  @Test
  public void testRecordNumberIsAlphaIsContinuation() {
    assertTrue(ContinuationRecordFilter.newInstance().isContinuationRecord("a"));
  }

  @Test
  public void testFilterToNonContinuationRecords() {
    ArincRecord record1 = mock(ArincRecord.class);
    when(record1.getOptionalField(matches("continuationRecordNumber"))).thenReturn(Optional.of("1"));

    ArincRecord record2 = mock(ArincRecord.class);
    when(record2.getOptionalField(matches("continuationRecordNumber"))).thenReturn(Optional.of("2"));

    ArincRecord record3 = mock(ArincRecord.class);
    when(record3.getOptionalField(matches("continuationRecordNumber"))).thenReturn(Optional.of("a"));

    ArincRecord record4 = mock(ArincRecord.class);
    when(record4.getOptionalField(matches("continuationRecordNumber"))).thenReturn(Optional.empty());

    long total = Stream.of(record1, record2, record3, record4)
        .map(record -> {
          ArincRecordDecorator decorator = () -> record;
          return decorator;
        })
        .filter(ContinuationRecordFilter.newInstance())
        .count();

    assertEquals(1, total);
  }
}
