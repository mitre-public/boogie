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
 * Abstract implementation of a {@link ResolvedElement} decorating a {@link Procedure} record. Many of the methods for procedures
 * are common across {@link ProcedureType}s and so we provide them defaulted here.
 * <br>
 * The three primary subclasses of this class:
 * <br>
 * 1. {@link ApproachElement}
 * 2. {@link SidElement}
 * 3. {@link StarElement}
 * <br>
 * Are primarily concerned with overriding specific implementations of the visitor pattern - and within that most of the main
 * overrides are related to STARs/Approaches (the SID one lives in the {@link AirportElement}). The reasons for and the methods
 * used for each are documented on the concrete subclasses of this.
 * <br>
 * Note due to the nature of the visitor pattern {@link ResolvedElement#linksTo(ResolvedElementVisitor)} cannot be overridden in
 * this class even though the implementations look "identical".
 */
abstract class ProcedureElement implements ResolvedElement {

  protected final Procedure procedure;
  private final List<LinkedLegs> linkedLegs;

  protected ProcedureElement(Procedure procedure) {
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
  public List<LinkedLegs> visit(AirwayElement airwayElement) {
    return orElse(PointsWithinRange.INSTANCE, ClosestPointBetween.INSTANCE).apply(airwayElement, this);
  }

  @Override
  public List<LinkedLegs> visit(FixElement fixElement) {
    return ClosestPointBetween.INSTANCE.apply(fixElement, this);
  }

  @Override
  public List<LinkedLegs> visit(TailoredElement tailoredElement) {
    return ClosestPointBetween.INSTANCE.apply(tailoredElement, this);
  }

  @Override
  public List<LinkedLegs> visit(LatLonElement latLonElement) {
    return ClosestPointBetween.INSTANCE.apply(latLonElement, this);
  }
}
