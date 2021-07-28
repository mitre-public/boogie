package org.mitre.tdp.boogie;

import java.util.List;

/**
 * The top-level interface for airway records within Boogie.
 * <br>
 * This class expects that all airways will contain an identifier for as well as a region code representing the region they
 * originate in.
 */
public interface Airway {

  /**
   * The string identifier of the enroute airway.
   * <br>
   * Generally speaking this is up to 5 characters in length but may be fewer.
   * <br>
   * e.g. Y141, J153, J8, V12, etc.
   */
  String airwayIdentifier();

  /**
   * Generally speaking airways are defined regionally
   */
  String airwayRegion();

  /**
   * The ordered sequence of {@link Leg}s contained within the airway. These legs are generally flyable bi-directionally unless
   * explicitly restricted otherwise.
   */
  List<? extends Leg> legs();
}
