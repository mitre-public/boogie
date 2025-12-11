package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.List;

import org.mitre.tdp.boogie.Heliport;
import org.mitre.tdp.boogie.arinc.database.ArincTerminalAreaDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincHelipad;
import org.mitre.tdp.boogie.arinc.model.ArincHeliport;

/**
 * Assembler class for converting {@link ArincHeliport} records into client-defined outputs.
 * <p>This class is used with a {@link HeliportAssemblyStrategy} to generate the implementations that are used with boogie algorithms</p>
 * @param <H> the heliport class
 */
@FunctionalInterface
public interface HeliportAssembler<H> {

  /**
   * Assembler using the {@link HeliportAssemblyStrategy#standard()} to create teh boogie defined Heliport
   * @param arincTerminalAreaDatabase containing the indexed terminal area 424 records
   * @return the assembler for default boogie objects.
   */
  static HeliportAssembler<Heliport> standard(ArincTerminalAreaDatabase arincTerminalAreaDatabase) {
    return usingStrategy(arincTerminalAreaDatabase, HeliportAssemblyStrategy.standard());
  }

  /**
   * This class assembles the heliports using the user defined objects and conversions.
   * @param arincTerminalAreaDatabase indexed with all terminal data.
   * @param heliportAssemblyStrategy the strategy to assemble the ports/pads into one.
   * @return the Assembler that will convert and assemble.
   * @param <H> the heliport class.
   * @param <P> the helipad class.
   */
  static<H,P> HeliportAssembler<H> usingStrategy(ArincTerminalAreaDatabase arincTerminalAreaDatabase, HeliportAssemblyStrategy<H, P> heliportAssemblyStrategy) {
    return new Standard<>(arincTerminalAreaDatabase, heliportAssemblyStrategy);
  }

  /**
   * This method will assemble the heliport and its pads into a client defined Heliport.
   * @param arincHeliport the arinc record to use.
   * @return the client defined heliport.
   */
  H assemble(ArincHeliport arincHeliport);

  final class Standard<H, P> implements HeliportAssembler<H> {
    private final ArincTerminalAreaDatabase arincTerminalAreaDatabase;
    private final HeliportAssemblyStrategy<H, P> strategy;

    public Standard(ArincTerminalAreaDatabase arincTerminalAreaDatabase, HeliportAssemblyStrategy<H, P> strategy) {
      this.arincTerminalAreaDatabase = arincTerminalAreaDatabase;
      this.strategy = strategy;
    }

    @Override
    public H assemble(ArincHeliport arincHeliport) {
      requireNonNull(arincHeliport);

      Collection<ArincHelipad> arincHelipads = arincTerminalAreaDatabase.helipadsAt(arincHeliport.heliportIdentifier(), arincHeliport.heliportIcaoRegion());
      List<P> helipads = arincHelipads.stream().map(strategy::convertHelipad).toList();

      return strategy.convertHeliport(arincHeliport, helipads);
    }
  }
}
