package org.mitre.tdp.boogie.arinc.v18;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.tdp.boogie.arinc.v21.field.PadShape;

/**
 * In earlier versions of 424 the helipad is either rectangular or round,
 * but you have to figure that out. The trick is that the last 3 are zeros when it's a circle.
 */
public final class PadShapeExtractor implements Function<String, PadShape> {
  public static final PadShapeExtractor INSTANCE = new PadShapeExtractor();
  private PadShapeExtractor() {
  }

  @Override
  public PadShape apply(String s) {
    return Optional.of(s)
        .filter(i -> i.endsWith("000"))
        .map(i -> PadShape.C)
        .orElse(PadShape.R);
  }
}
