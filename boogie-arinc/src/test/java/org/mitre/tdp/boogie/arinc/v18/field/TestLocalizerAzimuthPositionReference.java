package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestLocalizerAzimuthPositionReference {

  private static final LocalizerAzimuthPositionReference parser = new LocalizerAzimuthPositionReference();

  /**
   * Whitespace has meaning for this field and therefore should be passed through the spec.
   */
  @Test
  void testReturnsWhitespace() {
    assertEquals(Optional.of(" "), parser.apply(" "));
  }
}
