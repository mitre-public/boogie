package org.mitre.tdp.boogie.alg.split;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

class FaaIfrFormatTokenizerTest {

  private static final RouteTokenizer TOKENIZER = RouteTokenizer.faaIfrFormat();

  private static final Function<String, List<RouteToken.FaaIfr>> SPLITTER = s -> TOKENIZER.tokenize(s)
      .stream().map(t -> (RouteToken.FaaIfr) t).collect(toList());

  private final List<String> ROUTE0 = Arrays.asList(
      "KBDL.CSTL6.SHERL.J121.BRIGS.JIIMS2.KPHL/0054",
      "KBDL.CSTL6.SHERL.J121.BRIGS.JIIMS2.KPHL/1814",
      "KBDL./.HTO354018..CCC..MANTA.J121.BRIGS.JIIMS2.KPHL/1814");

  @Test
  void testAirportInMiddle() {
    List<RouteToken.FaaIfr> splits = SPLITTER.apply("VOBL.BLI..VOBL..MMW");
    assertEquals(" ", splits.get(2).wildcards().orElse(""));
  }

  @Test
  void testRoute0_0() {
    List<RouteToken.FaaIfr> splits = SPLITTER.apply(ROUTE0.get(0));

    assertAll(
        () -> assertEquals("KBDL", splits.get(0).infrastructureName()),
        () -> assertEquals("CSTL6", splits.get(1).infrastructureName()),
        () -> assertEquals("SHERL", splits.get(2).infrastructureName()),
        () -> assertEquals("J121", splits.get(3).infrastructureName()),
        () -> assertEquals("KPHL", splits.get(6).infrastructureName()),
        () -> assertEquals("0054", splits.get(6).etaEet().orElse(null))
    );
  }

  @Test
  void testRoute0_1() {
    List<RouteToken.FaaIfr> splits = SPLITTER.apply(ROUTE0.get(2));

    assertAll(
        () -> assertEquals("KBDL", splits.get(0).infrastructureName()),
        () -> assertEquals("HTO354018", splits.get(1).infrastructureName()),
        () -> assertEquals("/", splits.get(1).wildcards().orElse(null)),
        () -> assertEquals("KPHL", splits.get(7).infrastructureName()),
        () -> assertEquals("1814", splits.get(7).etaEet().orElse(null))
    );
  }

  private final List<String> ROUTE1 = singletonList("KDCA./.WALCE..KGSO*");

  @Test
  void testRoute1_0() {
    List<RouteToken.FaaIfr> splits = SPLITTER.apply(ROUTE1.get(0));

    assertAll(
        () -> assertEquals("KDCA", splits.get(0).infrastructureName()),
        () -> assertEquals("WALCE", splits.get(1).infrastructureName()),
        () -> assertEquals("/", splits.get(1).wildcards().orElse(null)),
        () -> assertEquals("KGSO", splits.get(2).infrastructureName()),
        () -> assertEquals("* ", splits.get(2).wildcards().orElse(null))
    );
  }

  private final List<String> ROUTE2 = singletonList("KFRG..+RBV.J230.SAAME.J6.+BWG*..KMEM");

  @Test
  void testRoute2_0() {
    List<RouteToken.FaaIfr> splits = SPLITTER.apply(ROUTE2.get(0));

    assertAll(
        () -> assertEquals("KFRG", splits.get(0).infrastructureName()),
        () -> assertEquals("RBV", splits.get(1).infrastructureName()),
        () -> assertEquals("+ ", splits.get(1).wildcards().orElse(null)),
        () -> assertEquals("BWG", splits.get(5).infrastructureName()),
        () -> assertTrue(splits.get(5).wildcards().map(Wildcard.PLUS::test).orElse(false)),
        () -> assertTrue(splits.get(5).wildcards().map(Wildcard.SUPPRESSED::test).orElse(false))
    );
  }

  private final List<String> ROUTE3 = singletonList("KMSO./.*4222N/10726W..WOOKY.TSHNR2.KDEN");

  @Test
  void testRoute3_0() {
    List<RouteToken.FaaIfr> splits = SPLITTER.apply(ROUTE3.get(0));

    assertAll(
        () -> assertEquals("4222N/10726W", splits.get(1).infrastructureName()),
        () -> assertEquals("*/", splits.get(1).wildcards().orElse(null))
    );
  }
}
