package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.arinc.database.TerminalAreaDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincAirport;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;

/**
 * Assembler class for converting {@link ArincAirport} records into a client-defined output class of type {@code A} representing
 * an airport.
 *
 * <p>This class can be used with the {@link AirportAssemblyStrategy#standard()} to generate lightweight Boogie-defined {@link Airport}
 * implementations that can be used with other Boogie algorithms.
 */
public final class AirportAssembler<A, R> implements Function<ArincAirport, A> {

  private final TerminalAreaDatabase terminalAreaDatabase;

  private final AirportAssemblyStrategy<A, R> strategy;

  public AirportAssembler(TerminalAreaDatabase terminalAreaDatabase, AirportAssemblyStrategy<A, R> strategy) {
    this.terminalAreaDatabase = requireNonNull(terminalAreaDatabase);
    this.strategy = requireNonNull(strategy);
  }

  /**
   * Create a new airport assembler with the given terminal area database and strategy for assembly as context. The assembler uses
   * the database to lookup relevant 424 records whose contents clients may wish to include in the generated model type.
   *
   * <p>Template types here notionally represent client-defined "Airport" and "Runway" classes.
   *
   * @param terminalAreaDatabase containing indexed terminal area 424 records
   * @param strategy             strategy class for converting related 424 records into runways and then combining those runways
   *                             into airports see {@link AirportAssemblyStrategy#standard()}
   */
  public static <A, R> AirportAssembler<A, R> create(TerminalAreaDatabase terminalAreaDatabase, AirportAssemblyStrategy<A, R> strategy) {
    return new AirportAssembler<>(terminalAreaDatabase, strategy);
  }

  @Override
  public A apply(ArincAirport arincAirport) {
    requireNonNull(arincAirport);

    Collection<ArincRunway> arincRunways = terminalAreaDatabase.runwaysAt(
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
              terminalAreaDatabase.primaryLocalizerGlideSlopeOf(
                  arincAirport.airportIdentifier(),
                  arincAirport.airportIcaoRegion(),
                  pair.first().runwayIdentifier()).orElse(null),
              terminalAreaDatabase.secondaryLocalizerGlideSlopeOf(
                  arincAirport.airportIdentifier(),
                  arincAirport.airportIcaoRegion(),
                  pair.first().runwayIdentifier()).orElse(null)
          ))
          .forEach(runways::add);
    }

    return strategy.convertAirport(arincAirport, runways);
  }
}
