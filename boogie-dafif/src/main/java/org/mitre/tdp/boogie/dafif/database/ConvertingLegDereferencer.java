package org.mitre.tdp.boogie.dafif.database;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.dafif.assemble.FixAssemblyStrategy;
import org.mitre.tdp.boogie.dafif.model.DafifRunway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConvertingLegDereferencer<F> {
  private static final Logger LOG = LoggerFactory.getLogger(ConvertingLegDereferencer.class);

  private final DafifFixDatabase fixDatabase;
  private final DafifTerminalAreaDatabase terminalAreaDatabase;
  private final FixAssemblyStrategy<F> fixAssemblyStrategy;

  private ConvertingLegDereferencer(DafifFixDatabase fixDatabase, DafifTerminalAreaDatabase terminalAreaDatabase, FixAssemblyStrategy<F> fixAssemblyStrategy) {
    this.fixDatabase = fixDatabase;
    this.terminalAreaDatabase = terminalAreaDatabase;
    this.fixAssemblyStrategy = fixAssemblyStrategy;
  }

  public static <F> ConvertingLegDereferencer<F> from(DafifFixDatabase fixDatabase, DafifTerminalAreaDatabase terminalAreaDatabase, FixAssemblyStrategy<F> fixAssemblyStrategy) {
    return new ConvertingLegDereferencer<>(fixDatabase, terminalAreaDatabase, fixAssemblyStrategy);
  }

  public F fix(String wptDesc, String airportIdentifier, String waypointIdent, String countryCode) {
    return switch (wptDesc) {
      case "A" -> terminalAreaDatabase.airportAt(waypointIdent, countryCode)
          .map(i -> fixAssemblyStrategy.convertAirport(i).stream())
          .flatMap(Stream::findFirst)
          .orElse(null);
      case "P", "E" -> fixDatabase.waypoint(waypointIdent, countryCode)
          .map(i -> fixAssemblyStrategy.convertWaypoint(i).stream())
          .flatMap(Stream::findFirst)
          .orElse(null);
      case "G" -> terminalAreaDatabase.runwaysAt(airportIdentifier).stream()
          .filter(r -> waypointIdent.contains(r.lowEndIdentifier()) || waypointIdent.contains(r.highEndIdentifier()))
          .findFirst()
          .map(r -> fixAssemblyStrategy.convertRunwayEnd(r, waypointIdent).stream())
          .flatMap(Stream::findFirst)
          .orElse(null);
      case "N", "V" -> fixDatabase.waypoint(waypointIdent, countryCode).stream()
          .map(fixDatabase::navaidFor)
          .flatMap(Optional::stream)
          .findFirst()
          .map(i -> fixAssemblyStrategy.convertNavaid(i).stream())
          .flatMap(Stream::findFirst)
          .orElse(null);
      default -> null;
    };
  }

  public F nav1(String airportIdentifier, String nav1Ident, String nav1Type, String nav1Country, Integer nav1KeyCode) {
    return switch (nav1Type) {
      case "1", "2", "3", "4", "5", "7", "9" -> fixDatabase.navaid(nav1Ident, nav1Country, Integer.parseInt(nav1Type), nav1KeyCode)
          .map(n -> fixAssemblyStrategy.convertNavaid(n).stream())
          .flatMap(Stream::findFirst)
          .orElse(null);
      case "P", "S" -> {
        LOG.warn("MLS What year is it nav1 type: '{}' for navaid {} at {}", nav1Type, nav1Ident, airportIdentifier);
        yield null;
      }
      case "Z" -> terminalAreaDatabase.airport(airportIdentifier).stream() //yes this is how you must do it :(
          .findFirst()
          .map(a -> {
            Collection<DafifRunway> runways = terminalAreaDatabase.runwaysAt(airportIdentifier);
            return runways.stream()
                .flatMap(r -> terminalAreaDatabase.ilsComponentsForRunway(r).stream())
                .filter(i -> i.ilsNavaidIdentifier().equals(Optional.of(nav1Ident)))
                .findFirst()
                .orElse(null);
          })
          .map(i -> fixAssemblyStrategy.convertIls(i).stream())
          .flatMap(Stream::findFirst)
          .orElse(null);
      case "D" -> terminalAreaDatabase.ilsByNavaidIdentifier(airportIdentifier, nav1Ident)
          .map(i -> fixAssemblyStrategy.convertIls(i).stream())
          .flatMap(Stream::findFirst)
          .orElse(null);
      default -> {
        LOG.warn("Unhandled nav1 type: '{}' for navaid {} at {}", nav1Type, nav1Ident, airportIdentifier);
        yield null;
      }
    };
  }

  public F arcCenter(String waypointIdent, String waypointCountry) {
    return fixDatabase.waypoint(waypointIdent, waypointCountry)
        .map(i -> fixAssemblyStrategy.convertWaypoint(i).stream())
        .flatMap(Stream::findFirst)
        .orElse(null);
  }
}
