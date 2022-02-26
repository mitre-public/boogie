package org.mitre.tdp.boogie;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

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

  @Test
  void testInputFileanmeFilter_Unspecified() {
    List<Path> paths = newArrayList();

    Consumer<Path> filteredConsumer = ArincConfiguration.InputFilenameFilter.allowMatchingPattern(
        paths::add,
        null
    );

    filteredConsumer.accept(Paths.get("/fake/arinc.dat"));
    filteredConsumer.accept(Paths.get("/fake/arinc.zip"));

    assertEquals(List.of(Paths.get("/fake/arinc.dat"), Paths.get("/fake/arinc.zip")), paths, "All supplied paths should be accepted.");
  }

  @Test
  void testInputFilenameFilter_Specified() {
    List<Path> paths = newArrayList();

    Consumer<Path> filteredConsumer = ArincConfiguration.InputFilenameFilter.allowMatchingPattern(
        paths::add,
        ".*dat"
    );

    filteredConsumer.accept(Paths.get("/fake/arinc.dat"));
    filteredConsumer.accept(Paths.get("/fake/arinc.zip"));

    assertEquals(List.of(Paths.get("/fake/arinc.dat")), paths, "Expected only path matching *.dat");
  }
}
