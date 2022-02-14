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

/**
 * Rest controller for handling all requests about/for ARINC-424 records prior to assembly into Boogie interfaces/models.
 */
@RestController
@RequestMapping("/arinc")
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

  @GetMapping(
      value = "/airports",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Description("Returns the ARINC Airport record(s) matching the provided comma-delimited airport IDs.")
  public ResponseEntity<String> airports(@RequestParam("airports") String airports) {

    Map<String, ArincAirport> result = boogieState.arincQuerier()
        .arincAirports(airports.split(",", -1));

    return result.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(gson.toJson(result));
  }

  @GetMapping(
      value = "/runways",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Description("Returns the ARINC Runway record(s) matching at the provided comma-delimited airport IDs.")
  public ResponseEntity<String> runways(@RequestParam("airports") String airports) {

    Map<String, Map<String, ArincRunway>> result = boogieState.arincQuerier()
        .arincRunways(airports.split(",", -1));

    return result.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(gson.toJson(result));
  }

  @GetMapping(
      value = "/localizers",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Description("Returns the ARINC Localizer record(s) matching at the provided comma-delimited airport IDs.")
  public ResponseEntity<String> localizers(@RequestParam("airports") String airports) {

    Map<String, Map<String, ArincLocalizerGlideSlope>> result = boogieState.arincQuerier()
        .arincLocalizers(airports.split(",", -1));

    return result.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(gson.toJson(result));
  }

  @GetMapping(
      value = "/ndbNavaids",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Description("Returns the ARINC NDB Navaid record(s) matching the provided comma-delimited navaid IDs.")
  public ResponseEntity<String> ndbNavaids(@RequestParam("identifiers") String identifiers) {

    Map<String, Map<String, ArincNdbNavaid>> result = boogieState.arincQuerier()
        .arincNdbNavaids(identifiers.split(",", -1));

    return result.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(gson.toJson(result));
  }

  @GetMapping(
      value = "/vhfNavaids",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Description("Returns the ARINC VHF Navaid record(s) matching the provided comma-delimited navaid IDs.")
  public ResponseEntity<String> vhfNavaids(@RequestParam("identifiers") String identifiers) {

    Map<String, Map<String, ArincVhfNavaid>> result = boogieState.arincQuerier()
        .arincVhfNavaids(identifiers.split(",", -1));

    return result.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(gson.toJson(result));
  }

  @GetMapping(
      value = "/legs",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Description("Returns the ARINC Procedure Leg record(s) for the given comma-delimited procedure(s) at the provided airport.")
  public ResponseEntity<String> procedures(
      @RequestParam("airport") String airport,
      @RequestParam(value = "procedures", required = false) String procedures
  ) {

    Map<String, Collection<ArincProcedureLeg>> result = boogieState.arincQuerier()
        .arincProcedureLegs(airport, Optional.ofNullable(procedures).map(s -> s.split(",", -1)).orElse(new String[] {}));

    return result.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(gson.toJson(result));
  }
}
