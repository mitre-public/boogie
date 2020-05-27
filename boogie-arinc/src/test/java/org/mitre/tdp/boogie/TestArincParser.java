package org.mitre.tdp.boogie;

import org.junit.jupiter.api.Test;

public class TestArincParser {

  class TestParser implements ArincParser {
    @Override
    public ArincSpec arincSpec() {
      return ArincVersion.V18;
    }
  }

  @Test
  public void testParserFunctionality() {
    TestParser parser = new TestParser();

//    ArincRecord record = parser.parse("");

  }
}
