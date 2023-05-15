package org.mitre.tdp.boogie.alg.resolve;

import static com.google.common.base.Preconditions.checkArgument;
import static org.mitre.tdp.boogie.alg.resolve.LinkingUtils.orElse;

import java.util.List;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;

public final class StarElement extends ProcedureElement {

  public StarElement(Procedure procedure) {
    super(procedure);
    checkArgument(ProcedureType.STAR.equals(procedure.procedureType()), "Provided procedure must be of type STAR.");
  }

  @Override
  public String identifier() {
    return procedure.procedureIdentifier();
  }

  @Override
  public List<LinkedLegs> linksTo(ResolvedElementVisitor resolvedElementVisitor) {
    return resolvedElementVisitor.visit(this);
  }

  @Override
  public List<LinkedLegs> visit(AirportElement airportElement) {
    return ClosestPointBetween.INSTANCE.apply(airportElement, this);
  }

  @Override
  public List<LinkedLegs> visit(SidElement sidElement) {
    return orElse(PointsWithinRange.INSTANCE, ClosestPointBetween.INSTANCE).apply(sidElement, this);
  }

  @Override
  public List<LinkedLegs> visit(StarElement starElement) {
    return orElse(PointsWithinRange.INSTANCE, ClosestPointBetween.INSTANCE).apply(starElement, this);
  }

  @Override
  public List<LinkedLegs> visit(ApproachElement approachElement) {
    return orElse(PointsWithinRange.INSTANCE, ClosestPointBetween.INSTANCE).apply(approachElement, this);
  }
}
