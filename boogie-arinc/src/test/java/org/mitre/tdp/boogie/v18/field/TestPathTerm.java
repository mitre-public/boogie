package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.TurnDirection;

public class TestPathTerm {

  @Test
  public void testParseGoodPathTerminator() {
    assertEquals(PathTerm.AF, new org.mitre.tdp.boogie.v18.spec.field.PathTerm().parseValue("AF"));
  }

  @Test
  public void testParseExceptionOnBadPathTerminator() {
    assertThrows(FieldSpecParseException.class, () -> new org.mitre.tdp.boogie.v18.spec.field.PathTerm().parseValue("RA"));
  }

  @Test
  public void testAfRequiresRecommendedNavaidLocation() {
    Fix locationFix = fixWithLocation();
    Fix noLocationFix = fixWithoutLocation();

    Leg leg = mock(Leg.class);
    when(leg.type()).thenReturn(PathTerm.AF);
    when(leg.pathTerminator()).thenReturn(locationFix);
    when(leg.theta()).thenReturn(Optional.of(270.0));
    when(leg.rho()).thenReturn(Optional.of(310.0));
    when(leg.turnDirection()).thenReturn((Optional) Optional.of(TurnDirection.right()));
    when(leg.recommendedNavaid()).thenReturn((Optional) Optional.of(noLocationFix));

    assertFalse(PathTerm.AF.hasRequiredFields(leg));

    when(leg.recommendedNavaid()).thenReturn((Optional) Optional.of(locationFix));

    assertTrue(PathTerm.AF.hasRequiredFields(leg));
  }

  @Test
  public void testRfRequiresCenterFixLocation() {
    Fix locationFix = fixWithLocation();
    Fix noLocationFix = fixWithoutLocation();

    Leg leg = mock(Leg.class);
    when(leg.type()).thenReturn(PathTerm.RF);
    when(leg.pathTerminator()).thenReturn(locationFix);
    when(leg.routeDistance()).thenReturn(Optional.of(10.0));
    when(leg.turnDirection()).thenReturn((Optional) Optional.of(TurnDirection.right()));
    when(leg.centerFix()).thenReturn((Optional) Optional.of(noLocationFix));

    assertFalse(PathTerm.RF.hasRequiredFields(leg));

    when(leg.centerFix()).thenReturn((Optional) Optional.of(locationFix));

    assertTrue(PathTerm.RF.hasRequiredFields(leg));
  }

  private Fix fixWithLocation() {
    Fix fix = mock(Fix.class);
    when(fix.latLong()).thenReturn(LatLong.of(0.0, 0.0));
    when(fix.latitude()).thenCallRealMethod();
    when(fix.longitude()).thenCallRealMethod();
    return fix;
  }

  private Fix fixWithoutLocation() {
    Fix fix = mock(Fix.class);
    return fix;
  }
}
