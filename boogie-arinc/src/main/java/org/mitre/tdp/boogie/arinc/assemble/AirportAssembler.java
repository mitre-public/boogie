package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.arinc.database.ArincTerminalAreaDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincAirport;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;

/**
 * Assembler class for converting {@link ArincAirport} records into a client-defined output class of type {@code A} representing
 * an airport.
 *
 * <p>This class can be used with the {@link AirportAssemblyStrategy#standard()} to generate lightweight Boogie-defined {@link Airport}
 * implementations that can be used with other Boogie algorithms.
 */
@FunctionalInterface
public interface AirportAssembler<A> {

  /**
   * Assembler using the {@link AirportAssemblyStrategy#standard()} to create Boogie airport records when provided a backing
   * database for 424 records in the terminal airspace.
   *
   * @param arincTerminalAreaDatabase containing indexed terminal area 424 records
   */
  static AirportAssembler<Airport> standard(ArincTerminalAreaDatabase arincTerminalAreaDatabase) {
    return usingStrategy(arincTerminalAreaDatabase, AirportAssemblyStrategy.standard());
  }

  /**
   * Create a new airport assembler with the given terminal area database and strategy for assembly as context. The assembler uses
   * the database to lookup relevant 424 records whose contents clients may wish to include in the generated model type.
   *
   * <p>Template types here notionally represent client-defined "Airport" and "Runway" classes.
   *
   * @param arincTerminalAreaDatabase containing indexed terminal area 424 records
   * @param strategy             strategy class for converting related 424 records into runways and then combining those runways
   *                             into airports see {@link AirportAssemblyStrategy#standard()}
   */
  static <A, R> AirportAssembler<A> usingStrategy(ArincTerminalAreaDatabase arincTerminalAreaDatabase, AirportAssemblyStrategy<A, R> strategy) {
    return new Standard<>(arincTerminalAreaDatabase, strategy);
  }

  /**
   * Creates a new airport of type {@code A} from the provided {@link ArincAirport} record.
   */
  A assemble(ArincAirport airport);

  final class Standard<A, R> implements AirportAssembler<A> {

    private final ArincTerminalAreaDatabase arincTerminalAreaDatabase;

    private final AirportAssemblyStrategy<A, R> strategy;

    public Standard(ArincTerminalAreaDatabase arincTerminalAreaDatabase, AirportAssemblyStrategy<A, R> strategy) {
      this.arincTerminalAreaDatabase = requireNonNull(arincTerminalAreaDatabase);
      this.strategy = requireNonNull(strategy);
    }

    @Override
    public A assemble(ArincAirport arincAirport) {
      requireNonNull(arincAirport);

      Collection<ArincRunway> arincRunways = arincTerminalAreaDatabase.runwaysAt(
          arincAirport.airportIdentifier(),
          arincAirport.airportIcaoRegion()
      );

      List<R> runways = new ArrayList<>();

      if (!arincRunways.isEmpty()) {
        ReciprocalRunwayPairer.INSTANCE.apply(arincRunways).stream()
            // add in the other direction for the reciprocal pairing (a,b) -> (a,b),(b,a)
            .flatMap(pair -> pair.second() != null ? Stream.of(pair, Pair.of(pair.second(), pair.first())) : Stream.of(pair))
            .map(pair -> strategy.convertRunway(
                arincAirport,
                pair.first(),
                pair.second(),
                arincTerminalAreaDatabase.primaryLocalizerGlideSlopeOf(
                    arincAirport.airportIdentifier(),
                    arincAirport.airportIcaoRegion(),
                    pair.first().runwayIdentifier()).orElse(null),
                arincTerminalAreaDatabase.secondaryLocalizerGlideSlopeOf(
                    arincAirport.airportIdentifier(),
                    arincAirport.airportIcaoRegion(),
                    pair.first().runwayIdentifier()).orElse(null)
            ))
            .forEach(runways::add);
      }

      return strategy.convertAirport(arincAirport, runways);
    }
  }
}
