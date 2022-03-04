package org.mitre.tdp.boogie;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

class InputFilenameFilterTest {

  @Test
  void testInputFilenameFilter_Unspecified() {
    List<Path> paths = newArrayList();

    Consumer<Path> filteredConsumer = InputFilenameFilter.allowMatchingPattern(
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

    Consumer<Path> filteredConsumer = InputFilenameFilter.allowMatchingPattern(
        paths::add,
        ".*dat"
    );

    filteredConsumer.accept(Paths.get("/fake/arinc.dat"));
    filteredConsumer.accept(Paths.get("/fake/arinc.zip"));

    assertEquals(List.of(Paths.get("/fake/arinc.dat")), paths, "Expected only path matching *.dat");
  }
}
