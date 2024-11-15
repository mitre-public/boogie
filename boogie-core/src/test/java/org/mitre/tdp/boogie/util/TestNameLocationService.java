package org.mitre.tdp.boogie.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;

class TestNameLocationService {

  @Test
  void testNameLookupFindsSingleMatchFor_A() {
    NameLocationService<Fix> nls = abccService();
    Collection<Fix> matches = nls.matches("A");

    assertEquals(1, matches.size());
  }

  @Test
  void testNameLookupFindsSingleMatchFor_B() {
    NameLocationService<Fix> nls = abccService();
    Collection<Fix> matches = nls.matches("B");

    assertEquals(1, matches.size());
  }

  @Test
  void testNameLookupFindsTwoMatchesFor_C() {
    NameLocationService<Fix> nls = abccService();
    Collection<Fix> matches = nls.matches("C");

    assertEquals(2, matches.size());
  }

  @Test
  void testNameLookupFindsNoMatchFor_D() {
    NameLocationService<Fix> nls = abccService();
    Collection<Fix> matches = nls.matches("D");

    assertEquals(0, matches.size());
  }

  private Fix fix(String id, LatLong loc) {
    Fix fix = mock(Fix.class);
    when(fix.fixIdentifier()).thenReturn(id);
    when(fix.latLong()).thenReturn(loc);
    return fix;
  }

  private NameLocationService<Fix> abccService() {
    Fix f1 = fix("A", LatLong.of(0.0, 0.0));
    Fix f2 = fix("B", LatLong.of(0.0, 1.0));
    Fix f3 = fix("C", LatLong.of(0.0, -2.0));
    Fix f4 = fix("C", LatLong.of(0.0, -2.1));
    return NameLocationService.from(Arrays.asList(f1, f2, f3, f4), Fix::fixIdentifier, Fix::latLong);
  }

  @Test
  void testNearestFixTo0_0Is_A() {
    NameLocationService<Fix> nls = abcService();
    Fix nearest = nls.nearest(LatLong.of(0.0, 0.3));

    assertEquals("A", nearest.fixIdentifier());
  }

  @Test
  void testNearestFixTo0_Neg15Is_C() {
    NameLocationService<Fix> nls = abcService();
    Fix nearest = nls.nearest(LatLong.of(0.0, -1.5));

    assertEquals("C", nearest.fixIdentifier());
  }

  @Test
  void testNearestFixToItselfIsItself() {
    NameLocationService<Fix> nls = abcService();
    LatLong locationA = LatLong.of(0.0, 0.0);

    Fix nearest = nls.nearest(locationA);
    assertEquals("A", nearest.fixIdentifier());

    nearest = nls.nearest(nearest.latLong());
    assertEquals("A", nearest.fixIdentifier());
  }

  private NameLocationService<Fix> abcService() {
    Fix f1 = fix("A", LatLong.of(0.0, 0.0));
    Fix f2 = fix("B", LatLong.of(0.0, 1.0));
    Fix f3 = fix("C", LatLong.of(0.0, -2.0));
    return NameLocationService.from(Arrays.asList(f1, f2, f3), Fix::fixIdentifier, Fix::latLong);
  }

  @Test
  void testAllFixesWithin5_5nmIs6Fixes() {
    NameLocationService<Fix> nls = fixPerNmService(10);

    List<Fix> allFixes = nls.allWithinRange(LatLong.of(0.0, 0.0), 5.5);
    assertEquals(6, allFixes.size());
  }

  private NameLocationService<Fix> fixPerNmService(int rings) {
    LatLong origin = LatLong.of(0.0, 0.0);
    List<Fix> fixes = IntStream.range(0, rings).boxed()
        .map(i -> {
          LatLong proj = origin.projectOut(0.0, (double) i);
          return fix(String.valueOf((char) (65 + i)), proj);
        })
        .collect(Collectors.toList());
    return NameLocationService.from(fixes, Fix::fixIdentifier, Fix::latLong);
  }
}
