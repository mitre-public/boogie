package org.mitre.tdp.boogie.alg.resolve;

import static com.google.common.base.Preconditions.checkArgument;
import static org.mitre.tdp.boogie.alg.resolve.LinkingUtils.orElse;

import java.util.List;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;

public final class ApproachElement extends ProcedureElement {

  private static final SectionGluer sectionGluer = new SectionGluer();

  ApproachElement(Procedure procedure) {
    super(procedure);
    checkArgument(ProcedureType.APPROACH.equals(procedure.procedureType()), "Provided procedure must be of type Approach.");
  }

  @Override
  public List<LinkedLegs> linksTo(ResolvedElementVisitor resolvedElementVisitor) {
    return resolvedElementVisitor.visit(this);
  }

  @Override
  public List<LinkedLegs> visit(AirportElement airportElement) {
    List<LinkedLegs> initialLinking = SectionToApproachLinker.INSTANCE.apply(airportElement, this);
    return sectionGluer.apply(initialLinking, airportElement);
  }

  @Override
  public List<LinkedLegs> visit(AirwayElement airwayElement) {
    List<LinkedLegs> initialLinking = SectionToApproachLinker.INSTANCE.apply(airwayElement, this);
    return sectionGluer.apply(initialLinking, airwayElement);
  }

  @Override
  public List<LinkedLegs> visit(FixElement fixElement) {
    List<LinkedLegs> initialLinking = SectionToApproachLinker.INSTANCE.apply(fixElement, this);
    return sectionGluer.apply(initialLinking, fixElement);
  }

  @Override
  public List<LinkedLegs> visit(SidElement sidElement) {
    List<LinkedLegs> initialLinking = SidToApproachLinker.INSTANCE.apply(sidElement, this);
    return sectionGluer.apply(initialLinking, sidElement);
  }

  @Override
  public List<LinkedLegs> visit(StarElement starElement) {
    List<LinkedLegs> initialLinking = StarToApproachLinker.INSTANCE.apply(starElement, this);
    return sectionGluer.apply(initialLinking, starElement);
  }

  @Override
  public List<LinkedLegs> visit(ApproachElement approachElement) {
    return orElse(PointsWithinRange.INSTANCE, ClosestPointBetween.INSTANCE).apply(approachElement, this);
  }
}
