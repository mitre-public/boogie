package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.Objects.requireNonNull;

import java.util.Collection;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.alg.resolve.LinkedLegs;

final class SidRunway implements GraphableToken {

  private final Procedure sid;

  SidRunway(Procedure sid) {
    this.sid = requireNonNull(sid);
  }

  @Override
  public Collection<LinkedLegs> linkedLegs() {
    return null;
  }

  @Override
  public Linker accept(LinkingVisitor visitor) {
    return null;
  }
}
