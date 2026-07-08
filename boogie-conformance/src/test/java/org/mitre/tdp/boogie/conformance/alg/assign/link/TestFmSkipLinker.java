package org.mitre.tdp.boogie.conformance.alg.assign.link;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;

class TestFmSkipLinker {

  static Fix fixA = Fix.builder().fixIdentifier("ALPHA").latLong(LatLong.of(42.0, -71.0)).build();
  static Fix fixB = Fix.builder().fixIdentifier("BRAVO").latLong(LatLong.of(43.0, -71.0)).build();
  static Fix fixC = Fix.builder().fixIdentifier("CHARLI").latLong(LatLong.of(44.0, -71.0)).build();

  static Leg tfLegA = Leg.builder(PathTerminator.TF, 1).associatedFix(fixA).build();
  static Leg fmLegB = Leg.builder(PathTerminator.FM, 2).associatedFix(fixB).build();
  static Leg ifLegB = Leg.builder(PathTerminator.IF, 3).associatedFix(fixB).build();
  static Leg ifLegC = Leg.builder(PathTerminator.IF, 4).associatedFix(fixC).build();

  static Leg tfLegC = Leg.builder(PathTerminator.TF, 5).associatedFix(fixC).build();
  static Leg fmLegC = Leg.builder(PathTerminator.FM, 6).associatedFix(fixC).build();

  FmSkipLinker linker = new FmSkipLinker();

  @Test
  void createsSkipLink() {
    FlyableLeg tfFlyable = new FlyableLeg(null, tfLegA, fmLegB);
    FlyableLeg fmFlyable = new FlyableLeg(tfLegA, fmLegB, null);
    FlyableLeg ifFlyable = new FlyableLeg(null, ifLegB, null);

    Collection<Pair<FlyableLeg, FlyableLeg>> links = linker.apply(
        List.of(tfFlyable, fmFlyable),
        List.of(ifFlyable)
    );

    assertAll("Simple case where the legs should line up",
        () -> assertEquals(1, links.size()),
        () -> assertEquals(tfFlyable, links.iterator().next().first()),
        () -> assertEquals(ifFlyable, links.iterator().next().second())
    );
  }

  @Test
  void noLinkWhenNoFmInNext() {
    FlyableLeg fmFlyable = new FlyableLeg(tfLegA, fmLegB, null);
    FlyableLeg ifFlyable = new FlyableLeg(null, ifLegB, null);

    Collection<Pair<FlyableLeg, FlyableLeg>> links = linker.apply(
        List.of(fmFlyable),
        List.of(ifFlyable)
    );

    assertEquals(0, links.size(), "make sure we are actually before the FM");
  }

  @Test
  void noLinkWhenFixDiffers() {
    FlyableLeg tfFlyable = new FlyableLeg(null, tfLegA, fmLegB);
    FlyableLeg fmFlyable = new FlyableLeg(tfLegA, fmLegB, null);
    FlyableLeg ifFlyable = new FlyableLeg(null, ifLegC, null);

    Collection<Pair<FlyableLeg, FlyableLeg>> links = linker.apply(
        List.of(tfFlyable, fmFlyable),
        List.of(ifFlyable)
    );

    assertEquals(0, links.size(), "The null case yay");
  }

  @Test
  void multipleLinks() {
    FlyableLeg tfFlyableB = new FlyableLeg(null, tfLegA, fmLegB);
    FlyableLeg fmFlyableB = new FlyableLeg(tfLegA, fmLegB, null);
    FlyableLeg tfFlyableC = new FlyableLeg(null, tfLegC, fmLegC);
    FlyableLeg fmFlyableC = new FlyableLeg(tfLegC, fmLegC, null);

    FlyableLeg ifFlyableB = new FlyableLeg(null, ifLegB, null);
    FlyableLeg ifFlyableC = new FlyableLeg(null, ifLegC, null);

    Collection<Pair<FlyableLeg, FlyableLeg>> links = linker.apply(
        List.of(tfFlyableB, fmFlyableB, tfFlyableC, fmFlyableC),
        List.of(ifFlyableB, ifFlyableC)
    );

    assertEquals(2, links.size(), "Can link to more than one path in the downstream.");
  }
}
