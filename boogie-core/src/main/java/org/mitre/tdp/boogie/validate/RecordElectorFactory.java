package org.mitre.tdp.boogie.validate;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Function;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Procedure;

/**
 * Factory class for instantiating implementations of {@link UniqueRecordElector}s which can be used to de-duplicate replicate
 * records within an input collection with an injectable strategy for choosing which version of the record to use.
 */
public final class RecordElectorFactory {

  private RecordElectorFactory() {
    throw new IllegalStateException("Unable to instantiate utility factory class.");
  }

  /**
   * Returns a new {@link UniqueRecordElector} which will throw an exception if there are duplicates of any input {@link Airport}
   * records - and will also log the offending records.
   * <br>
   * This is generally more useful for checking expectations on input data than testing something out in production - when used
   * in prod it's more robust to provide your own elector.
   */
  public static UniqueRecordElector<Airport> newUniqueAirportElector() {
    return new UniqueRecordElector<>(RecordElectorFactory::airportUuid);
  }

  /**
   * Returns a new {@link UniqueRecordElector} which will elect the {@link Airport} using the provided elector function when
   * multiple are encountered with the same UUID.
   */
  public static UniqueRecordElector<Airport> newUniqueAirportElector(Function<List<Airport>, Airport> elector) {
    requireNonNull(elector);
    return new UniqueRecordElector<>(RecordElectorFactory::airportUuid, elector);
  }

  /**
   * Similar implementation to {@link #newUniqueAirportElector()} but for {@link Fix}es.
   */
  public static UniqueRecordElector<Fix> newUniqueFixElector() {
    return new UniqueRecordElector<>(RecordElectorFactory::fixUuid);
  }

  /**
   * Similar implementation to {@link #newUniqueAirportElector(Function)} but for {@link Fix}es.
   */
  public static UniqueRecordElector<Fix> newUniqueFixElector(Function<List<Fix>, Fix> elector) {
    requireNonNull(elector);
    return new UniqueRecordElector<>(RecordElectorFactory::fixUuid, elector);
  }

  /**
   * Similar implementation to {@link #newUniqueAirportElector()} but for {@link Procedure}s.
   */
  public static UniqueRecordElector<Procedure> newUniqueProcedureElector() {
    return new UniqueRecordElector<>(RecordElectorFactory::procedureUuid);
  }

  /**
   * Similar implementation to {@link #newUniqueAirportElector(Function)} but for {@link Procedure}s.
   */
  public static UniqueRecordElector<Procedure> newUniqueProcedureElector(Function<List<Procedure>, Procedure> elector) {
    requireNonNull(elector);
    return new UniqueRecordElector<>(RecordElectorFactory::procedureUuid, elector);
  }

  /**
   * Similar implementation to {@link #newUniqueAirportElector()} but for {@link Procedure}s.
   */
  public static UniqueRecordElector<Airway> newUniqueAirwayElector() {
    return new UniqueRecordElector<>(RecordElectorFactory::airwayUuid);
  }

  /**
   * Similar implementation to {@link #newUniqueAirportElector(Function)} but for {@link Procedure}s.
   */
  public static UniqueRecordElector<Airway> newUniqueAirwayElector(Function<List<Airway>, Airway> elector) {
    requireNonNull(elector);
    return new UniqueRecordElector<>(RecordElectorFactory::airwayUuid, elector);
  }

  /**
   * Fix-like records are generally taken to be unique by identifier and region as VHF/NDB navaids and waypoints don't generally
   * share names within the same region.
   */
  private static String fixUuid(Fix fix) {
    requireNonNull(fix.fixIdentifier());
    requireNonNull(fix.fixRegion());
    return fix.fixIdentifier().concat(fix.fixRegion());
  }

  /**
   * Airports are generally taken to be unique based on their identifier (typically an ICAO code) and the region in which they
   * are located.
   * <br>
   * Internationally region is important when considering potential overlap of FAA LOC IDs of non-ICAO airports and international
   * airport LOC IDs (should they be present in the input data).
   */
  private static String airportUuid(Airport airport) {
    requireNonNull(airport.airportIdentifier());
    requireNonNull(airport.airportRegion());
    return airport.airportIdentifier().concat(airport.airportRegion());
  }

  /**
   * Procedure implementations may be replicated in name (and generally in contained waypoints/navaids) between airports - but
   * most implementations want to honor those distinctions as they may have slightly different descriptive characteristics.
   * <br>
   * Most downstream algorithms like the RouteExpander will prefer the declared version of a procedure servicing a particular
   * airport when doing the route expansion.
   */
  private static String procedureUuid(Procedure procedure) {
    requireNonNull(procedure.procedureIdentifier());
    requireNonNull(procedure.airportIdentifier());
    requireNonNull(procedure.airportRegion());
    return procedure.procedureIdentifier().concat(procedure.airportIdentifier()).concat(procedure.airportRegion());
  }

  /**
   * Airways are taken to be unique based on their given identifier as well as their country of origin/ownership. The same airway
   * identifier may be used for distinct airways in different regions - but should never be replicated within the same region.
   */
  private static String airwayUuid(Airway airway) {
    requireNonNull(airway.airwayIdentifier());
    requireNonNull(airway.airwayRegion());
    return airway.airwayIdentifier().concat(airway.airwayRegion());
  }
}
