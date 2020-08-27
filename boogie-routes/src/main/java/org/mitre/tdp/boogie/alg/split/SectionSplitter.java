package org.mitre.tdp.boogie.alg.split;

import java.util.List;

/**
 * Responsible for splitting the input route string into a sequence of elements matchable to infrastructure elements based on ID.
 * <p>
 * Returns a list of {@link SectionSplit}s for use downstream.
 */
@FunctionalInterface
public interface SectionSplitter {

  /**
   * Takes as an argument a route string and returns the split sections of the route mapping to specific infrastructure elements.
   */
  List<SectionSplit> splits(String route);
}
