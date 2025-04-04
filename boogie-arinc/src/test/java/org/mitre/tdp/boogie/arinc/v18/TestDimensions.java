package org.mitre.tdp.boogie.arinc.v18;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.utils.Dimensions;
import org.mitre.tdp.boogie.arinc.v21.field.PadShape;

public class TestDimensions {
  static String circle = "020000";
  static String rectangle = "123456";
  static PadShapeExtractor extractor = PadShapeExtractor.INSTANCE;
  static DimensionsParser parser = DimensionsParser.INSTANCE;
  @Test
  public void test() {
    PadShape s1 = extractor.apply(circle);
    Dimensions d1 = parser.apply(circle, s1).orElseThrow();
    PadShape s2 = extractor.apply(rectangle);
    Dimensions d2 = parser.apply(rectangle, s2).orElseThrow();
    assertAll(
        () -> assertEquals(PadShape.C, s1),
        () -> assertEquals(20, d1.diameter()),
        () -> assertEquals(PadShape.R, s2),
        () -> assertEquals(123, d2.y()),
        () -> assertEquals(456, d2.x()),
        () -> assertTrue(parser.apply("", PadShape.U).isEmpty())
    );
  }
}
