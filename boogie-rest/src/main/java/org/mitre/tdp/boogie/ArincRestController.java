package org.mitre.tdp.boogie;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.model.ArincAirport;
import org.mitre.tdp.boogie.arinc.model.ArincLocalizerGlideSlope;
import org.mitre.tdp.boogie.arinc.model.ArincNdbNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;
import org.mitre.tdp.boogie.arinc.model.ArincVhfNavaid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.Description;
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
 * Rest controller for handling all requests about/for ARINC-424 records prior to assembly into Boogie interfaces/models.
 */
@RestController
@RequestMapping("/arinc")
@Tag(name = "ARINC", description = "API providing access to underlying parsed ARINC-424 data in a more structured JSON format.")
final class ArincRestController {

  private final Gson gson;
  private final BoogieState boogieState;

  @Autowired
  ArincRestController(
      Gson gson,
      BoogieState boogieState
  ) {
    this.gson = requireNonNull(gson);
    this.boogieState = requireNonNull(boogieState);
  }

  @Operation(
      summary = "ARINC airport records.",
      description = "Returns a mapping from {airportId, airport} for a comma-delimited collection of airports."
  )
  @GetMapping(
      value = "/airports",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<String> airports(
      @Parameter(description = "Comma-delimited set of airport ICAO codes: KATL,KJFK,etc.")
      @RequestParam("airports") String airports
  ) {

    Map<String, ArincAirport> result = boogieState.arincQuerier()
        .arincAirports(airports.split(",", -1));

    return result.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(gson.toJson(result));
  }

  @Operation(
      summary = "ARINC runway records.",
      description = "Returns a mapping from {airportId, {runwayId, runway}} for a comma-delimited collection of airports."
  )
  @GetMapping(
      value = "/runways",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<String> runways(
      @Parameter(description = "Comma-delimited set of airport ICAO codes: KATL,KJFK,etc.")
      @RequestParam("airports") String airports
  ) {

    Map<String, Map<String, ArincRunway>> result = boogieState.arincQuerier()
        .arincRunways(airports.split(",", -1));

    return result.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(gson.toJson(result));
  }

  @Operation(
      summary = "ARINC localizer records.",
      description = "Returns a mapping from {airportId, {localizerId, localizer}} for a comma-delimited collection of airports."
  )
  @GetMapping(
      value = "/localizers",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<String> localizers(
      @Parameter(description = "Comma-delimited set of airport ICAO codes: KATL,KJFK,etc.")
      @RequestParam("airports") String airports
  ) {

    Map<String, Map<String, ArincLocalizerGlideSlope>> result = boogieState.arincQuerier()
        .arincLocalizers(airports.split(",", -1));

    return result.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(gson.toJson(result));
  }

  @Operation(
      summary = "ARINC NDB navaid records.",
      description = "Returns a mapping from {navaidId, {icaoRegion, ndbNavaid}} for a comma-delimited collection of NDB navaid identifiers."
  )
  @GetMapping(
      value = "/ndbNavaids",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<String> ndbNavaids(
      @Parameter(description = "Comma-delimited set of navaid identifiers: ATL,DTL,etc.")
      @RequestParam("identifiers") String identifiers
  ) {

    Map<String, Map<String, ArincNdbNavaid>> result = boogieState.arincQuerier()
        .arincNdbNavaids(identifiers.split(",", -1));

    return result.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(gson.toJson(result));
  }

  @Operation(
      summary = "ARINC VHF navaid records.",
      description = "Returns a mapping from {navaidId, {icaoRegion, vhfNavaid}} for a comma-delimited collection of VHF navaid identifiers."
  )
  @GetMapping(
      value = "/vhfNavaids",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<String> vhfNavaids(
      @Parameter(description = "Comma-delimited set of navaid identifiers: ATL,DTL,etc.")
      @RequestParam("identifiers") String identifiers
  ) {

    Map<String, Map<String, ArincVhfNavaid>> result = boogieState.arincQuerier()
        .arincVhfNavaids(identifiers.split(",", -1));

    return result.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(gson.toJson(result));
  }

  @Operation(
      summary = "ARINC procedure leg records.",
      description = "Returns a mapping from {procedureName, [procedureLegs]} for an airport and a comma-delimited collection of procedure names."
  )
  @GetMapping(
      value = "/legs",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<String> procedures(
      @Parameter(description = "The airport identifier to look for the procedures at, e.g. KATL.")
      @RequestParam("airport") String airport,
      @Parameter(description = "Comma-delimited set of procedure identifiers (all will be returned if none are specified), e.g. GNDLF2,HOBTT2,etc.")
      @RequestParam(value = "procedures", required = false) String procedures
  ) {

    Map<String, Collection<ArincProcedureLeg>> result = boogieState.arincQuerier()
        .arincProcedureLegs(airport, Optional.ofNullable(procedures).map(s -> s.split(",", -1)).orElse(new String[] {}));

    return result.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(gson.toJson(result));
  }
}
