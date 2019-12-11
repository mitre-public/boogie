package org.mitre.tdp.boogie.alg.resolve.element;

import java.util.ArrayList;
import java.util.List;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.alg.graph.ProcedureGraph;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.SectionSplitLeg;
import org.mitre.tdp.boogie.models.LinkedLegs;
import org.mitre.tdp.boogie.models.Procedure;

public class ProcedureElement extends ResolvedElement<Procedure> {

  public ProcedureElement(Procedure ref) {
    super(ElementType.PROCEDURE, ref);
  }

  @Override
  public List<LinkedLegs> buildLegs() {
    List<LinkedLegs> legs = new ArrayList<>();

    ProcedureGraph graph = ProcedureGraph.from(reference.transitions());

    graph.edgeSet().forEach(edge -> {
      Leg source = graph.getEdgeSource(edge);
      Leg target = graph.getEdgeTarget(edge);
      SectionSplitLeg ssl1 = new SectionSplitLeg(source);
      SectionSplitLeg ssl2 = new SectionSplitLeg(target);
      legs.add(new LinkedLegs(ssl1, ssl2));
    });
    return legs;
  }
}
