package org.mitre.tdp.boogie;

/**
 * Interface for classes (typically enums) which represent a unique finite set of sources for navigational data. This is used
 * extensively within the project for grouping {@link Infrastructure} records such that they remain partitioned between sources.
 */
@FunctionalInterface
public interface NavigationSource {

  /**
   * The string name of the navigation source.
   */
  String name();
}
