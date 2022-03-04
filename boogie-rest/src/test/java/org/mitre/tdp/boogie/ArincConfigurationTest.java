package org.mitre.tdp.boogie;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.springframework.mock.env.MockEnvironment;

class ArincConfigurationTest {

  @Test
  void testSafeVersionParse() {
    MockEnvironment mockEnvironment = new MockEnvironment();
    ArincConfiguration configuration = new ArincConfiguration(mockEnvironment);

    assertAll(
        () -> assertEquals(ArincVersion.V19, configuration.tryParseVersion("V19").orElse(null), "Valid V19"),
        () -> assertEquals(Optional.empty(), configuration.tryParseVersion("FAKE"), "Invalid version should return empty (and not throw an exception).")
    );
  }
}
