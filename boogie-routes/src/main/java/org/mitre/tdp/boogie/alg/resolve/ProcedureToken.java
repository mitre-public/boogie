package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.alg.resolve.LinkingUtils.orElse;
import static org.mitre.tdp.boogie.model.ProcedureFactory.newProcedureGraph;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.model.ProcedureGraph;

/**
 * Abstract implementation of a {@link ResolvedToken} decorating a {@link Procedure} record. Many of the methods for procedures
 * are common across {@link ProcedureType}s and so we provide them defaulted here.
 * <br>
 * The three primary subclasses of this class:
 * <br>
 * 1. {@link ApproachToken}
 * 2. {@link SidToken}
 * 3. {@link StarToken}
 * <br>
 * Are primarily concerned with overriding specific implementations of the visitor pattern - and within that most of the main
 * overrides are related to STARs/Approaches (the SID one lives in the {@link AirportToken}). The reasons for and the methods
 * used for each are documented on the concrete subclasses of this.
 * <br>
 * Note due to the nature of the visitor pattern {@link ResolvedToken#linksTo(ResolvedElementVisitor)} cannot be overridden in
 * this class even though the implementations look "identical".
 */
abstract class ProcedureToken implements ResolvedToken {

  protected final Procedure procedure;
  private final List<LinkedLegs> linkedLegs;

  protected ProcedureToken(Procedure procedure) {
    this.procedure = requireNonNull(procedure);
    this.linkedLegs = toLinkedLegsInternal();
  }

  /**
   * Allow visibility so non-extension classes (within the package) can get access to the element's procedure.
   * <br>
   * e.g. {@link StarToAirportLinker}.
   */
  public Procedure procedure() {
    return procedure;
  }

  private List<LinkedLegs> toLinkedLegsInternal() {
    // have to be a bit careful - we may mask all of the transitions somewhere...
    if (procedure.transitions().isEmpty()) {
      return Collections.emptyList();
    }

    ProcedureGraph graph = newProcedureGraph(procedure);

    return graph.edgeSet().stream()
        .map(edge -> {
          Leg source = graph.getEdgeSource(edge);
          Leg target = graph.getEdgeTarget(edge);
          return new LinkedLegs(source, target, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT);
        })
        .collect(Collectors.toList());
  }

  @Override
  public List<LinkedLegs> toLinkedLegs() {
    return linkedLegs;
  }

  @Override
  public List<LinkedLegs> visit(AirwayToken airwayElement) {
    return orElse(PointsWithinRange.INSTANCE, ClosestPointBetween.INSTANCE).apply(airwayElement, this);
  }

  @Override
  public List<LinkedLegs> visit(FixToken fixElement) {
    return ClosestPointBetween.INSTANCE.apply(fixElement, this);
  }

  @Override
  public List<LinkedLegs> visit(TailoredToken tailoredElement) {
    return ClosestPointBetween.INSTANCE.apply(tailoredElement, this);
  }

  @Override
  public List<LinkedLegs> visit(LatLonToken latLonElement) {
    return ClosestPointBetween.INSTANCE.apply(latLonElement, this);
  }
}
