package org.mitre.tdp.boogie;

import java.util.List;

public interface Airport<R extends Runway> extends Fix {

  /**
   * Collection of all runways available at the airport.
   */
  List<R> runways();
}
