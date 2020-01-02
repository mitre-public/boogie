package org.mitre.tdp.boogie.conformance.scorers;

import org.mitre.tdp.boogie.conformance.ConformablePoint;
import org.mitre.tdp.boogie.conformance.Scorer;

/**
 * Top level class for leg scoring, providing access to both the previous and the subsequent
 * legs as declared in the procedure/airway definition.
 */
public interface LegScorer extends Scorer<ConformablePoint> {}
