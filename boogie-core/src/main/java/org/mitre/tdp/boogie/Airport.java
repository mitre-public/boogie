package org.mitre.tdp.boogie;

import java.util.List;

import org.mitre.caasd.commons.HasPosition;

public interface Airport<R extends Runway> extends Infrastructure, HasPosition {

  /**
   * Collection of all runways available at the airport.
   */
  List<R> runways();
}
