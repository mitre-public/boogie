package org.mitre.tdp.boogie;

import java.util.Optional;

import org.mitre.caasd.commons.HasPosition;

public interface Runway extends Infrastructure {

  /**
   * The physical position of the base end of the runway.
   */
  HasPosition baseEnd();

  /**
   * The physical position of the reciprocal end of the runway.
   */
  Optional<HasPosition> reciprocalEnd();
}
