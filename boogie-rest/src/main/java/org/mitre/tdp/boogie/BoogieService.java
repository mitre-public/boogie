package org.mitre.tdp.boogie;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.mitre.tdp.boogie.contract.arinc.*;
import org.mitre.tdp.boogie.contract.routes.*;
import org.mitre.tdp.boogie.contract.routes.Airport;
import org.mitre.tdp.boogie.contract.routes.Procedure;
import org.mitre.tdp.boogie.contract.routes.Airway;
import org.mitre.tdp.boogie.contract.routes.Fix;

public interface BoogieService {

  Optional<ArincAirport> arincAirport(String airportIdentifier, String airportIcaoRegion);

  Set<ArincLocalizerGlideSlope> arincLocalizerGlideSlope(String airportIdentifier, String airportIcaoRegion);

  Set<ArincNdbNavaid> arincNdbNavaid(String identifier);

  Set<ArincProcedureLeg> arincProcedureLegs(String airportIdentifier, String airportIcaoRegion);

  Set<ArincRunway> arincRunway(String airportIdentifier, String airportIcaoRegion);

  Optional<ArincVhfNavaid> arincVhfNavaid(String navaidIdentifier, String navaidIcaoRegion);

  Set<Fix> fix(String identifier);

  Set<Fix> fix(String fixIdentifier, String fixIcaoRegion);

  Optional<Airport> airport(String airportIdentifier, String airportIcaoRegion);

  Set<Airport> airport(String airportIdentifier);

  Set<Airport> airportsInRegion(String icaoRegion);

  Collection<String> allIcaoRegions();

  Set<Procedure> proceduresAt(String airportIdentifier, String airportIcaoRegion);

  Set<Airway> airway(String Identifier);

  Optional<ExpandedRoute> expandedRoute(String routeString, String departureRunway, String arrivalRunway, String requiredNavigationEquipage);
}
