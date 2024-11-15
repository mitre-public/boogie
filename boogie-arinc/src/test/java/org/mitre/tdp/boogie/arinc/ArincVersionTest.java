package org.mitre.tdp.boogie.arinc;

import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mitre.tdp.boogie.arinc.ArincVersion.parse;

import java.util.Set;

import org.junit.jupiter.api.Test;

class ArincVersionTest {

  @Test
  void testParse() {

    Set<ArincVersion> expected = Set.of(ArincVersion.values());

    Set<ArincVersion> actual = expected.stream().flatMap(v -> parse(v.name()).stream()).collect(toSet());

    assertEquals(expected, actual, "Parse should support all well-formatted version identifiers");
  }
}
