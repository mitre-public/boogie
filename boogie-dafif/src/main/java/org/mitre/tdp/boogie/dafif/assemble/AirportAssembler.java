package org.mitre.tdp.boogie.dafif.assemble;

import java.util.List;
import java.util.stream.Collectors;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.dafif.database.DafifTerminalAreaDatabase;
import org.mitre.tdp.boogie.dafif.model.DafifAirport;

public interface AirportAssembler<A> {
  A assemble(DafifAirport a);

  static AirportAssembler<Airport> standard(DafifTerminalAreaDatabase database) {
    return usingStrategy(database, AirportAssemblyStrategy.standard());
  }

  static <A, R> AirportAssembler<A> usingStrategy(DafifTerminalAreaDatabase database, AirportAssemblyStrategy<A, R> strategy) {
    return new Standard<>(database, strategy);
  }

  final class Standard<A, R> implements AirportAssembler<A> {
    private final DafifTerminalAreaDatabase database;
    private final AirportAssemblyStrategy<A, R> strategy;

    public Standard(DafifTerminalAreaDatabase database, AirportAssemblyStrategy<A, R> strategy) {
      this.database = database;
      this.strategy = strategy;
    }

    @Override
    public A assemble(DafifAirport a) {
      List<R> convertedRunways = database.runwaysAt(a.airportIdentification()).stream()
          .map(runway -> Pair.of(runway, database.addRunwayFor(runway).orElse(null)))
          .map(pair -> strategy.convertRunway(pair.first(), pair.second(), database.ilsComponentsForRunway(pair.first())))
          .flatMap(List::stream)
          .collect(Collectors.toList());
      return strategy.convertAirport(a, convertedRunways);
    }
  }
}
