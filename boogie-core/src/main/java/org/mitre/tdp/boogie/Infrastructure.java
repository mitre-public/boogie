package org.mitre.tdp.boogie;

import java.io.Serializable;

/**
 * Base interface for all infrastructure elements.
 * <p>
 * The general expectation of all infrastructure elements is that the combination of the identifier,
 * region, type, and source determine a unique object, at least within any given update cycle (given
 * most infrastructure resources are republished cyclically e.g. NFDC, CIFP, JEPPESEN, etc.).
 */
public interface Infrastructure extends Serializable {

  /**
   * The string name of the infrastructure elemtn. Typically these should be relatively standard across
   * all implementations of the interface.
   * <p>
   * e.g. WAYCO, HPO, J151, GNDLF1, KORD, 16L, etc.
   */
  String identifier();

  /**
   * The navigational database source for the information.
   */
  NavigationSource source();
}
