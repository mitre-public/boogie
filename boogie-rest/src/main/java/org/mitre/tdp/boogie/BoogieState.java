package org.mitre.tdp.boogie;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.alg.RouteExpander;
import org.mitre.tdp.boogie.alg.RouteExpanderFactory;
import org.mitre.tdp.boogie.arinc.ArincFileParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.assemble.AirportAssembler;
import org.mitre.tdp.boogie.arinc.assemble.AirwayAssembler;
import org.mitre.tdp.boogie.arinc.assemble.FixAssembler;
import org.mitre.tdp.boogie.arinc.assemble.ProcedureAssembler;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.database.FixDatabase;
import org.mitre.tdp.boogie.arinc.database.TerminalAreaDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincRecordConverterFactory;
import org.mitre.tdp.boogie.arinc.model.ConvertingArincRecordConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

/**
 * Class representing the current cached state of the Boogie REST endpoint.
 * <p>
 * This singleton class (as managed by Spring) contains the single copy of all the relevant Boogie databases which are generated
 * and updated when a new ARINC-424 file is provided to the application.
 * <p>
 * If this class grows too large we'll need to consider ways to break it up into more modular chunks...
 */
final class BoogieState implements Consumer<Path> {

  private static final Logger LOG = LoggerFactory.getLogger(BoogieState.class);

  /**
   * The {@link ArincVersion} to use to parse the data specify the target model classes to convert the input raw records into.
   */
  private final ArincVersion arincVersion;
  /**
   * The standard file record parser to use to read any new files handed off to the cache and build new structured content.
   */
  private final ArincFileParser fileParser;

  /**
   * Custom query class for formatting request-responses for converted ARINC information.
   */
  private FormattingArincQuerier arincQuerier;

  /**
   * Mapping from {{@link Fix#fixIdentifier()}, {@link Fix}}.
   */
  private Multimap<String, Fix> assembledFixes;

  /**
   * Mapping from {{@link Airport#airportIdentifier()}, {@link Airport}}.
   */
  private Multimap<String, Airport> assembledAirports;

  /**
   * Mapping from {{@link Airway#airwayIdentifier()}, {@link Airway}}.
   */
  private Multimap<String, Airway> assembledAirways;

  /**
   * Mapping from {{@link Procedure#procedureIdentifier()}, {{@link Procedure#airportIdentifier()}, {@link Procedure}}.
   */
  private Multimap<String, Procedure> assembledProcedures;

  /**
   * Shared {@link RouteExpander} instance for general use.
   */
  private RouteExpander routeExpander;

  private BoogieState(ArincVersion version) {
    this.arincVersion = requireNonNull(version);
    this.fileParser = new ArincFileParser(version.parser());
  }

  /**
   * Creates a new {@link BoogieState}
   */
  public static BoogieState forVersion(ArincVersion version) {
    LOG.info("Instantiating new Boogie record cache for version: {}", version);
    return new BoogieState(version);
  }

  public FormattingArincQuerier arincQuerier() {
    return requireNonNull(this.arincQuerier);
  }

  public Collection<Fix> fixes(String... identifiers) {
    return FormattingArincQuerier.streamUnique(identifiers).flatMap(identifier -> assembledFixes.get(identifier).stream()).collect(Collectors.toList());
  }

  public Collection<Airport> airports(String... airports) {
    return FormattingArincQuerier.streamUnique(airports).flatMap(airport -> assembledAirports.get(airport).stream()).collect(Collectors.toList());
  }

  public Collection<Airway> airways(String... airways) {
    return FormattingArincQuerier.streamUnique(airways).flatMap(airway -> assembledAirways.get(airway).stream()).collect(Collectors.toList());
  }

  public Collection<Procedure> procedures(String... procedures) {
    return FormattingArincQuerier.streamUnique(procedures).flatMap(procedure -> assembledProcedures.get(procedure).stream()).collect(Collectors.toList());
  }

  public RouteExpander routeExpander() {
    return requireNonNull(routeExpander);
  }

  /**
   * This relatively long (re-)initialization block is a one-shot from the raw ARINC 424 file to both the parsed records in their
   * close-to-424 format as well as thinner assembled versions of them more useful in tools like the {@link RouteExpander}.
   */
  @Override
  public void accept(Path path) {
    requireNonNull(path, "Input path cannot be null.");
    checkArgument(path.toFile().isFile(), "Input path must be a file.");

    LOG.info("Accepted input path to 424 data '{}' - rebuilding local cache.", path);

    ConvertingArincRecordConsumer consumer = ArincRecordConverterFactory.consumerForVersion(arincVersion);
    fileParser.apply(path.toFile()).forEach(consumer);

    LOG.info("Finished parsing and converting records.");

    FixDatabase fixDatabase = ArincDatabaseFactory.newFixDatabase(
        consumer.arincNdbNavaids(),
        consumer.arincVhfNavaids(),
        consumer.arincWaypoints(),
        consumer.arincAirports()
    );

    LOG.info("Finished instantiation if FixDatabase.");

    TerminalAreaDatabase terminalAreaDatabase = ArincDatabaseFactory.newTerminalAreaDatabase(
        consumer.arincAirports(),
        consumer.arincRunways(),
        consumer.arincLocalizerGlideSlopes(),
        consumer.arincNdbNavaids(),
        consumer.arincVhfNavaids(),
        consumer.arincWaypoints(),
        consumer.arincProcedureLegs()
    );

    LOG.info("Finished instantiation of TerminalAreaDatabase.");
    this.arincQuerier = FormattingArincQuerier.with(fixDatabase, terminalAreaDatabase);

    Stream<Fix> fixStream = Stream.concat(
            consumer.arincWaypoints().stream(),
            Stream.concat(
                consumer.arincNdbNavaids().stream(),
                consumer.arincVhfNavaids().stream()
            ))
        .map(FixAssembler.INSTANCE);
    this.assembledFixes = toMultimap(Fix::fixIdentifier, fixStream);

    LOG.info("Finished assembling {} total fixes.", this.assembledFixes.size());

    AirportAssembler airportAssembler = new AirportAssembler(terminalAreaDatabase);

    Stream<Airport> airportStream = consumer.arincAirports().stream().map(airportAssembler);
    this.assembledAirports = toMultimap(Airport::airportIdentifier, airportStream);

    LOG.info("Finished assembling {} total airports.", this.assembledAirports.size());

    Stream<Airway> airwayStream = new AirwayAssembler(fixDatabase).apply(consumer.arincAirwayLegs());
    this.assembledAirways = toMultimap(Airway::airwayIdentifier, airwayStream);

    LOG.info("Finished assembling {} total airways.", this.assembledAirways.size());

    Stream<Procedure> procedureStream = new ProcedureAssembler(terminalAreaDatabase, fixDatabase).apply(consumer.arincProcedureLegs());
    this.assembledProcedures = toMultimap(Procedure::procedureIdentifier, procedureStream);

    LOG.info("Finished assembling {} total procedures.", this.assembledProcedures.size());

    this.routeExpander = RouteExpanderFactory.newGraphicalRouteExpander(
        this.assembledFixes.values(),
        this.assembledAirways.values(),
        this.assembledAirports.values(),
        this.assembledProcedures.values()
    );

    LOG.info("Finished construction of RouteExpander.");
    LOG.info("Completed processing and re-indexing of internal cache with data from the provided file: {}", path);
  }

  private <K, V> Multimap<K, V> toMultimap(Function<V, K> keyFn, Stream<V> stream) {
    Multimap<K, V> multimap = LinkedHashMultimap.create();
    stream.forEach(value -> multimap.put(keyFn.apply(value), value));
    return multimap;
  }
}
