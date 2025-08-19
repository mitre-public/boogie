package org.mitre.tdp.boogie.alg.split;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class CombinedFormatTokenizerTest {

  RouteTokenizer tokenizer = RouteTokenizer.combinedFormat();

  static String ROUTE0 = "KBDL.CSTL6.SHERL.J121.BRIGS.JIIMS2.KPHL/0054";
  static String ROUTE1 = "KBDL CSTL6 SHERL J121 BRIGS JIIMS2 KPHL/0054";

  @Test
  void testFaa() {
    List<RouteToken> tokens = tokenizer.tokenize(ROUTE0);
    assertAll(
        () -> assertEquals("KBDL", tokens.get(0).infrastructureName()),
        () -> assertEquals("CSTL6", tokens.get(1).infrastructureName()),
        () -> assertEquals("SHERL", tokens.get(2).infrastructureName()),
        () -> assertEquals("J121", tokens.get(3).infrastructureName()),
        () -> assertEquals("KPHL", tokens.get(6).infrastructureName()),
        () -> assertEquals("0054", RouteTokenVisitor.extractEtaEet(tokens.get(6)).orElse(null))
    );
  }

  @Test
  void testICao() {
    List<RouteToken> tokens = tokenizer.tokenize(ROUTE1);
    assertAll(
        () -> assertEquals("KBDL", tokens.get(0).infrastructureName()),
        () -> assertEquals("CSTL6", tokens.get(1).infrastructureName()),
        () -> assertEquals("SHERL", tokens.get(2).infrastructureName()),
        () -> assertEquals("J121", tokens.get(3).infrastructureName()),
        () -> assertEquals("KPHL", tokens.get(6).infrastructureName()),
        () -> assertEquals("0054", RouteTokenVisitor.extractEtaEet(tokens.get(6)).orElse(null))
    );
  }

}
