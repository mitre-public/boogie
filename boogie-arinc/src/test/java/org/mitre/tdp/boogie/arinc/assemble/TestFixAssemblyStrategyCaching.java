package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.model.ArincWaypoint;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

class TestFixAssemblyStrategyCaching {

  @Test
  void testIdentityCachingReusesOnlyTheSameInputInstance() {
    ArincWaypoint first = waypoint();
    ArincWaypoint equalButDistinct = first.toBuilder().build();
    FixAssemblyStrategy<Object> delegate = strategy();
    when(delegate.convertWaypoint(any())).thenAnswer(invocation -> new Object());

    FixAssemblyStrategy<Object> caching = FixAssemblyStrategy.identityCaching(delegate);
    Object firstConversion = caching.convertWaypoint(first);
    Object repeatedConversion = caching.convertWaypoint(first);
    Object distinctConversion = caching.convertWaypoint(equalButDistinct);

    assertAll(
        () -> assertEquals(first, equalButDistinct),
        () -> assertNotSame(first, equalButDistinct),
        () -> assertSame(firstConversion, repeatedConversion),
        () -> assertNotSame(firstConversion, distinctConversion),
        () -> verify(delegate, times(2)).convertWaypoint(any())
    );
  }

  @Test
  void testExistingCachingContinuesToUseStructuralEquality() {
    ArincWaypoint first = waypoint();
    ArincWaypoint equalButDistinct = first.toBuilder().build();
    FixAssemblyStrategy<Object> delegate = strategy();
    when(delegate.convertWaypoint(any())).thenAnswer(invocation -> new Object());

    FixAssemblyStrategy<Object> caching = FixAssemblyStrategy.caching(delegate);
    Object firstConversion = caching.convertWaypoint(first);
    Object distinctConversion = caching.convertWaypoint(equalButDistinct);

    assertAll(
        () -> assertSame(firstConversion, distinctConversion),
        () -> verify(delegate).convertWaypoint(first)
    );
  }

  @Test
  void testIdentityCachingDoesNotCacheNullConversions() {
    ArincWaypoint waypoint = waypoint();
    FixAssemblyStrategy<Object> delegate = strategy();
    when(delegate.convertWaypoint(waypoint)).thenReturn(null);

    FixAssemblyStrategy<Object> caching = FixAssemblyStrategy.identityCaching(delegate);

    assertAll(
        () -> assertNull(caching.convertWaypoint(waypoint)),
        () -> assertNull(caching.convertWaypoint(waypoint)),
        () -> verify(delegate, times(2)).convertWaypoint(waypoint)
    );
  }

  @Test
  void testIdentityCachingValidatesAndAvoidsRedecorating() {
    FixAssemblyStrategy<Object> delegate = strategy();
    FixAssemblyStrategy<Object> caching = FixAssemblyStrategy.identityCaching(delegate);

    assertAll(
        () -> assertSame(caching, FixAssemblyStrategy.identityCaching(caching)),
        () -> assertThrows(NullPointerException.class, () -> FixAssemblyStrategy.identityCaching(null))
    );
  }

  @SuppressWarnings("unchecked")
  private static FixAssemblyStrategy<Object> strategy() {
    return mock(FixAssemblyStrategy.class);
  }

  private static ArincWaypoint waypoint() {
    return new ArincWaypoint.Builder()
        .sectionCode(SectionCode.E)
        .enrouteSubSectionCode("A")
        .waypointIdentifier("DUP")
        .waypointIcaoRegion("K2")
        .latitude(38.)
        .longitude(-77.)
        .fileRecordNumber(1)
        .lastUpdateCycle("2608")
        .build();
  }
}
