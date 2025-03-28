package org.mitre.tdp.boogie.dafif.database;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.dafif.model.ConvertingDafifRecordConsumer;
import org.mitre.tdp.boogie.dafif.model.DafifAddRunway;
import org.mitre.tdp.boogie.dafif.model.DafifAirport;
import org.mitre.tdp.boogie.dafif.model.DafifIls;
import org.mitre.tdp.boogie.dafif.model.DafifNavaid;
import org.mitre.tdp.boogie.dafif.model.DafifRunway;
import org.mitre.tdp.boogie.dafif.model.DafifWaypoint;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

public final class DafifDatabaseFactory {

  private static final Function<DafifIls, IlsKey> ILS_KEY = ils -> new IlsKey(ils.airportIdentification(), ils.runwayIdentifier());
  private static final Function<DafifAirport, AirportKey> ARPT_KEY = arpt -> new AirportKey(arpt.airportIdentification());
  private static final Function<DafifWaypoint, WaypointKey> WPT_KEY = wpt -> new WaypointKey(wpt.waypointIdentifier(), wpt.countryCode());
  private static final Function<DafifNavaid, NavaidKey> NAV_KEY = nav -> new NavaidKey(nav.navaidIdentifier(), nav.navaidType(), nav.countryCode(), nav.navaidKeyCode());
  private static final Function<DafifRunway, AirportKey> RWY_KEY = rwy -> new AirportKey(rwy.airportIdentification());
  private static final Function<DafifAddRunway, AirportKey> ADD_Key = rwy -> new AirportKey(rwy.airportIdentification());
  private DafifDatabaseFactory()  {
    throw new IllegalStateException("Cannot instantiate static factory class.");
  }

  public static DafifFixDatabase newFixDatabase(Collection<DafifWaypoint> waypoints, Collection<DafifNavaid> navaids) {
    Map<WaypointKey, DafifWaypoint> waypointMap = waypoints.stream().collect(Collectors.toMap(WPT_KEY, Function.identity()));
    Map<NavaidKey, DafifNavaid> navaidMap = navaids.stream().collect(Collectors.toMap(NAV_KEY, Function.identity()));
    return new DafifFixDatabase(waypointMap, navaidMap);
  }

  public static DafifTerminalAreaDatabase newTerminalAreaDatabase(Collection<DafifAirport> airports, Collection<DafifRunway> runways, Collection<DafifAddRunway> addRunways, Collection<DafifIls> ils) {
    Map<AirportKey, DafifAirport> airportMap= airports.stream().collect(Collectors.toMap(ARPT_KEY, Function.identity()));
    Multimap<AirportKey, DafifRunway> runwayMap = Multimaps.index(runways, RWY_KEY::apply);
    Multimap<AirportKey, DafifAddRunway> addMap = Multimaps.index(addRunways, ADD_Key::apply);
    Multimap<IlsKey, DafifIls> ilsMap = Multimaps.index(ils, ILS_KEY::apply);

    return new DafifTerminalAreaDatabase(airportMap, runwayMap, addMap, ilsMap);
  }

  public static DafifTerminalAreaDatabase newTerminalAreaDatabase(ConvertingDafifRecordConsumer consumer) {
    return newTerminalAreaDatabase(consumer.dafifAirports(), consumer.dafifRunways(), consumer.dafifAddRunways(), consumer.dafifIls());
  }
}
