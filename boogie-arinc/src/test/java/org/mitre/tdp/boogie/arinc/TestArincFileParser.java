package org.mitre.tdp.boogie.arinc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public final class TestArincFileParser {

  private final ArincRecordParser parser;

  public TestArincFileParser(ArincRecordParser parser) {
    this.parser = parser;
  }

  public Collection<ArincRecord> parseAll(File file) {
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      return reader.lines().map(parser::parse).flatMap(Optional::stream).toList();
    } catch (IOException e) {
      throw new RuntimeException("Error during parse of ARINC 424 file: " + file, e);
    }
  }
}
