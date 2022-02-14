package org.mitre.tdp.boogie;

import static java.util.Objects.requireNonNull;
import static java.util.function.Function.identity;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.alg.ExpandedRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

/**
 * Rest controller for handling all requests against assembled Boogie classes and interfaces as well as route expansion.
 */
@RestController
@RequestMapping("/routes")
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

  @GetMapping(
      value = "/airports",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Description("Returns the assembled ARINC Airport record(s) matching the provided comma-delimited airport IDs.")
  public ResponseEntity<String> airports(@RequestParam("airports") String airports) {

    Map<String, Airport> result = boogieState.airports(airports.split(",", -1)).stream()
        .collect(Collectors.toMap(Airport::airportIdentifier, identity()));

    return result.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(gson.toJson(result));
  }

  @GetMapping(
      value = "/fixes",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Description("Returns the assembled ARINC Fix record(s) matching the provided comma-delimited fix IDs.")
  public ResponseEntity<String> fixes(@RequestParam("fixes") String fixes) {

    Map<String, Fix> result = boogieState.fixes(fixes.split(",", -1)).stream()
        .collect(Collectors.toMap(Fix::fixIdentifier, identity()));

    return result.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(gson.toJson(result));
  }

  @GetMapping(
      value = "/airways",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Description("Returns the assembled ARINC Airway record(s) matching the provided comma-delimited airway IDs.")
  public ResponseEntity<String> airways(@RequestParam("airways") String airways) {

    Map<String, Airway> result = boogieState.airways(airways.split(",", -1)).stream()
        .collect(Collectors.toMap(Airway::airwayIdentifier, identity()));

    return result.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(gson.toJson(result));
  }

  @GetMapping(
      value = "/procedures",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Description("Returns the assembled ARINC Procedure record(s) matching the provided comma-delimited procedure IDs.")
  public ResponseEntity<String> procedures(@RequestParam("procedures") String procedures) {

    Map<String, Procedure> result = boogieState.procedures(procedures.split(",", -1)).stream()
        .collect(Collectors.toMap(Procedure::procedureIdentifier, identity()));

    return result.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(gson.toJson(result));
  }

  @GetMapping(
      value = "/expand",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Description("Returns the result of performing route expansion given the provided input route string and optional arr/dep runway + equipage.")
  public ResponseEntity<String> fixes(
      @RequestParam("route") String route,
      @RequestParam(value = "departure-runway", required = false) String departureRunway,
      @RequestParam(value = "arrival-runway", required = false) String arrivalRunway,
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
