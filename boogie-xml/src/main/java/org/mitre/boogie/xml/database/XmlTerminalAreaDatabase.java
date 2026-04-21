package org.mitre.boogie.xml.database;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * An indexed collection of {@link PortPage}s providing airport-scoped terminal fix lookups.
 *
 * <p>Pages are indexed by the composite key {@code (identifier, icaoCode)} for exact lookups, and also
 * by identifier alone for convenience when the ICAO code is not known or not needed. Airport/heliport
 * identifiers are <b>not</b> globally unique &mdash; the same identifier may appear with different ICAO
 * codes &mdash; so the identifier-only lookup returns a {@link Collection} of matching pages.
 *
 * <p>This is the XML equivalent of the ARINC {@code ArincTerminalAreaDatabase}, simplified for the
 * hierarchical XML model where terminal records are already nested under their parent port.
 *
 * @param <F> the client fix type
 * @param <R> the client runway type
 * @param <H> the client helipad type
 */
public final class XmlTerminalAreaDatabase<F, R, H> {

  private final Map<PortKey, PortPage<F, R, H>> airportPagesByKey;
  private final Map<String, List<PortPage<F, R, H>>> airportPagesByIdent;
  private final Map<PortKey, PortPage<F, R, H>> heliportPagesByKey;
  private final Map<String, List<PortPage<F, R, H>>> heliportPagesByIdent;

  private XmlTerminalAreaDatabase(
      Map<PortKey, PortPage<F, R, H>> airportPagesByKey,
      Map<String, List<PortPage<F, R, H>>> airportPagesByIdent,
      Map<PortKey, PortPage<F, R, H>> heliportPagesByKey,
      Map<String, List<PortPage<F, R, H>>> heliportPagesByIdent) {
    this.airportPagesByKey = Map.copyOf(requireNonNull(airportPagesByKey));
    this.airportPagesByIdent = Map.copyOf(requireNonNull(airportPagesByIdent));
    this.heliportPagesByKey = Map.copyOf(requireNonNull(heliportPagesByKey));
    this.heliportPagesByIdent = Map.copyOf(requireNonNull(heliportPagesByIdent));
  }

  public static <F, R, H> Builder<F, R, H> builder() {
    return new Builder<>();
  }

  /**
   * Look up an airport page by identifier and ICAO code (exact match).
   */
  public Optional<PortPage<F, R, H>> airportPage(String identifier, String icaoCode) {
    return Optional.ofNullable(airportPagesByKey.get(new PortKey(identifier, icaoCode)));
  }

  /**
   * Look up all airport pages sharing the given identifier (any ICAO code).
   */
  public Collection<PortPage<F, R, H>> airportPages(String identifier) {
    return airportPagesByIdent.getOrDefault(identifier, List.of());
  }

  /**
   * Returns all airport pages in the database.
   */
  public Collection<PortPage<F, R, H>> airportPages() {
    return Collections.unmodifiableCollection(airportPagesByKey.values());
  }

  /**
   * Look up a heliport page by identifier and ICAO code (exact match).
   */
  public Optional<PortPage<F, R, H>> heliportPage(String identifier, String icaoCode) {
    return Optional.ofNullable(heliportPagesByKey.get(new PortKey(identifier, icaoCode)));
  }

  /**
   * Look up all heliport pages sharing the given identifier (any ICAO code).
   */
  public Collection<PortPage<F, R, H>> heliportPages(String identifier) {
    return heliportPagesByIdent.getOrDefault(identifier, List.of());
  }

  /**
   * Returns all heliport pages in the database.
   */
  public Collection<PortPage<F, R, H>> heliportPages() {
    return Collections.unmodifiableCollection(heliportPagesByKey.values());
  }

  record PortKey(String identifier, String icaoCode) {
  }

  public static final class Builder<F, R, H> {

    private final Map<PortKey, PortPage<F, R, H>> airportPagesByKey = new HashMap<>();
    private final Map<String, List<PortPage<F, R, H>>> airportPagesByIdent = new HashMap<>();
    private final Map<PortKey, PortPage<F, R, H>> heliportPagesByKey = new HashMap<>();
    private final Map<String, List<PortPage<F, R, H>>> heliportPagesByIdent = new HashMap<>();

    private Builder() {
    }

    public Builder<F, R, H> withAirportPage(PortPage<F, R, H> page) {
      airportPagesByKey.put(new PortKey(page.identifier(), page.icaoCode()), page);
      airportPagesByIdent.computeIfAbsent(page.identifier(), k -> new ArrayList<>()).add(page);
      return this;
    }

    public Builder<F, R, H> withHeliportPage(PortPage<F, R, H> page) {
      heliportPagesByKey.put(new PortKey(page.identifier(), page.icaoCode()), page);
      heliportPagesByIdent.computeIfAbsent(page.identifier(), k -> new ArrayList<>()).add(page);
      return this;
    }

    public XmlTerminalAreaDatabase<F, R, H> build() {
      return new XmlTerminalAreaDatabase<>(airportPagesByKey, airportPagesByIdent, heliportPagesByKey, heliportPagesByIdent);
    }
  }
}
