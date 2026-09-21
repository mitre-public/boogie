package org.mitre.boogie.xml.fixtures;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.mitre.boogie.xml.v23_4.generated.Airport;
import org.mitre.boogie.xml.v23_4.generated.Dme;
import org.mitre.boogie.xml.v23_4.generated.LocalizerGlideslope;
import org.mitre.boogie.xml.v23_4.generated.Runway;
import org.mitre.boogie.xml.v23_4.generated.Waypoint;

/** Links the DAFIF fixture's database keys to its generated JAXB objects. */
final class DafifXmlReferences {
  private final Map<String, Airport> airports = new HashMap<>();
  private final Map<String, Airport> airportAliases = new HashMap<>();
  private final Set<String> ambiguousAirportAliases = new HashSet<>();
  private final Map<AirportPointKey, Runway> runways = new HashMap<>();
  private final Map<AirportPointKey, LocalizerGlideslope> localizers = new HashMap<>();
  private final Set<AirportPointKey> ambiguousLocalizers = new HashSet<>();
  private final Map<IlsDmeKey, Dme> ilsDmes = new HashMap<>();
  private final Set<IlsDmeKey> ambiguousIlsDmes = new HashSet<>();
  private final Map<WaypointKey, Waypoint> waypoints = new HashMap<>();
  private final Map<WaypointKey, Object> waypointNavaids = new HashMap<>();
  private final Map<NavaidKey, Object> navaids = new HashMap<>();

  void addAirport(String airportId, Airport airport) {
    add(airports, clean(airportId), airport);
  }

  Airport airport(String airportId) {
    return Objects.requireNonNull(airports.get(clean(airportId)), () -> "Missing DAFIF airport: " + airportId);
  }

  void addAirportAlias(String alias, Airport airport) {
    String key = clean(alias);
    if (key.isEmpty()) {
      return;
    }
    Airport previous = airportAliases.putIfAbsent(key, airport);
    if (previous != null && previous != airport) {
      ambiguousAirportAliases.add(key);
    }
  }

  Airport airportByIdentifier(String alias) {
    String key = clean(alias);
    if (ambiguousAirportAliases.contains(key)) {
      return null;
    }
    return airportAliases.get(key);
  }

  void addRunway(String airportId, String ident, Runway runway) {
    add(runways, new AirportPointKey(clean(airportId), clean(ident)), runway);
  }

  Runway runway(String airportId, String ident) {
    return runways.get(new AirportPointKey(clean(airportId), clean(ident)));
  }

  void addLocalizer(String airportId, String ident, LocalizerGlideslope localizer) {
    AirportPointKey key = new AirportPointKey(clean(airportId), clean(ident));
    LocalizerGlideslope previous = localizers.putIfAbsent(key, localizer);
    if (previous != null && previous != localizer) {
      ambiguousLocalizers.add(key);
    }
  }

  LocalizerGlideslope localizer(String airportId, String ident) {
    AirportPointKey key = new AirportPointKey(clean(airportId), clean(ident));
    if (ambiguousLocalizers.contains(key)) {
      return null;
    }
    return localizers.get(key);
  }

  void addWaypoint(String ident, String country, Waypoint waypoint) {
    add(waypoints, new WaypointKey(clean(ident), clean(country)), waypoint);
  }

  void addIlsDme(String airportId, String componentType, String ident, Dme dme) {
    IlsDmeKey key = new IlsDmeKey(clean(airportId), clean(componentType), clean(ident));
    Dme previous = ilsDmes.putIfAbsent(key, dme);
    if (previous != null && previous != dme) {
      ambiguousIlsDmes.add(key);
    }
  }

  Dme ilsDme(String airportId, String componentType, String ident) {
    IlsDmeKey key = new IlsDmeKey(clean(airportId), clean(componentType), clean(ident));
    if (ambiguousIlsDmes.contains(key)) {
      return null;
    }
    return ilsDmes.get(key);
  }

  Waypoint waypoint(String ident, String country) {
    return waypoints.get(new WaypointKey(clean(ident), clean(country)));
  }

  void addWaypointNavaid(String ident, String country, Object navaid) {
    if (navaid != null) {
      add(waypointNavaids, new WaypointKey(clean(ident), clean(country)), navaid);
    }
  }

  Object waypointNavaid(String ident, String country) {
    return waypointNavaids.get(new WaypointKey(clean(ident), clean(country)));
  }

  void addNavaid(String ident, String country, int type, int keyCode, Object navaid) {
    add(navaids, new NavaidKey(clean(ident), clean(country), type, keyCode), navaid);
  }

  Object navaid(String ident, String country, int type, int keyCode) {
    return navaids.get(new NavaidKey(clean(ident), clean(country), type, keyCode));
  }

  static String id(String... components) {
    StringBuilder key = new StringBuilder();
    for (String component : components) {
      String value = clean(component);
      key.append(value.length()).append(':').append(value);
    }
    return "dafif-" + UUID.nameUUIDFromBytes(key.toString().getBytes(StandardCharsets.UTF_8));
  }

  private static <K, V> void add(Map<K, V> values, K key, V value) {
    V previous = values.putIfAbsent(key, value);
    if (previous != null && previous != value) {
      throw new IllegalArgumentException("Duplicate DAFIF reference: " + key);
    }
  }

  private static String clean(String value) {
    return Objects.toString(value, "").trim();
  }

  private record AirportPointKey(String airport, String ident) {}
  private record IlsDmeKey(String airport, String componentType, String ident) {}
  private record WaypointKey(String ident, String country) {}
  private record NavaidKey(String ident, String country, int type, int keyCode) {}
}
