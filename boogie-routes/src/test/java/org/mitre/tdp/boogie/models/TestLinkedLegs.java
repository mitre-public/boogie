package org.mitre.tdp.boogie.models;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.alg.resolve.SectionSplitLeg;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mitre.tdp.boogie.MockObjects.CA;
import static org.mitre.tdp.boogie.MockObjects.TF;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestLinkedLegs {

  @Test
  public void testSameSectionLinkWeight() {
    SectionSplitLeg ssl1 = mock(SectionSplitLeg.class);

    Leg l1 = TF("HRRDR", 0.0, 1.0);

    SectionSplit ss1 = mock(SectionSplit.class);
    when(ss1.value()).thenReturn("HELP");

    when(ssl1.leg()).thenReturn(l1);
    when(ssl1.sectionSplit()).thenReturn(ss1);

    SectionSplitLeg ssl2 = mock(SectionSplitLeg.class);
    Leg l2 = TF("GRRDR", 0.0, 2.0);

    when(ssl2.leg()).thenReturn(l2);
    when(ssl2.sectionSplit()).thenReturn(ss1);

    LinkedLegs ll = new LinkedLegs(ssl1, ssl2);
    assertEquals(0.0, ll.linkWeight(), 0.1, "Same section fixes should be zero weight.");
  }

  @Test
  public void testDifferentSectionLinkWeightConcrete() {
    SectionSplitLeg ssl1 = mock(SectionSplitLeg.class);

    Leg l1 = TF("HRRDR", 0.0, 1.0);

    SectionSplit ss1 = mock(SectionSplit.class);
    when(ss1.value()).thenReturn("HELP");

    when(ssl1.leg()).thenReturn(l1);
    when(ssl1.sectionSplit()).thenReturn(ss1);

    SectionSplitLeg ssl2 = mock(SectionSplitLeg.class);
    Leg l2 = TF("GRRDR", 0.0, 2.0);

    SectionSplit ss2 = mock(SectionSplit.class);
    when(ss2.value()).thenReturn("HELPME");

    when(ssl2.leg()).thenReturn(l2);
    when(ssl2.sectionSplit()).thenReturn(ss2);

    LinkedLegs ll = new LinkedLegs(ssl1, ssl2);
    assertEquals(60.007, ll.linkWeight(), 0.1, "Different section link weight should be non-zero where concrete fixes present (we can compute a distance).");
  }

  @Test
  public void testDifferentSectionLinkWeightNonConcrete() {
    SectionSplitLeg ssl1 = mock(SectionSplitLeg.class);

    Leg l1 = TF("HRRDR", 0.0, 1.0);

    SectionSplit ss1 = mock(SectionSplit.class);
    when(ss1.value()).thenReturn("HELP");

    when(ssl1.leg()).thenReturn(l1);
    when(ssl1.sectionSplit()).thenReturn(ss1);

    SectionSplitLeg ssl2 = mock(SectionSplitLeg.class);
    Leg l2 = CA();

    SectionSplit ss2 = mock(SectionSplit.class);
    when(ss2.value()).thenReturn("HELPME");

    when(ssl2.leg()).thenReturn(l2);
    when(ssl2.sectionSplit()).thenReturn(ss2);

    LinkedLegs ll = new LinkedLegs(ssl1, ssl2);
    assertEquals(LinkedLegs.MATCH_WEIGHT, ll.linkWeight(), 0.0, "Different sections where non-concrete legs are linked we default weight to zero (can't compute distance).");
  }
}
