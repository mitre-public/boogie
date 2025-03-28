package org.mitre.tdp.boogie.arinc.assemble;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.tdp.boogie.Geometry;
import org.mitre.tdp.boogie.arinc.v18.field.BoundaryVia;

public final class ViaToBoundary implements Function<BoundaryVia, Optional<Geometry>> {
  public static final ViaToBoundary INSTANCE = new ViaToBoundary();
  private ViaToBoundary() {}
  @Override
  public Optional<Geometry> apply(BoundaryVia boundaryVia) {
    return switch (boundaryVia) {
      case CE, C -> Optional.of(Geometry.CIRCLE);
      case GE, G -> Optional.of(Geometry.GREAT_CIRCLE);
      case HE, H -> Optional.of(Geometry.RHUMB_LINE);
      case LE, L -> Optional.of(Geometry.COUNTER_CLOCKWISE_ARC);
      case RE, R -> Optional.of(Geometry.CLOCKWISE_ARC);
      case E -> Optional.empty();
      default -> throw new IllegalArgumentException("Unknown boundary: " + boundaryVia);
    };
  }
}
