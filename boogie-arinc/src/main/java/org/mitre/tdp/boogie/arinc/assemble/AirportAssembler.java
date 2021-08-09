package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Runway;
import org.mitre.tdp.boogie.arinc.database.TerminalAreaDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincAirport;
import org.mitre.tdp.boogie.arinc.model.ArincLocalizerGlideSlope;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;
import org.mitre.tdp.boogie.fn.PentaFunction;
import org.mitre.tdp.boogie.model.BoogieAirport;

/**
 * Functional class for converting a {@link ArincAirport} record into an implementation of the {@link Airport} interface with
 * injectable functionality for constructing that concrete data class.
 * <br>
 * This class provides a default implementation of that conversion targeting the {@link BoogieAirport} data model.
 */
public final class AirportAssembler implements Function<ArincAirport, Airport> {

  private final TerminalAreaDatabase terminalAreaDatabase;

  /**
   * Function for converting a {@link ArincAirport} and a collection of associated runways into a {@link Airport}.
   * <br>
   * The list of runways may be empty.
   */
  private final BiFunction<ArincAirport, List<Runway>, Airport> airportConverter;
  /**
   * Converter for transforming the arrival + (optional) departure end of a runway and its (optional) associated primary/secondary
   * localizer and glide slope.
   */
  private final PentaFunction<ArincAirport, ArincRunway, ArincRunway, ArincLocalizerGlideSlope, ArincLocalizerGlideSlope, Runway> runwayConverter;

  public AirportAssembler(TerminalAreaDatabase terminalAreaDatabase) {
    this(
        terminalAreaDatabase,
        ArincToBoogieConverterFactory::newAirportFrom,
        ArincToBoogieConverterFactory::newRunwayFrom
    );
  }

  public AirportAssembler(
      TerminalAreaDatabase terminalAreaDatabase,
      BiFunction<ArincAirport, List<Runway>, Airport> airportConverter,
      PentaFunction<ArincAirport, ArincRunway, ArincRunway, ArincLocalizerGlideSlope, ArincLocalizerGlideSlope, Runway> runwayConverter) {
    this.terminalAreaDatabase = requireNonNull(terminalAreaDatabase);
    this.airportConverter = requireNonNull(airportConverter);
    this.runwayConverter = requireNonNull(runwayConverter);
  }

  @Override
  public Airport apply(ArincAirport arincAirport) {
    requireNonNull(arincAirport);

    Collection<ArincRunway> arincRunways = terminalAreaDatabase.runwaysAt(
        arincAirport.airportIdentifier(),
        arincAirport.airportIcaoRegion()
    );

    List<Runway> runways = new ArrayList<>();

    if (!arincRunways.isEmpty()) {
      ReciprocalRunwayPairer.INSTANCE.apply(arincRunways).stream()
          // add in the other direction for the reciprocal pairing (a,b) -> (a,b),(b,a)
          .flatMap(pair -> pair.second() != null ? Stream.of(pair, Pair.of(pair.second(), pair.first())) : Stream.of(pair))
          .map(pair -> runwayConverter.apply(
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

    return airportConverter.apply(arincAirport, runways);
  }
}
