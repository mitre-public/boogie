package org.mitre.boogie.xml.v23_4;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.boogie.xml.model.ArincAirport;
import org.mitre.boogie.xml.model.ArincAirway;
import org.mitre.boogie.xml.model.ArincHoldingPattern;
import org.mitre.boogie.xml.model.ArincNdbNavaid;
import org.mitre.boogie.xml.model.ArincRecords;
import org.mitre.boogie.xml.model.ArincVhfNavaid;
import org.mitre.boogie.xml.model.ArincWaypoint;
import org.mitre.boogie.xml.v23_4.convert.ArincAirportConverter;
import org.mitre.boogie.xml.v23_4.convert.ArincAirwayConverter;
import org.mitre.boogie.xml.v23_4.convert.ArincHoldingPatternConverter;
import org.mitre.boogie.xml.v23_4.convert.ArincNdbNavaidConverter;
import org.mitre.boogie.xml.v23_4.convert.ArincVhfNavaidConverter;
import org.mitre.boogie.xml.v23_4.convert.ArincWaypointConverter;
import org.mitre.boogie.xml.v23_4.generated.AeroPublication;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

/**
 * This class uses JAXB to unmarshall an XML instance document to an AeroPublication generated object.
 */
public final class Unmarshaller implements Function<InputStream, Optional<ArincRecords>> {
  private static final ArincWaypointConverter WAYPOINT_CONVERTER = ArincWaypointConverter.INSTANCE;
  private static final ArincAirportConverter AIRPORT_CONVERTER = ArincAirportConverter.INSTANCE;
  private static final ArincAirwayConverter AIRWAY_CONVERTER = ArincAirwayConverter.INSTANCE;
  private static final ArincHoldingPatternConverter HOLDING_PATTERN_CONVERTER = ArincHoldingPatternConverter.INSTANCE;
  private static final ArincNdbNavaidConverter NDB_CONVERTER = ArincNdbNavaidConverter.INSTANCE;
  private static final ArincVhfNavaidConverter VHF_CONVERTER = ArincVhfNavaidConverter.INSTANCE;

  private final List<Class<?>> supportArincXmlClasses;

  public Unmarshaller(List<Class<?>> supportArincXmlClasses) {
    this.supportArincXmlClasses = supportArincXmlClasses;
  }

  @Override
  public Optional<ArincRecords> apply(InputStream inputStream) {
    try {
      JAXBContext context = JAXBContext.newInstance(supportArincXmlClasses.toArray(new Class[0]));
      jakarta.xml.bind.Unmarshaller unmarshaller = context.createUnmarshaller();
      unmarshaller.setProperty(org.glassfish.jaxb.runtime.IDResolver.class.getName(), StringIdResolver.INSTANCE);
      AeroPublication pubs = (AeroPublication) unmarshaller.unmarshal(inputStream);
      Set<ArincWaypoint> enrts = pubs.getEnrouteWaypoints().getWaypoint().stream()
          .map(WAYPOINT_CONVERTER)
          .flatMap(Optional::stream)
          .collect(Collectors.toSet());
      Set<ArincAirport> arpts = pubs.getAirports().getAirport().stream()
          .map(AIRPORT_CONVERTER)
          .flatMap(Optional::stream)
          .collect(Collectors.toSet());
      Set<ArincNdbNavaid> ndbs = pubs.getEnrouteNdbs().getNdb().stream()
          .map(NDB_CONVERTER)
          .flatMap(Optional::stream)
          .collect(Collectors.toSet());
      Set<ArincVhfNavaid> vhfs = pubs.getVhfNavaids().getVhfNavaid().stream()
          .map(VHF_CONVERTER)
          .flatMap(Optional::stream)
          .collect(Collectors.toSet());
      Set<ArincAirway> airways = pubs.getAirways().getAirway().stream()
          .map(AIRWAY_CONVERTER)
          .flatMap(Optional::stream)
          .collect(Collectors.toSet());
      Set<ArincHoldingPattern> holdingPatterns = pubs.getHoldingPatterns().getHoldingPattern().stream()
          .map(HOLDING_PATTERN_CONVERTER)
          .flatMap(Optional::stream)
          .collect(Collectors.toSet());
      ArincRecords records = ArincRecords.standard()
          .waypoints(enrts)
          .airports(arpts)
          .ndbNavaids(ndbs)
          .vhfNavaids(vhfs)
          .arincAirways(airways)
          .holdingPatterns(holdingPatterns);
      return Optional.of(records);
    } catch (JAXBException e) {
      throw new RuntimeException("Could not unmarshall the xml file");
    }
  }
}