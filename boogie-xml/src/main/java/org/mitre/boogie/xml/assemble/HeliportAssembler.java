package org.mitre.boogie.xml.assemble;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.List;

import org.mitre.tdp.boogie.Heliport;
import org.mitre.boogie.xml.model.ArincHeliport;
import org.mitre.boogie.xml.model.ArincRecords;

/**
 * Assembler class for converting {@link ArincHeliport} records into a client-defined output class representing a heliport.
 *
 * <p>Because the XML model is hierarchical, the assembler does not require an external terminal area database. Helipads
 * are accessed directly from the {@link ArincHeliport}'s port info.
 *
 * <p>This class can be used with the {@link HeliportAssemblyStrategy#standard()} to generate lightweight Boogie-defined
 * {@link Heliport} implementations that can be used with other Boogie algorithms.
 */
public interface HeliportAssembler<H> {

  static HeliportAssembler<Heliport> standard() {
    return withStrategy(HeliportAssemblyStrategy.standard());
  }

  static <H, P> HeliportAssembler<H> withStrategy(HeliportAssemblyStrategy<H, P> strategy) {
    return new Standard<>(strategy);
  }

  Collection<H> assemble(ArincRecords records);

  H assembleOne(ArincHeliport heliport);

  final class Standard<H, P> implements HeliportAssembler<H> {

    private final HeliportAssemblyStrategy<H, P> strategy;

    private Standard(HeliportAssemblyStrategy<H, P> strategy) {
      this.strategy = requireNonNull(strategy);
    }

    @Override
    public Collection<H> assemble(ArincRecords records) {
      return records.heliports().stream()
          .map(this::assembleOne)
          .toList();
    }

    @Override
    public H assembleOne(ArincHeliport heliport) {
      List<P> helipads = heliport.portInfo().helipads()
          .orElse(List.of())
          .stream()
          .map(strategy::convertHelipad)
          .toList();

      return strategy.convertHeliport(heliport, helipads);
    }
  }
}
