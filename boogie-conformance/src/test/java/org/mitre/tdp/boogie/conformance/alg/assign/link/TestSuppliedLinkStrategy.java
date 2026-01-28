package org.mitre.tdp.boogie.conformance.alg.assign.link;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.Route;

class TestSuppliedLinkStrategy {
  static LatLong one = LatLong.of(0.0, 0.0);
  static LatLong two = LatLong.of(1.0, 0.0);
  static LatLong three = LatLong.of(2.0, 0.0);

  static Fix fix1 = Fix.builder().fixIdentifier("ONE").latLong(one).build();
  static Fix fix2 = Fix.builder().fixIdentifier("TWO").latLong(two).build();
  static Fix fix3 = Fix.builder().fixIdentifier("THREE").latLong(three).build();

  static Leg ifLeg = Leg.ifBuilder(fix1, 0).build();
  static Leg tfLeg = Leg.tfBuilder(fix2, 2).build();
  static Leg tfLeg2 = Leg.tfBuilder(fix3, 3).build();

  static String routeSource = "SOURCE_1";
  static String routeSource2 = "SOURCE_2";

  static Route route1 = Route.newRoute(List.of(ifLeg, tfLeg), routeSource);
  static Route route2 = Route.newRoute(List.of(tfLeg2), routeSource2);

  @Test
  void testLink() {
    SuppliedLinkStrategy leftToRight = SuppliedLinkStrategy.leftToRight();
    List<Pair<FlyableLeg, FlyableLeg>> link = leftToRight.generateLinks(route1, route2);
    assertAll(
        () -> assertEquals(1, link.size()),
        () -> assertEquals("TWO", link.get(0).first().current().associatedFix().orElseThrow().fixIdentifier()),
        () -> assertEquals("THREE", link.get(0).second().current().associatedFix().orElseThrow().fixIdentifier())
    );
  }
}