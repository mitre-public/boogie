package org.mitre.tdp.boogie;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.contract.arinc.*;
import org.mitre.tdp.boogie.contract.routes.Airport;
import org.mitre.tdp.boogie.contract.routes.Airway;
import org.mitre.tdp.boogie.contract.routes.Fix;
import org.mitre.tdp.boogie.contract.routes.Procedure;
import org.mitre.tdp.boogie.contract.routes.*;
import org.mitre.tdp.boogie.convert.arinc.*;
import org.mitre.tdp.boogie.convert.routes.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * This class caches a cycle's worth of navigation data and then provides what is asked for.
 * The route expansion cache's unique expand requests or expands based on new inputs.
 */
@CrossOrigin
@RestController
@RequestMapping("/nav")
@Tag(name = "Route Expansion and Navigation Information", description = "API for performing expansion of flight plan route strings into 2D paths and queries on arinc 424 files.")
public final class SingleCycleController implements BoogieService {

  private final BoogieState boogieState;

  private static final ConvertExpandedRoute EXPAND = ConvertExpandedRoute.INSTANCE;
  private static final ConvertAirport ARPT = ConvertAirport.INSTANCE;
  private static final ConvertProcedure PROC = ConvertProcedure.INSTANCE;
  private static final ConvertAirway AIRWAY = ConvertAirway.INSTANCE;
  private static final ConvertFix FIX = ConvertFix.INSTANCE;
  private static final ConvertArincVhfNavaid ARINC_VHF = ConvertArincVhfNavaid.INSTANCE;
  private static final ConvertArincAirport ARINC_ARPT = ConvertArincAirport.INSTANCE;
  private static final ConvertArincRunway ARINC_RUNWAY = ConvertArincRunway.INSTANCE;
  private static final ConvertArincNdbNavaid ARINC_NDB = ConvertArincNdbNavaid.INSTANCE;
  private static final ConvertArincProcedureLeg ARINC_LEG = ConvertArincProcedureLeg.INSTANCE;
  private static final ConvertArincLocalizerGlideSlope ARINC_ILS = ConvertArincLocalizerGlideSlope.INSTANCE;

  @Autowired
  public SingleCycleController(BoogieState boogieState) {
    this.boogieState = boogieState;
  }

  @Operation(summary = "ARINC 424 Airport", description =  "The ARINC 424 encoding for airports")
  @GetMapping(
      value = "/arinc-airport",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Override
  public Optional<ArincAirport> arincAirport(
      @Parameter(description = "airport identifier (e.g., KBOS, 1A0)")
      @RequestParam("airportIdentifier") String airportIdentifier,
      @Parameter(description = "Airport icao region (e.g., K1)")
      @RequestParam("airportIcaoRegion") String airportIcaoRegion) {
    return boogieState.arincQuerier().arincAirport(airportIdentifier, airportIcaoRegion).map(ARINC_ARPT);
  }

  @Operation(summary = "ARINC 424 Localizer and Glideslopes", description =  "The ARINC 424 encoding for ILS/LOCs")
  @GetMapping(
      value = "/arinc-ilsloc",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Override
  public Set<ArincLocalizerGlideSlope> arincLocalizerGlideSlope(
      @Parameter(description = "airport identifier (e.g., KBOS, 1A0)")
      @RequestParam("airportIdentifier") String airportIdentifier,
      @Parameter(description = "Airport icao region (e.g., K1)")
      @RequestParam("airportIcaoRegion") String airportIcaoRegion) {
    return boogieState.arincQuerier().arincLocalizer(airportIdentifier, airportIcaoRegion).values().stream().map(ARINC_ILS).collect(Collectors.toSet());
  }

  @Operation(summary = "ARINC 424 NDB Navaids", description =  "The ARINC 424 encoding for NDBs with matching identifiers")
  @GetMapping(
      value = "/arinc-ndb",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Override
  public Set<ArincNdbNavaid> arincNdbNavaid(
      @Parameter(description = "The identifier being searched on")
      @RequestParam("identifier") String identifier) {
    return boogieState.arincQuerier().arincNdbNavaids(identifier).get(identifier).values().stream().map(ARINC_NDB).collect(Collectors.toSet());
  }

  @Operation(summary = "ARINC 424 Procedure Legs", description =  "The ARINC 424 encoding for procedure legs at an airport")
  @GetMapping(
      value = "/arinc-legs",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Override
  public Set<ArincProcedureLeg> arincProcedureLegs(
      @Parameter(description = "airport identifier (e.g., KBOS, 1A0)")
      @RequestParam("airportIdentifier") String airportIdentifier,
      @Parameter(description = "Airport icao region (e.g., K1)")
      @RequestParam("airportIcaoRegion") String airportIcaoRegion) {
    return boogieState.arincQuerier().arincProcedureLegs(airportIdentifier, airportIcaoRegion).stream().map(ARINC_LEG).collect(Collectors.toSet());
  }

  @Operation(summary = "ARINC 424 Runways", description =  "The ARINC 424 runway encoding at an airport")
  @GetMapping(
      value = "/arinc-runway",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Override
  public Set<ArincRunway> arincRunway(
      @Parameter(description = "airport identifier (e.g., KBOS, 1A0)")
      @RequestParam("airportIdentifier") String airportIdentifier,
      @Parameter(description = "Airport icao region (e.g., K1)")
      @RequestParam("airportIcaoRegion") String airportIcaoRegion) {
    return boogieState.arincQuerier().arincRunwaysAt(airportIdentifier, airportIcaoRegion).stream().map(ARINC_RUNWAY).collect(Collectors.toSet());
  }

  @Operation(summary = "ARINC 424 VHF Navaid", description =  "The ARINC 424 encoding for the VHF Navaid")
  @GetMapping(
      value = "/arinc-vhf",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Override
  public Optional<ArincVhfNavaid> arincVhfNavaid(
      @Parameter(description = "navaid identifier (e.g., VOR)")
      @RequestParam("navaidIdentifier") String navaidIdentifier,
      @Parameter(description = "navaid region")
      @RequestParam("navaidIcaoRegion") String navaidIcaoRegion) {
    return boogieState.arincQuerier().arincVhfNavaid(navaidIdentifier, navaidIcaoRegion).map(ARINC_VHF);
  }

  @Operation(summary = "Converted fixes from (e.g., waypoints, navaids)", description =  "Converts any fix with the matching identifier.")
  @GetMapping(
      value = "/ambiguous-fix",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Override
  public Set<Fix> fix(
      @Parameter(description = "The fix identifier (e.g, STRDR, VOR)")
      @RequestParam("fixIdentifier") String fixIdentifier) {
    return boogieState.fixes(fixIdentifier).stream().map(FIX).collect(Collectors.toSet());
  }

  @Operation(summary = "Converted fixes from (e.g., waypoints, navaids)", description = "Converts an fix in a region with the matching identifier.")
  @GetMapping(
      value = "/fix",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Override
  public Set<Fix> fix(
      @Parameter(description = "The fix identifier (e.g, STRDR, VOR)")
      @RequestParam("fixIdentifier") String fixIdentifier,
      @Parameter(description = "The fixes icao region (.e.g., K1)")
      @RequestParam("fixIcaoRegion") String fixIcaoRegion) {
    return boogieState.fixes(fixIdentifier).stream().filter(i -> i.fixRegion().equals(fixIcaoRegion)).map(FIX).collect(Collectors.toSet());
  }

  @Operation(summary = "Converted airport record", description = "Returns an airport if one is found with both the airport ident and the icao region.")
  @GetMapping(
      value = "/airport",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Override
  public Optional<Airport> airport(
      @Parameter(description = "The airport identifier (e.g., KBOS, 1A0)")
      @RequestParam("airportIdentifier") String airportIdentifier,
      @Parameter(description = "The airport icao region (e.g., K1, K2")
      @RequestParam("airportIcaoRegion") String airportIcaoRegion) {
    return boogieState.airports(airportIdentifier).stream().filter(i -> i.airportRegion().equals(airportIcaoRegion)).findFirst().map(ARPT);
  }

  @Operation(summary = "Converted airport with ambiguous name", description = "Returns a set of airports matching the ident, can be more than one.")
  @GetMapping(
      value = "/ambiguous-airport",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Override
  public Set<Airport> airport(
      @Parameter(description = "The airport identifier (e.g., KBOS, 1A0)")
      @RequestParam("airportIdentifier")
      String identifier) {
    return boogieState.airports(identifier).stream().map(ARPT).collect(Collectors.toSet());
  }

  @Operation(summary = "Provide the airports in a region", description = "Returns all airports in the two letter icao region.")
  @GetMapping(
      value = "/regions-airport",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Override
  public Set<Airport> airportsInRegion(
      @Parameter(description = "The icao region (e.g., K1, K2")
      @RequestParam("icaoRegion") String icaoRegion) {
    return boogieState.allAirportsInRegion(icaoRegion).stream().map(ARPT).collect(Collectors.toSet());
  }

  @Operation(summary = "Provides all 2 letter region codes in the source data", description = "This streams the airport collection and provides all unique 2 letter region codes.")
  @GetMapping(
      value = "/all-regions",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Override
  public Collection<String> allIcaoRegions() {
    return boogieState.allRegions();
  }

  @Operation(summary = "Provide the procedures at this airport", description = "Returns a set of converted procedures at this airport.")
  @GetMapping(
      value = "/procedures-at",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Override
  public Set<Procedure> proceduresAt(
      @Parameter(description = "The airport identifier (e.g., KBOS, 1A0)")
      @RequestParam("airportIdentifier") String airportIdentifier,
      @Parameter(description = "The airport icao region (e.g., K1, K2")
      @RequestParam("airportIcaoRegion") String airportIcaoRegion) {
    return boogieState.proceduresAt(airportIdentifier, airportIcaoRegion).stream().map(PROC).collect(Collectors.toSet());
  }

  @Operation(summary = "Provide the converted airways", description = "Returns a set of airways that match the ident, there can be more than one e.g., J1 in different regions")
  @GetMapping(
      value = "/airway",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Override
  public Set<Airway> airway(
      @Parameter(description = "The identifier for the airway(s) (e.g., J1, Q1)")
      @RequestParam("airwayIdentifier") String airwayIdentifier) {
    return boogieState.airways(airwayIdentifier).stream().map(AIRWAY).collect(Collectors.toSet());
  }

  @Operation(summary = "Route expansion", description = "Returns an expanded 2D route representing intended path of an aircraft based on it's filed route string.")
  @GetMapping(
      value = "/expanded-route",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Override
  public Optional<ExpandedRoute> expandedRoute(
      @Parameter(description = "The intended route of the flight (complete or partial), e.g. STRDR.GNDLF2.KATL")
      @RequestParam("route") String route,
      @Parameter(description = "The intended departure runway of the flight (used to resolve a departure runway transition), e.g. RW04L,RW16R")
      @RequestParam(value = "departure-runway", required = false) String departureRunway,
      @Parameter(description = "The intended arrival runway of the flight (used to resolve an arrival runway transition), e.g. RW28,RW03C")
      @RequestParam(value = "arrival-runway", required = false) String arrivalRunway,
      @Parameter(description = "The equipage of the aircraft (used to choose an approach procedure), e.g. one of [RNP,RNAV,CONV]")
      @RequestParam(value = "equipage", required = false) String equipage) {
    return boogieState.routeExpander().apply(route, departureRunway, arrivalRunway, Optional.ofNullable(equipage).map(RequiredNavigationEquipage::valueOf).orElse(null)).map(EXPAND);
  }
}
