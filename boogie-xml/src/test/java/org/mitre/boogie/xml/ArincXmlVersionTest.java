package org.mitre.boogie.xml;

import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mitre.boogie.xml.ArincXmlVersion.parse;

import java.util.Set;

import org.junit.jupiter.api.Test;

class ArincXmlVersionTest {

  @Test
  void testParse() {

    Set<ArincXmlVersion> expected = Set.of(ArincXmlVersion.values());

    Set<ArincXmlVersion> actual = expected.stream().flatMap(v -> parse(v.name()).stream()).collect(toSet());

    assertEquals(expected, actual, "Parse should support all well-formatted version identifiers");
  }
}
