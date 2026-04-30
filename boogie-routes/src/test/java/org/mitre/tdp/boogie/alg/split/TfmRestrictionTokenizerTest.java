package org.mitre.tdp.boogie.alg.split;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

class TfmRestrictionTokenizerTest {

  private static final RouteTokenizer TOKENIZER = RouteTokenizer.tfmRestrictionFormat();

  @Test
  void testSingleToken() {
    List<RouteToken> tokens = TOKENIZER.tokenize("DRSDN");

    assertEquals(1, tokens.size());
    assertAll(
        () -> assertEquals("DRSDN", tokens.get(0).infrastructureName()),
        () -> assertEquals(0.0, tokens.get(0).index())
    );
  }

  @Test
  void testTwoTokens() {
    List<RouteToken> tokens = TOKENIZER.tokenize("DRSDN/AFM");

    assertEquals(2, tokens.size());
    assertAll(
        () -> assertEquals("DRSDN", tokens.get(0).infrastructureName()),
        () -> assertEquals(0.0, tokens.get(0).index()),
        () -> assertEquals("AFM", tokens.get(1).infrastructureName()),
        () -> assertEquals(1.0, tokens.get(1).index())
    );
  }

  @Test
  void testMultipleTokens() {
    List<RouteToken> tokens = TOKENIZER.tokenize("KATL/CHPPR1/DRSDN/J121/JMACK/HOBTT");

    assertEquals(6, tokens.size());
    assertAll(
        () -> assertEquals("KATL", tokens.get(0).infrastructureName()),
        () -> assertEquals(0.0, tokens.get(0).index()),
        () -> assertEquals("CHPPR1", tokens.get(1).infrastructureName()),
        () -> assertEquals(1.0, tokens.get(1).index()),
        () -> assertEquals("DRSDN", tokens.get(2).infrastructureName()),
        () -> assertEquals(2.0, tokens.get(2).index()),
        () -> assertEquals("J121", tokens.get(3).infrastructureName()),
        () -> assertEquals(3.0, tokens.get(3).index()),
        () -> assertEquals("JMACK", tokens.get(4).infrastructureName()),
        () -> assertEquals(4.0, tokens.get(4).index()),
        () -> assertEquals("HOBTT", tokens.get(5).infrastructureName()),
        () -> assertEquals(5.0, tokens.get(5).index())
    );
  }

  @Test
  void testEmptyString() {
    List<RouteToken> tokens = TOKENIZER.tokenize("");

    assertEquals(0, tokens.size());
  }

  @Test
  void testTrailingSlash() {
    List<RouteToken> tokens = TOKENIZER.tokenize("DRSDN/");

    assertEquals(1, tokens.size());
    assertAll(
        () -> assertEquals("DRSDN", tokens.get(0).infrastructureName()),
        () -> assertEquals(0.0, tokens.get(0).index())
    );
  }

  @Test
  void testLeadingSlash() {
    List<RouteToken> tokens = TOKENIZER.tokenize("/DRSDN");

    assertEquals(1, tokens.size());
    assertAll(
        () -> assertEquals("DRSDN", tokens.get(0).infrastructureName()),
        () -> assertEquals(1.0, tokens.get(0).index(), "Its ok as long as its the lowest number in the list.")
    );
  }
}
