package org.mitre.tdp.boogie;

import java.util.List;

public interface Airport extends Fix {

  /**
   * Collection of all runways available at the airport.
   */
  List<? extends Runway> runways();
}
