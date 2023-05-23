package org.mitre.tdp.boogie.alg.chooser.graph;

import java.util.Collection;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.alg.resolve.LinkedLegs;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;

final class StarToAirportLinker implements Linker {

  StarToAirportLinker(Procedure star, Airport airport) {

  }

  @Override
  public Collection<LinkedLegs> links() {
    return null;
  }
}
