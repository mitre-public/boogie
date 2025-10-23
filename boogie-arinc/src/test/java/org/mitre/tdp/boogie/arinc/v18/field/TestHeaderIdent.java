package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestHeaderIdent {
  static HeaderIdent parser = HeaderIdent.SPEC;

  @Test
  void testParserFiltersEmptyInputs() {
    Assertions.assertEquals(Optional.empty(), parser.apply(""));
  }

  @Test
  void testParserFiltersWhitespaceInputs() {
    Assertions.assertEquals(Optional.empty(), parser.apply(" "));
  }

  @Test
  void testParserPassesAllowedCodes() {
    Arrays.stream(HeaderIdent.values())
        .filter(i -> !HeaderIdent.SPEC.equals(i))
        .forEach(code -> Assertions.assertEquals(Optional.of(code), parser.apply(code.name())));
  }

  @Test
  void testParserFiltersDisallowedCodes() {
    Assertions.assertEquals(Optional.empty(), parser.apply("@"));
  }
 }
