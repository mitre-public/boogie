package org.mitre.tdp.boogie;

import org.mitre.tdp.boogie.v18.ArincAirport;
import org.mitre.tdp.boogie.v18.ArincAirway;

/**
 * Top level interface for any models which provided decorated functionality on top of a baseline parsed {@link ArincRecord} object.
 * Typically extensions of this takes the form of interfaces provided typed method accessors to the declared set of record fields.
 *
 * See {@link ArincAirport}, {@link ArincAirway}, etc.
 */
@FunctionalInterface
public interface ArincRecordDecorator {

  /**
   * The parsed {@link ArincRecord} to decorate.
   */
  ArincRecord arincRecord();
}
