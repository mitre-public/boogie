package org.mitre.tdp.boogie.alg.resolve;

import java.util.List;

public interface ResolvedToken extends ResolvedElementVisitor {

  /**
   * The identifier of the {@link ResolvedToken} as it was looked-up from configured infrastructure data.
   *
   * <p>For some element types this may not point to an explicit
   */
  String identifier();

  List<LinkedLegs> toLinkedLegs();

  List<LinkedLegs> linksTo(ResolvedElementVisitor resolvedElementVisitor);

  void accept(ResolvedTokenVisitor visitor);
}
