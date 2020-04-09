package org.mitre.tdp.boogie.conformance.alg.assemble;

import java.util.Collection;
import java.util.List;

import org.mitre.tdp.boogie.Infrastructure;
import org.mitre.tdp.boogie.conformance.model.ConsecutiveLegs;

/** Assembler class  */
@FunctionalInterface
public interface ConsecutiveLegAssembler {

  List<ConsecutiveLegs> assemble(Collection<Infrastructure> infrastructure);
}
