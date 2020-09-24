package org.mitre.tdp.boogie.service;

import java.util.Collection;

import org.mitre.tdp.boogie.alg.approach.ApproachPredictor;
import org.mitre.tdp.boogie.alg.graph.ProcedureGraph;
import org.mitre.tdp.boogie.models.Procedure;

/**
 * Special lookup service for {@link ProcedureGraph} objects which includes a lookup method
 * for procedures by airport for use in {@link ApproachPredictor}s.
 */
public interface ProcedureService extends LookupService<Procedure> {

  /**
   * Returns a collection of all known procedures matching the given airport identifier.
   */
  Collection<Procedure> allMatchingAirport(String airport);
}
