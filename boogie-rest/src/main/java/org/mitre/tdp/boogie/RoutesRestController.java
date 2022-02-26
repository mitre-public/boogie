package org.mitre.tdp.boogie;

import static java.util.Objects.requireNonNull;
import static java.util.function.Function.identity;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.alg.ExpandedRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Rest controller for handling all requests against assembled Boogie classes and interfaces as well as route expansion.
 */
@RestController
@RequestMapping("/routes")
@Tag(name = "Route Expansion", description = "API for performing expansion of flight plan route strings into 2D paths.")
final class RoutesRestController {

  private final Gson gson;
  private final BoogieState boogieState;

  @Autowired
  RoutesRestController(
      Gson gson,
      BoogieState boogieState
  ) {
    this.gson = requireNonNull(gson);
    this.boogieState = requireNonNull(boogieState);
  }

  @Operation(summary = "Converted airport records", description = "Returns a mapping from {airportId, airport} (BoogieAirport.java) for a comma-delimited collection of airports.")
  @GetMapping(
      value = "/airports",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<String> airports(
      @Parameter(description = "Comma-delimited set of airport ICAO codes: KATL,KJFK,etc.")
      @RequestParam("airports") String airports
  ) {

    Map<String, Airport> result = boogieState.airports(airports.split(",", -1)).stream()
        .collect(Collectors.toMap(Airport::airportIdentifier, identity()));

    return result.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(gson.toJson(result));
  }

  @Operation(summary = "Converted fix records", description = "Returns a mapping from {fixId, fix} (BoogieFix.java) including navaids and waypoints for a comma-delimited collection of fix identifiers.")
  @GetMapping(
      value = "/fixes",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<String> fixes(
      @Parameter(description = "Comma-delimited set of fix identifiers: JMACK,ATL,SMAUG,etc.")
      @RequestParam("fixes") String fixes
  ) {

    Map<String, Fix> result = boogieState.fixes(fixes.split(",", -1)).stream()
        .collect(Collectors.toMap(Fix::fixIdentifier, identity()));

    return result.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(gson.toJson(result));
  }

  @Operation(summary = "Converted airway records", description = "Returns a mapping from {airwayId, airway} (BoogieAirway.java) for a comma-delimited collection of airway identifiers.")
  @GetMapping(
      value = "/airways",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<String> airways(
      @Parameter(description = "Comma-delimited set of airway identifiers: J121,V6,etc.")
      @RequestParam("airways") String airways
  ) {

    Map<String, Airway> result = boogieState.airways(airways.split(",", -1)).stream()
        .collect(Collectors.toMap(Airway::airwayIdentifier, identity()));

    return result.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(gson.toJson(result));
  }

  @Operation(
      summary = "Converted procedure records",
      description = "Returns a mapping from {procedureName, {airportId, procedure}} (BoogieProcedure.java) for a comma-delimited collection of procedure identifiers. (Note there can be multiple versions of a procedure at different airports) "
  )
  @GetMapping(
      value = "/procedures",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<String> procedures(
      @Parameter(description = "Comma-delimited set of procedure identifiers: GNDLF1,CHPPR1,H28-LZ,etc.")
      @RequestParam("procedures") String procedures
  ) {

    Map<String, Map<String, Procedure>> result = boogieState.procedures(procedures.split(",", -1)).stream()
        .collect(Collectors.groupingBy(Procedure::procedureIdentifier))
        .entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream().collect(Collectors.toMap(Procedure::airportIdentifier, identity()))));

    return result.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(gson.toJson(result));
  }

  @Operation(
      summary = "Route expansion",
      description = "Returns an expanded 2D route representing intended path of an aircraft based on it's filed route string."
  )
  @GetMapping(
      value = "/expand",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<String> fixes(
      @Parameter(description = "The intended route of the flight (complete or partial), e.g. STRDR.GNDLF2.KATL")
      @RequestParam("route") String route,
      @Parameter(description = "The intended departure runway of the flight (used to resolve a departure runway transition), e.g. RW04L,RW16R")
      @RequestParam(value = "departure-runway", required = false) String departureRunway,
      @Parameter(description = "The intended arrival runway of the flight (used to resolve an arrival runway transition), e.g. RW28,RW03C")
      @RequestParam(value = "arrival-runway", required = false) String arrivalRunway,
      @Parameter(description = "The equipage of the aircraft (used to choose an approach procedure), e.g. one of [RNP,RNAV,CONV]")
      @RequestParam(value = "equipage", required = false) String equipage
  ) {

    Optional<ExpandedRoute> expandedRoute = boogieState.routeExpander().apply(
        route,
        departureRunway,
        arrivalRunway,
        Optional.ofNullable(equipage).map(RequiredNavigationEquipage::valueOf).orElse(null)
    );

    return expandedRoute.map(gson::toJson).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
  }
}
