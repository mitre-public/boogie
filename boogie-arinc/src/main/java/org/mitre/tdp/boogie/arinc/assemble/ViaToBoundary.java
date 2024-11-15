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
    switch (boundaryVia) {
      case CE:
      case C : return Optional.of(Geometry.CIRCLE);
      case GE:
      case G : return Optional.of(Geometry.GREAT_CIRCLE);
      case HE:
      case H : return Optional.of(Geometry.RHUMB_LINE);
      case LE:
      case L : return Optional.of(Geometry.COUNTER_CLOCKWISE_ARC);
      case RE:
      case R : return Optional.of(Geometry.CLOCKWISE_ARC);
      case E: return Optional.empty();
      default: throw new IllegalArgumentException("Unknown boundary: " + boundaryVia);
    }
  }
}
