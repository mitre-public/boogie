package org.mitre.tdp.boogie;

/**
 * Base interface for all infrastructure elements.
 *
 * The general expectation of all infrastructure elements is that the combination of the identifier,
 * region, type, and source determine a unique object, at least within any given update cycle (given
 * most infrastructure resources are republished cyclically e.g. NFDC, CIFP, JEPPESEN, etc.).
 */
public interface Infrastructure {

  /**
   * The string name of the infrastructure elemtn. Typically these should be relatively standard across
   * all implementations of the interface.
   *
   * e.g. WAYCO, HPO, J151, GNDLF1, KORD, 16L, etc.
   */
  String identifier();

//  /**
//   * A regional identifier for the infrastructure element. Typically these would be things like ICAO
//   * regions for fix, airport, etc. elements (e.g. K2, YT, etc.). However for others like runways it
//   * makes sense to use something like the airport identifier for this.
//   */
//  String region();
//
//  /**
//   * Some type indication for the infrastructure element.. Generally speaking this should provide info
//   * about the kind of infrastructure element we're referring to. However the format of this typing need
//   * not be constrained a-la enum values across all implementations of the interface out of need for
//   * flexibility reasons.
//   *
//   * e.g. for models built off the ARINC424 spec this is generally the combination section/subsection code.
//   */
//  String type();

  /**
   * The navigational database source for the information.
   */
  NavigationSource source();
}
