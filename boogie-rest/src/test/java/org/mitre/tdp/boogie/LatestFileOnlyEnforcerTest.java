package org.mitre.tdp.boogie;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class LatestFileOnlyEnforcerTest {

  @Test
  void testDeletesNotLatestSeenFiles(@TempDir Path directory) throws IOException {
    Path testFile1 = Files.createTempFile(directory, "testFile1", null);
    Path testFile2 = Files.createTempFile(directory, "testFile2", null);
    Path subDir1 = Files.createTempDirectory(directory, "testDir1");

    List<Path> accepted = newArrayList();
    LatestFileOnlyEnforcer enforcer = LatestFileOnlyEnforcer.managing(directory, accepted::add);

    enforcer.accept(testFile2);

    assertAll(
        () -> assertEquals(List.of(testFile2), accepted, "Accepted files list should have been delegated to and contain test2"),
        () -> assertTrue(testFile2.toFile().exists(), "Accepted file test2 should still exist in the directory."),
        () -> assertFalse(testFile1.toFile().exists(), "Non-latest file test1 should have been cleaned up."),
        () -> assertFalse(subDir1.toFile().exists(), "Subdirectory should also have been cleaned up.")
    );
  }
}
