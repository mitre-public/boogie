package org.mitre.tdp.boogie.alg.resolve;

import java.util.List;

public interface ResolvedElement extends ResolvedElementVisitor {

  List<LinkedLegs> toLinkedLegs();

  List<LinkedLegs> linksTo(ResolvedElementVisitor resolvedElementVisitor);
}
