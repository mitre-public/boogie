package org.mitre.tdp.boogie.arinc.assemble;

import java.util.Collection;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.arinc.database.ArincTerminalAreaDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincHeliport;

public interface HeliportAssembler<HPT> {
  //todo
  Stream<HPT> assemble(Collection<ArincHeliport> legs);

  static <HPT, HLPD> HeliportAssembler<HPT> usingStrategy(ArincTerminalAreaDatabase tad, HeliportAssemblyStrategy<HPT, HLPD> strategy) {
    return new Standard<>(tad, strategy);
  }

  final class Standard<HPT, HLPD> implements HeliportAssembler<HPT> {
    private final ArincTerminalAreaDatabase tad;
    private final HeliportAssemblyStrategy<HPT, HLPD> strategy;

    public Standard(ArincTerminalAreaDatabase tad, HeliportAssemblyStrategy<HPT, HLPD> strategy) {
      this.tad = tad;
      this.strategy = strategy;
    }

    @Override
    public Stream<HPT> assemble(Collection<ArincHeliport> legs) {
      throw new IllegalStateException("DID NOT DO HTIS YET");
    }
  }
}
