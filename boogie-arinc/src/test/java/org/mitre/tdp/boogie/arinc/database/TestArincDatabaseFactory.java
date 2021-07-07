package org.mitre.tdp.boogie.arinc.database;

import java.io.File;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class TestArincDatabaseFactory {

  @Test
  @Disabled
  void testDatabaseFromFile() {
    ArincDatabase database = ArincDatabaseFactory.newDatabaseFromFile(new File(""));
  }
}
