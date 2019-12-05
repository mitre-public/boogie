package org.mitre.tdp.boogie;

import java.util.List;

/**
 * The top level TDP interface for Airways.
 */
public interface Airway<L extends Leg> extends Infrastructure {

  /**
   * The collection of {@link Leg}s contained within the airway.
   *
   * For all airways the type
   */
  List<L> legs();
}
