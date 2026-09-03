package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.List;

import org.mitre.tdp.boogie.Heliport;
import org.mitre.tdp.boogie.arinc.database.ArincTerminalAreaDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincHelipad;
import org.mitre.tdp.boogie.arinc.model.ArincHeliport;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;

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
   * @param <R> the runway class.
   * @param <P> the helipad class.
   */
  static <H, R, P> HeliportAssembler<H> usingStrategy(ArincTerminalAreaDatabase arincTerminalAreaDatabase, HeliportAssemblyStrategy<H, R, P> heliportAssemblyStrategy) {
    return new Standard<>(arincTerminalAreaDatabase, heliportAssemblyStrategy);
  }

  /**
   * This method will assemble the heliport and its pads into a client defined Heliport.
   * @param arincHeliport the arinc record to use.
   * @return the client defined heliport.
   */
  H assemble(ArincHeliport arincHeliport);

  final class Standard<H, R, P> implements HeliportAssembler<H> {
    private final ArincTerminalAreaDatabase arincTerminalAreaDatabase;
    private final HeliportAssemblyStrategy<H, R, P> strategy;

    public Standard(ArincTerminalAreaDatabase arincTerminalAreaDatabase, HeliportAssemblyStrategy<H, R, P> strategy) {
      this.arincTerminalAreaDatabase = requireNonNull(arincTerminalAreaDatabase);
      this.strategy = requireNonNull(strategy);
    }

    @Override
    public H assemble(ArincHeliport arincHeliport) {
      requireNonNull(arincHeliport);

      Collection<ArincRunway> arincRunways = arincTerminalAreaDatabase.heliportsRunwaysAt(
          arincHeliport.heliportIdentifier(),
          arincHeliport.heliportIcaoRegion()
      );
      List<R> runways = RunwayAssembly.directedPairs(arincRunways)
          .map(pair -> strategy.convertRunway(
              arincHeliport,
              pair.thisRunway(),
              pair.otherEnd(),
              arincTerminalAreaDatabase.heliportsPrimaryLocalizerGlideSlopeOf(
                  arincHeliport.heliportIdentifier(),
                  arincHeliport.heliportIcaoRegion(),
                  pair.thisRunway().runwayIdentifier()).orElse(null),
              arincTerminalAreaDatabase.heliportsSecondaryLocalizerGlideSlopeOf(
                  arincHeliport.heliportIdentifier(),
                  arincHeliport.heliportIcaoRegion(),
                  pair.thisRunway().runwayIdentifier()).orElse(null)
          ))
          .toList();

      Collection<ArincHelipad> arincHelipads = arincTerminalAreaDatabase.heliportsHelipadsAt(arincHeliport.heliportIdentifier(), arincHeliport.heliportIcaoRegion());
      List<P> helipads = arincHelipads.stream().map(strategy::convertHelipad).toList();

      return strategy.convertHeliport(arincHeliport, runways, helipads);
    }
  }
}
