package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.Objects.requireNonNull;

import java.util.Collection;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.alg.resolve.LinkedLegs;

final class SidEnrouteCommon implements GraphableToken {

  private final Procedure sid;

  private final Collection<LinkedLegs> linkedLegs;

  SidEnrouteCommon(Procedure sid, Collection<LinkedLegs> linkedLegs) {
    this.sid = requireNonNull(sid);
    this.linkedLegs = linkedLegs;
  }
}
