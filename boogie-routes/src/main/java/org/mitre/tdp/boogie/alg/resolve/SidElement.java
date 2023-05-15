package org.mitre.tdp.boogie.alg.resolve;

import static com.google.common.base.Preconditions.checkArgument;
import static org.mitre.tdp.boogie.alg.resolve.LinkingUtils.orElse;

import java.util.List;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;

public final class SidElement extends ProcedureElement {

  private static final double PUNISHMENT = .001;

  public SidElement(Procedure procedure) {
    super(procedure);
    checkArgument(ProcedureType.SID.equals(procedure.procedureType()), "Provided procedure must be of type SID.");
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
    return AirportToSidLinker.INSTANCE.apply(airportElement, this);
  }

  @Override
  public List<LinkedLegs> visit(AirwayElement airwayElement) {
    return orElse(PointsWithinRange.INSTANCE, ClosestPointBetween.INSTANCE)
        .andThen(new UnlikelyCombinationPenalizer(PUNISHMENT))
        .apply(airwayElement, this);
  }

  @Override
  public List<LinkedLegs> visit(SidElement sidElement) {
    return orElse(PointsWithinRange.INSTANCE, ClosestPointBetween.INSTANCE).apply(sidElement, this);
  }

  @Override
  public List<LinkedLegs> visit(StarElement starElement) {
    return orElse(PointsWithinRange.INSTANCE, ClosestPointBetween.INSTANCE)
        .andThen(new UnlikelyCombinationPenalizer(PUNISHMENT))
        .apply(starElement, this);
  }

  @Override
  public List<LinkedLegs> visit(ApproachElement approachElement) {
    return orElse(PointsWithinRange.INSTANCE, ClosestPointBetween.INSTANCE)
        .andThen(new UnlikelyCombinationPenalizer(PUNISHMENT))
        .apply(approachElement, this);
  }
}
