package org.mitre.tdp.boogie.arinc.database;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.v18.ArincDatabase;
import org.mitre.tdp.boogie.arinc.v18.ArincDatabaseFactory;

class TestArincDatabaseFactory {

  @Test
  void testDatabaseFromFile() {
    ArincDatabase database = ArincDatabaseFactory.newDatabaseFromFile(new File(""));
  }
}
