package org.mitre.tdp.boogie.conformance.alg.assign.combine;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.Route;

public class TestHybridHasher {
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

  static Route<String> route1 = Route.newRoute(List.of(ifLeg, tfLeg, tfLeg2), routeSource);
  static FlyableLeg flyableLeg = new FlyableLeg(null, ifLeg, tfLeg, route1);
  static FlyableLeg flyableLeg2 = new FlyableLeg(ifLeg, tfLeg, tfLeg2, route1);
  static FlyableLeg flyableLeg3 = new FlyableLeg(tfLeg, tfLeg2, null, route1);

  static PathTerminatorBasedLegHasher legHasher = PathTerminatorBasedLegHasher.newInstance();
  static RouteHasher routeHasher = RouteHasher.newInstance();
  static HybridHasher hybridHasher = HybridHasher.from(List.of(routeHasher, legHasher));

  @Test
  void testHybridHasher() {
    int flyableLegHas = hybridHasher.apply(flyableLeg);
    int flyableLegHas2 = hybridHasher.apply(flyableLeg2);
    int flyableLegHas3 = hybridHasher.apply(flyableLeg3);

    int legOnly = legHasher.apply(flyableLeg);
    int legOnly2 = legHasher.apply(flyableLeg2);
    int legOnly3 = legHasher.apply(flyableLeg3);

    assertAll(
        () -> assertNotEquals(flyableLegHas, legOnly),
        () -> assertNotEquals(flyableLegHas2, legOnly2),
        () -> assertNotEquals(flyableLegHas3, legOnly3)
    );
  }
}
