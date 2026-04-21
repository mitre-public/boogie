package org.mitre.boogie.xml;

import static java.util.Optional.ofNullable;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.ArincRecords;
import org.mitre.boogie.xml.v23_4.convert.ArincAirportConverter;
import org.mitre.boogie.xml.v23_4.convert.ArincAirwayConverter;
import org.mitre.boogie.xml.v23_4.convert.ArincHeliportConverter;
import org.mitre.boogie.xml.v23_4.convert.ArincHoldingPatternConverter;
import org.mitre.boogie.xml.v23_4.convert.ArincNdbNavaidConverter;
import org.mitre.boogie.xml.v23_4.convert.ArincVhfNavaidConverter;
import org.mitre.boogie.xml.v23_4.convert.ArincWaypointConverter;
import org.mitre.boogie.xml.v23_4.generated.AeroPublication;
import org.mitre.boogie.xml.v23_4.generated.Airway;
import org.mitre.boogie.xml.v23_4.generated.Airport;
import org.mitre.boogie.xml.v23_4.generated.Heliport;
import org.mitre.boogie.xml.v23_4.generated.HoldingPattern;
import org.mitre.boogie.xml.v23_4.generated.Navaid;
import org.mitre.boogie.xml.v23_4.generated.Ndb;
import org.mitre.boogie.xml.v23_4.generated.Waypoint;

/**
 * Pre-configured set of unmarshallers for various well-known ARINC 424 XML schema versions.
 *
 * <p>Each version defines the JAXB context classes required to parse the XML schema and a set of
 * {@link XmlRecordHandler}s that bind XML element names to their version-specific validators and converters.
 *
 * <p>This class explicitly <i>does not</i> attempt to cover every possible version of ARINC 424 XML data, but it is
 * relatively simple to add new versions as needed.
 *
 * <p>This mirrors the {@code ArincVersion} enum from the ARINC fixed-width module and the {@code DafifVersion} enum
 * from the DAFIF module.
 */
public enum ArincXmlVersion {

  /**
   * ARINC 424 XML schema version 23.4.
   */
  V23_4(
      List.of(AeroPublication.class),
      List.of(
          XmlRecordHandler.of("waypoint", Waypoint.class, ArincWaypointConverter.INSTANCE, ArincRecords::addWaypoint),
          XmlRecordHandler.of("airport", Airport.class, ArincAirportConverter.INSTANCE, ArincRecords::addAirport),
          XmlRecordHandler.of("ndb", Ndb.class, ArincNdbNavaidConverter.INSTANCE, ArincRecords::addNdbNavaid),
          XmlRecordHandler.of("vhfNavaid", Navaid.class, ArincVhfNavaidConverter.INSTANCE, ArincRecords::addVhfNavaid),
          XmlRecordHandler.of("airway", Airway.class, ArincAirwayConverter.INSTANCE, ArincRecords::addAirway),
          XmlRecordHandler.of("holdingPattern", HoldingPattern.class, ArincHoldingPatternConverter.INSTANCE, ArincRecords::addHoldingPattern),
          XmlRecordHandler.of("heliport", Heliport.class, ArincHeliportConverter.INSTANCE, ArincRecords::addHeliport)
      )
  );

  private static final Map<String, ArincXmlVersion> LOOKUP = Map.ofEntries(
      Map.entry(V23_4.name(), V23_4)
  );

  private final List<Class<?>> jaxbContextClasses;
  private final List<XmlRecordHandler<?>> handlers;

  ArincXmlVersion(List<Class<?>> jaxbContextClasses, List<XmlRecordHandler<?>> handlers) {
    this.jaxbContextClasses = jaxbContextClasses;
    this.handlers = handlers;
  }

  /**
   * Implements a "safe" {@link ArincXmlVersion} parse step which returns {@link Optional#empty()} if the string
   * doesn't match one of the known versions.
   */
  public static Optional<ArincXmlVersion> parse(String version) {
    return ofNullable(version).map(String::toUpperCase).map(LOOKUP::get);
  }

  /**
   * Return the JAXB context classes required to unmarshal this version's XML schema.
   */
  public List<Class<?>> jaxbContextClasses() {
    return jaxbContextClasses;
  }

  /**
   * Return the collection of {@link XmlRecordHandler}s that define the supported record types and their
   * version-specific validators and converters.
   */
  public List<XmlRecordHandler<?>> handlers() {
    return handlers;
  }

  /**
   * Return the unmarshaller function for this XML version which converts an {@link InputStream} to
   * {@link ArincRecords}.
   */
  public Function<InputStream, ArincRecords> unmarshaller() {
    return is -> new StreamingUnmarshaller(jaxbContextClasses, handlers).apply(is).orElseThrow(
        () -> new RuntimeException("Failed to unmarshal XML input."));
  }
}
