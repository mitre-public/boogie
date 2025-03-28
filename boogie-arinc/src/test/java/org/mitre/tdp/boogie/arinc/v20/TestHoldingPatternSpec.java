package org.mitre.tdp.boogie.arinc.v20;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.model.ArincHoldingPattern;
import org.mitre.tdp.boogie.arinc.v18.HoldingPatternValidator;

public class TestHoldingPatternSpec {
  private static final ArincRecordParser PARSER = ArincRecordParser.standard(new HoldingPatternSpec());
  private static final HoldingPatternConverter CONVERTER = new HoldingPatternConverter();
  static String HOLD3 = "SEEUEPEEEIEE               50VEGEREEPC00620R043  02200     230                  I                 VEGER                    233142210";
  static String HOLD_WRONG = "SEEUEPEEEIEE               50VEGEREEPC00620R043  02200     230                  0                 VEGER                    233142210";

  @Test
  void matchesRecord19() {
    assertTrue(new org.mitre.tdp.boogie.arinc.v20.HoldingPatternSpec().matchesRecord(HOLD3));
  }

  @Test
  void validatorPasses() {
    assertTrue(new HoldingPatternValidator().test(PARSER.parse(HOLD3).orElseThrow()), "no change to the validator we get a new optional field");
  }

  @Test
  void parse20() {
    ArincRecord pattern = PARSER.parse(HOLD3).orElseThrow();
    assertEquals("I", pattern.optionalField("legInboundOutboundIndicator").orElseThrow(), "only one change");
  }

  @Test
  void robust() {
    ArincRecord patternWrong = PARSER.parse(HOLD_WRONG).orElseThrow();
    assertFalse(patternWrong.containsParsedField("legInboundOutboundIndicator"));
  }

  @Test
  void convert() {
    ArincHoldingPattern pattern = PARSER.parse(HOLD3).flatMap(CONVERTER).orElseThrow();
    assertEquals("I", pattern.inboundOutboundIndicator().orElseThrow(), "only one change");
  }
}
