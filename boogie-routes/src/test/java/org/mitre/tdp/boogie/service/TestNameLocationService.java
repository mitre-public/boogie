package org.mitre.tdp.boogie.service;

import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.service.impl.NameLocationService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestNameLocationService {

  private Fix fix(String id, LatLong loc) {
    Fix fix = mock(Fix.class);
    when(fix.identifier()).thenReturn(id);
    when(fix.latLong()).thenReturn(loc);
    return fix;
  }

  @Test
  public void testIdentifierLookup() {
    Fix f1 = fix("A", LatLong.of(0.0, 0.0));
    Fix f2 = fix("B", LatLong.of(0.0, 1.0));
    Fix f3 = fix("C", LatLong.of(0.0, -2.0));
    Fix f4 = fix("C", LatLong.of(0.0, -2.1));

    NameLocationService<Fix> nls = NameLocationService.from(Arrays.asList(f1, f2, f3, f4), Fix::identifier, Fix::latLong);

    Collection<Fix> matches = nls.matches("A");
    assertEquals(1, matches.size());

    matches = nls.matches("B");
    assertEquals(1, matches.size());

    matches = nls.matches("C");
    assertEquals(2, matches.size());

    matches = nls.matches("D");
    assertEquals(0, matches.size());
  }

  @Test
  public void testLocationLookup() {
    Fix f1 = fix("A", LatLong.of(0.0, 0.0));
    Fix f2 = fix("B", LatLong.of(0.0, 1.0));
    Fix f3 = fix("C", LatLong.of(0.0, -2.0));

    NameLocationService<Fix> nls = NameLocationService.from(Arrays.asList(f1, f2, f3), Fix::identifier, Fix::latLong);

    Fix nearest = nls.nearest(LatLong.of(0.0, 0.3));
    assertEquals(nearest.identifier(), "A");

    nearest = nls.nearest(LatLong.of(0.0, -1.5));
    assertEquals(nearest.identifier(), "C");

    nearest = nls.nearest(f1.latLong());
    assertEquals(nearest.identifier(), f1.identifier());
  }
}
