package org.mitre.boogie.xml.assemble;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.arinc.assemble.ReciprocalRunwayIdentifier;
import org.mitre.boogie.xml.model.ArincAirport;
import org.mitre.boogie.xml.model.ArincLocalizerGlideSlope;
import org.mitre.boogie.xml.model.ArincRecords;
import org.mitre.boogie.xml.model.ArincRunway;

/**
 * Assembler interface for converting {@link ArincAirport} records into a client-defined output class representing an airport.
 *
 * <p>Because the XML model is hierarchical, the assembler does not require an external terminal area database. Runways,
 * helipads, and localizer/glideslope records are all accessed directly from the {@link ArincAirport}.
 *
 * <p>This class can be used with the {@link AirportAssemblyStrategy#standard()} to generate lightweight Boogie-defined
 * {@link Airport} implementations that can be used with other Boogie algorithms.
 */
public interface AirportAssembler<A> {

  static AirportAssembler<Airport> standard() {
    return withStrategy(AirportAssemblyStrategy.standard());
  }

  static <A, R, H> AirportAssembler<A> withStrategy(AirportAssemblyStrategy<A, R, H> strategy) {
    return new Standard<>(strategy);
  }

  Collection<A> assemble(ArincRecords records);

  final class Standard<A, R, H> implements AirportAssembler<A> {

    private static final ReciprocalRunwayIdentifier RECIPROCAL_ID = ReciprocalRunwayIdentifier.INSTANCE;

    private final AirportAssemblyStrategy<A, R, H> strategy;

    private Standard(AirportAssemblyStrategy<A, R, H> strategy) {
      this.strategy = requireNonNull(strategy);
    }

    @Override
    public Collection<A> assemble(ArincRecords records) {
      return records.airports().stream()
          .map(this::assembleOne)
          .toList();
    }

    private A assembleOne(ArincAirport airport) {
      List<ArincRunway> arincRunways = airport.runways();

      Map<String, ArincRunway> runwayById = arincRunways.stream()
          .collect(Collectors.toMap(r -> r.pointInfo().identifier(), Function.identity(), (a, b) -> a));

      Map<String, ArincLocalizerGlideSlope> ilsByRunwayRef = airport.portInfo().localizerGlideSlopes()
          .orElse(List.of())
          .stream()
          .filter(ils -> ils.runwayRef().isPresent())
          .collect(Collectors.toMap(ils -> ils.runwayRef().get(), Function.identity(), (a, b) -> a));

      List<R> runways = new ArrayList<>();

      for (ArincRunway origin : arincRunways) {
        String originId = origin.pointInfo().identifier();

        ArincRunway reciprocal = RECIPROCAL_ID.apply(originId)
            .map(runwayById::get)
            .orElse(null);

        ArincLocalizerGlideSlope ilsGls1 = ilsByRunwayRef.get(originId);
        ArincLocalizerGlideSlope ilsGls2 = null;

        runways.add(strategy.convertRunway(airport, origin, reciprocal, ilsGls1, ilsGls2));
      }

      List<H> helipads = airport.portInfo().helipads()
          .orElse(List.of())
          .stream()
          .map(strategy::convertHelipad)
          .toList();

      return strategy.convertAirport(airport, runways, helipads);
    }
  }
}
