package org.mitre.tdp.boogie.dafif.assemble;

import java.util.Collection;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.dafif.model.DafifTerminalParent;

public interface ProcedureAssembler<P> {

  Stream<P> assemble(Collection<DafifTerminalParent> parents);

  class Standard implements ProcedureAssembler<Procedure> {

    @Override
    public Stream<Procedure> assemble(Collection<DafifTerminalParent> parents) {
      return Stream.empty();
    }
  }
}
