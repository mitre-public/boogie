package org.mitre.tdp.boogie.conformance.alg.assemble;

import java.util.Collection;
import java.util.List;

import org.mitre.tdp.boogie.Infrastructure;
import org.mitre.tdp.boogie.conformance.model.ConsecutiveLegs;

/**
 * Converts a collection of infrastructure elements into a collection of {@link ConsecutiveLegs}.
 */
@FunctionalInterface
public interface ConsecutiveLegAssembler {

  List<ConsecutiveLegs> assemble(Collection<Infrastructure> infrastructure);
}
