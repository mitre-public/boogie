package org.mitre.tdp.boogie.alg.split;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class TestIfrFormatSectionSplitter {

  private final List<String> ROUTE0 = Arrays.asList(
      "KBDL.CSTL6.SHERL.J121.BRIGS.JIIMS2.KPHL/0054",
      "KBDL.CSTL6.SHERL.J121.BRIGS.JIIMS2.KPHL/1814",
      "KBDL./.HTO354018..CCC..MANTA.J121.BRIGS.JIIMS2.KPHL/1814");

  @Test
  void testRoute0_0() {
    List<SectionSplit> splits = new IfrFormatSectionSplitter().splits(ROUTE0.get(0));

    assertAll(
        () -> assertEquals("KBDL", splits.get(0).value()),
        () -> assertEquals("CSTL6", splits.get(1).value()),
        () -> assertEquals("SHERL", splits.get(2).value()),
        () -> assertEquals("J121", splits.get(3).value()),
        () -> assertEquals("KPHL", splits.get(6).value()),
        () -> assertEquals("0054", splits.get(6).etaEet())
    );
  }

  @Test
  void testRoute0_1() {
    List<SectionSplit> splits = new IfrFormatSectionSplitter().splits(ROUTE0.get(2));

    assertAll(
        () -> assertEquals("KBDL", splits.get(0).value()),
        () -> assertEquals("HTO354018", splits.get(1).value()),
        () -> assertEquals("/", splits.get(1).wildcards()),
        () -> assertEquals("KPHL", splits.get(7).value()),
        () -> assertEquals("1814", splits.get(7).etaEet())
    );
  }

  private final List<String> ROUTE1 = singletonList("KDCA./.WALCE..KGSO*");

  @Test
  void testRoute1_0() {
    List<SectionSplit> splits = new IfrFormatSectionSplitter().splits(ROUTE1.get(0));

    assertAll(
        () -> assertEquals("KDCA", splits.get(0).value()),
        () -> assertEquals("WALCE", splits.get(1).value()),
        () -> assertEquals("/", splits.get(1).wildcards()),
        () -> assertEquals("KGSO", splits.get(2).value()),
        () -> assertEquals("*", splits.get(2).wildcards())
    );
  }

  private final List<String> ROUTE2 = singletonList("KFRG..+RBV.J230.SAAME.J6.+BWG*..KMEM");

  @Test
  void testRoute2_0() {
    List<SectionSplit> splits = new IfrFormatSectionSplitter().splits(ROUTE2.get(0));

    assertAll(
        () -> assertEquals("KFRG", splits.get(0).value()),
        () -> assertEquals("RBV", splits.get(1).value()),
        () -> assertEquals("+", splits.get(1).wildcards()),
        () -> assertEquals("BWG", splits.get(5).value()),
        () -> assertTrue(Wildcard.PLUS.test(splits.get(5).wildcards())),
        () -> assertTrue(Wildcard.SUPPRESSED.test(splits.get(5).wildcards()))
    );
  }

  private final List<String> ROUTE3 = singletonList("KMSO./.*4222N/10726W..WOOKY.TSHNR2.KDEN");

  @Test
  void testRoute3_0() {
    List<SectionSplit> splits = new IfrFormatSectionSplitter().splits(ROUTE3.get(0));

    assertAll(
        () -> assertEquals("4222N/10726W", splits.get(1).value()),
        () -> assertEquals("*/", splits.get(1).wildcards())
    );
  }
}
