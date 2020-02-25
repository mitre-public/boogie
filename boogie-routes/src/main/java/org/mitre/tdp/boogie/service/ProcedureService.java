package org.mitre.tdp.boogie.service;

import java.util.Collection;

import org.mitre.tdp.boogie.alg.approach.ApproachPredictor;
import org.mitre.tdp.boogie.alg.graph.ProcedureGraph;

/**
 * Special lookup service for {@link ProcedureGraph} objects which includes a lookup method
 * for procedures by airport for use in {@link ApproachPredictor}s.
 */
public interface ProcedureService extends LookupService<ProcedureGraph> {

  /**
   * Returns a collection of all known procedures matching the given airport identifier.
   */
  Collection<ProcedureGraph> allMatchingAirport(String airport);
}
