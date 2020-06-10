package org.mitre.tdp.boogie.alg.resolve.element;

import java.util.ArrayList;
import java.util.List;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.alg.graph.ProcedureGraph;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.GraphableLeg;
import org.mitre.tdp.boogie.models.LinkedLegs;
import org.mitre.tdp.boogie.models.Procedure;

public class ProcedureElement extends ResolvedElement<Procedure> {

  public ProcedureElement(Procedure ref) {
    super(elementTypeFor(ref.type()), ref);
  }

  /**
   * The {@link ProcedureType} -> {@link ElementType} mapping for resolved procedures.
   */
  public static ElementType elementTypeFor(ProcedureType ref) {
    switch (ref) {
      case SID:
        return ElementType.SID;
      case STAR:
        return ElementType.STAR;
      case APPROACH:
        return ElementType.APPROACH;
      default:
        throw new UnsupportedOperationException("Unknown procedure type: " + ref);
    }
  }

  @Override
  public List<LinkedLegs> buildLegs() {
    List<LinkedLegs> legs = new ArrayList<>();

    ProcedureGraph graph = ProcedureGraph.from(reference.transitions());

    graph.edgeSet().forEach(edge -> {
      Leg source = graph.getEdgeSource(edge);
      Leg target = graph.getEdgeTarget(edge);
      GraphableLeg ssl1 = new GraphableLeg(source);
      GraphableLeg ssl2 = new GraphableLeg(target);
      legs.add(new LinkedLegs(ssl1, ssl2));
    });
    return legs;
  }
}
