package org.mitre.tdp.boogie.conformance.alg.assign.generate;

import java.util.Optional;

import org.mitre.tdp.boogie.Leg;

import com.google.common.annotations.Beta;

@Beta
public final class AreaProximityVisitor implements Leg.Visitor{
  private AreaProximity areaProximity;

  private AreaProximityVisitor() {
  }

  public static Optional<AreaProximity> getAreaProximity(Leg leg) {
    AreaProximityVisitor visitor = new AreaProximityVisitor();
    leg.accept(visitor);
    return Optional.ofNullable(visitor.areaProximity);
  }

  @Override
  public void visit(Leg.Standard standard) {
    areaProximity = null;
  }

  @Override
  public void visit(Leg.Record<?> record) {
    areaProximity = Optional.ofNullable(record)
        .map(Leg.Record::datum)
        .filter(i -> AreaProximity.class.isAssignableFrom(i.getClass()))
        .map(AreaProximity.class::cast)
        .orElse(null);
  }
}
