package org.mitre.tdp.boogie.alg.resolve;

import static com.google.common.base.Preconditions.checkArgument;
import static org.mitre.tdp.boogie.alg.resolve.LinkingUtils.orElse;

import java.util.List;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.alg.chooser.graph.SectionGluer;

public final class ApproachToken extends ProcedureToken {

  private static final SectionGluer sectionGluer = new SectionGluer();

  public ApproachToken(Procedure procedure) {
    super(procedure);
    checkArgument(ProcedureType.APPROACH.equals(procedure.procedureType()), "Provided procedure must be of type Approach.");
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
    List<LinkedLegs> initialLinking = SectionToApproachLinker.INSTANCE.apply(airportElement, this);
    return sectionGluer.apply(initialLinking, airportElement);
  }

  @Override
  public List<LinkedLegs> visit(AirwayToken airwayElement) {
    List<LinkedLegs> initialLinking = SectionToApproachLinker.INSTANCE.apply(airwayElement, this);
    return sectionGluer.apply(initialLinking, airwayElement);
  }

  @Override
  public List<LinkedLegs> visit(FixToken fixElement) {
    List<LinkedLegs> initialLinking = SectionToApproachLinker.INSTANCE.apply(fixElement, this);
    return sectionGluer.apply(initialLinking, fixElement);
  }

  @Override
  public List<LinkedLegs> visit(SidToken sidElement) {
    List<LinkedLegs> initialLinking = SidToApproachLinker.INSTANCE.apply(sidElement, this);
    return sectionGluer.apply(initialLinking, sidElement);
  }

  @Override
  public List<LinkedLegs> visit(StarToken starElement) {
    List<LinkedLegs> initialLinking = StarToApproachLinker.INSTANCE.apply(starElement, this);
    return sectionGluer.apply(initialLinking, starElement);
  }

  @Override
  public List<LinkedLegs> visit(ApproachToken approachElement) {
    return orElse(PointsWithinRange.INSTANCE, ClosestPointBetween.INSTANCE).apply(approachElement, this);
  }
}
