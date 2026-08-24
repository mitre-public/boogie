package org.mitre.tdp.boogie.arinc.assemble;

import java.util.Optional;
import java.util.regex.Matcher;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestReciprocalRunwayIdentifier {

  @Test
  void testCharacterFlipsLToR() {
    assertEquals("R", ReciprocalRunwayIdentifier.INSTANCE.inverseCharacter("03L"));
  }

  @Test
  void testCharacterFlipsRToL() {
    assertEquals("L", ReciprocalRunwayIdentifier.INSTANCE.inverseCharacter("03R"));
  }

  @Test
  void testNonLRCharacterLeftUnchanged() {
    assertEquals("C", ReciprocalRunwayIdentifier.INSTANCE.inverseCharacter("23C"));
  }

  @Test
  void testInverseBearingWraps360() {
    assertEquals("05", ReciprocalRunwayIdentifier.INSTANCE.inverseBearing("23"));
  }

  @Test
  void testInverseBearingWorksAsExpectedWithoutWrapping() {
    assertEquals("23", ReciprocalRunwayIdentifier.INSTANCE.inverseBearing("05"));
  }

  @Test
  void testInvertIdReturnsConcatenatedInverseBearingAndCharacter() {
    assertEquals("23L", ReciprocalRunwayIdentifier.INSTANCE.invertIdentifier("05R"));
  }

  @Test
  void testInvertIfThrowsExceptionOnNon3CharacterId() {
    assertThrows(IllegalArgumentException.class, () -> ReciprocalRunwayIdentifier.INSTANCE.invertIdentifier("R03L"));
  }

  @Test
  void testRunwayIdRegexMatchesRW03L() {
    assertTrue(ReciprocalRunwayIdentifier.RUNWAY_ID.asPredicate().test("RW03L"));
  }

  @Test
  void testRunwayIdRegexMatchesRW06R() {
    assertTrue(ReciprocalRunwayIdentifier.RUNWAY_ID.asPredicate().test("RW06R"));
  }

  @Test
  void testRunwayIdRegexMatchesRW33C() {
    assertTrue(ReciprocalRunwayIdentifier.RUNWAY_ID.asPredicate().test("RW33C"));
  }

  @Test
  void testAssaultStripSuffix() {
    Matcher matcher = ReciprocalRunwayIdentifier.RUNWAY_ID.matcher("RW164");
    assertAll(
        () -> assertTrue(matcher.find()),
        () -> assertEquals("164", matcher.group())
    );
  }

  @Test
  void testRunwayIdRegexExcludesBearingsOutside0_36() {
    assertFalse(ReciprocalRunwayIdentifier.RUNWAY_ID.asPredicate().test("RW45L"));
  }

  @Test
  void testRunwayIdRegexAllows2CharacterIds() {
    assertTrue(ReciprocalRunwayIdentifier.RUNWAY_ID.asPredicate().test("03"));
  }

  @Test
  void testReciprocalIdSkipsNoPrefixOrPostfix() {
    assertEquals(Optional.of("03L"), ReciprocalRunwayIdentifier.INSTANCE.apply("21R"));
  }

  @Test
  void testReciprocalIdPreservesPrefixIfPresent() {
    assertEquals(Optional.of("R03L"), ReciprocalRunwayIdentifier.INSTANCE.apply("R21R"));
  }

  @Test
  void testReciprocalIdPreservesPostfixIfPresent() {
    assertEquals(Optional.of("03R-AB"), ReciprocalRunwayIdentifier.INSTANCE.apply("21L-AB"));
  }

  @Test
  void testReciprocalIdPreservesSingleCharacterPostfixIfPresent() {
    assertEquals(Optional.of("03R-"), ReciprocalRunwayIdentifier.INSTANCE.apply("21L-"));
  }

  @Test
  void testReciprocalIdPreservesPreAndPostFixWhenPresent() {
    assertEquals(Optional.of("RW03R-AB"), ReciprocalRunwayIdentifier.INSTANCE.apply("RW21L-AB"));
  }

  @Test
  void testReciprocalIdPreservesNumericAssaultStripSuffix() {
    assertAll(
        () -> assertEquals(Optional.of("RW344"), ReciprocalRunwayIdentifier.INSTANCE.apply("RW164")),
        () -> assertEquals(Optional.of("RW164"), ReciprocalRunwayIdentifier.INSTANCE.apply("RW344"))
    );
  }

  @Test
  void testReciprocalIdSkipsWeirdIdentifiers() {
    assertEquals(Optional.empty(), ReciprocalRunwayIdentifier.INSTANCE.apply("SW"));
  }

  @Test
  void testReciprocalIDEdgeCaseOf18To36() {
    assertEquals(Optional.of("RW36"), ReciprocalRunwayIdentifier.INSTANCE.apply("RW18"));
  }

  @Test
  void testReciprocalIDEdgeCaseOf36To18() {
    assertEquals(Optional.of("RW18"), ReciprocalRunwayIdentifier.INSTANCE.apply("RW36"));
  }
}
