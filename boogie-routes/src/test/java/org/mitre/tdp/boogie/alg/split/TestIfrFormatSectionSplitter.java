package org.mitre.tdp.boogie.alg.split;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;


public class TestIfrFormatSectionSplitter {

  private List<String> ROUTE0 = Arrays.asList(
      "KBDL.CSTL6.SHERL.J121.BRIGS.JIIMS2.KPHL/0054",
      "KBDL.CSTL6.SHERL.J121.BRIGS.JIIMS2.KPHL/1814",
      "KBDL./.HTO354018..CCC..MANTA.J121.BRIGS.JIIMS2.KPHL/1814");
  private List<String> ROUTE1 = Collections.singletonList("KDCA./.WALCE..KGSO*");
  private List<String> ROUTE2 = Collections.singletonList("KFRG..+RBV.J230.SAAME.J6.+BWG*..KMEM");
  private List<String> ROUTE3 = Collections.singletonList("KMSO./.*4222N/10726W..WOOKY.TSHNR2.KDEN");

  @Test
  public void testRoute0_0() {
    List<SectionSplit> splits = new IfrFormatSectionSplitter().splits(ROUTE0.get(0));

    assertEquals(splits.get(0).value(), "KBDL");
    assertEquals(splits.get(1).value(), "CSTL6");
    assertEquals(splits.get(2).value(), "SHERL");
    assertEquals(splits.get(3).value(), "J121");
    assertEquals(splits.get(6).value(), "KPHL");
    assertEquals(splits.get(6).etaEet(), "0054");
  }

  @Test
  public void testRoute0_1() {
    List<SectionSplit> splits = new IfrFormatSectionSplitter().splits(ROUTE0.get(2));

    assertEquals(splits.get(0).value(), "KBDL");
    assertEquals(splits.get(1).value(), "");
    assertEquals(splits.get(1).wildcards(), "/");
    assertEquals(splits.get(2).value(), "HTO354018");
    assertEquals(splits.get(10).value(), "KPHL");
    assertEquals(splits.get(10).etaEet(), "1814");

    assertTrue(Wildcard.TAILORED.test("+*/"));
  }

  @Test
  public void testRoute1_0() {
    List<SectionSplit> splits = new IfrFormatSectionSplitter().splits(ROUTE1.get(0));

    assertEquals(splits.get(0).value(), "KDCA");
    assertEquals(splits.get(1).value(), "");
    assertTrue(Wildcard.TAILORED.test(splits.get(1).wildcards()));
    assertEquals(splits.get(2).value(), "WALCE");
    assertEquals(splits.get(3).value(), "");
    assertEquals(splits.get(4).value(), "KGSO");
    assertTrue(Wildcard.SUPPRESSED.test(splits.get(4).wildcards()));
  }

  @Test
  public void testRoute2_0() {
    List<SectionSplit> splits = new IfrFormatSectionSplitter().splits(ROUTE2.get(0));

    assertEquals(splits.get(0).value(), "KFRG");
    assertTrue(Wildcard.PLUS.test(splits.get(2).wildcards()));
    assertEquals(splits.get(2).value(), "RBV");

    assertEquals(splits.get(6).value(), "BWG");
    assertTrue(Wildcard.PLUS.test(splits.get(6).wildcards()));
    assertTrue(Wildcard.SUPPRESSED.test(splits.get(6).wildcards()));
  }

  @Test
  public void testRoute3_0() {
    List<SectionSplit> splits = new IfrFormatSectionSplitter().splits(ROUTE3.get(0));

    SectionSplit ll = splits.get(2);

    assertEquals(ll.value(), "4222N/10726W");
    assertEquals(ll.wildcards(), "*");
    assertEquals(splits.get(1).value(), "");
    assertEquals(splits.get(1).wildcards(), "/");
  }
}
