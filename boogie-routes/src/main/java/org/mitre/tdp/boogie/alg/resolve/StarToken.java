package org.mitre.tdp.boogie.alg.resolve;

import static com.google.common.base.Preconditions.checkArgument;
import static org.mitre.tdp.boogie.alg.resolve.LinkingUtils.orElse;

import java.util.List;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;

public final class StarToken extends ProcedureToken {

  public StarToken(Procedure procedure) {
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
  public void accept(ResolvedTokenVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public List<LinkedLegs> visit(AirportToken airportElement) {
    return ClosestPointBetween.INSTANCE.apply(airportElement, this);
  }

  @Override
  public List<LinkedLegs> visit(SidToken sidElement) {
    return orElse(PointsWithinRange.INSTANCE, ClosestPointBetween.INSTANCE).apply(sidElement, this);
  }

  @Override
  public List<LinkedLegs> visit(StarToken starElement) {
    return orElse(PointsWithinRange.INSTANCE, ClosestPointBetween.INSTANCE).apply(starElement, this);
  }

  @Override
  public List<LinkedLegs> visit(ApproachToken approachElement) {
    return orElse(PointsWithinRange.INSTANCE, ClosestPointBetween.INSTANCE).apply(approachElement, this);
  }
}
