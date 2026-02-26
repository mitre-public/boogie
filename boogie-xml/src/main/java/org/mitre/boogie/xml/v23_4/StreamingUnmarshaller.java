package org.mitre.boogie.xml.v23_4;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;

import org.mitre.boogie.xml.model.*;
import org.mitre.boogie.xml.v23_4.convert.ArincAirportConverter;
import org.mitre.boogie.xml.v23_4.convert.ArincAirwayConverter;
import org.mitre.boogie.xml.v23_4.convert.ArincHoldingPatternConverter;
import org.mitre.boogie.xml.v23_4.convert.ArincNdbNavaidConverter;
import org.mitre.boogie.xml.v23_4.convert.ArincVhfNavaidConverter;
import org.mitre.boogie.xml.v23_4.convert.ArincWaypointConverter;
import org.mitre.boogie.xml.v23_4.generated.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;

/**
 * This class does a series of partial unmarshalling events to build up the records from the xml doc
 * without reading the whole thing into memory.
 */
public final class StreamingUnmarshaller implements Function<InputStream, Optional<ArincRecords>> {
  private static final Logger LOG = LoggerFactory.getLogger(StreamingUnmarshaller.class);

  private static final ArincWaypointConverter WAYPOINT_CONVERTER = ArincWaypointConverter.INSTANCE;
  private static final ArincAirportConverter AIRPORT_CONVERTER = ArincAirportConverter.INSTANCE;
  private static final ArincAirwayConverter AIRWAY_CONVERTER = ArincAirwayConverter.INSTANCE;
  private static final ArincHoldingPatternConverter HOLDING_PATTERN_CONVERTER = ArincHoldingPatternConverter.INSTANCE;
  private static final ArincNdbNavaidConverter NDB_CONVERTER = ArincNdbNavaidConverter.INSTANCE;
  private static final ArincVhfNavaidConverter VHF_CONVERTER = ArincVhfNavaidConverter.INSTANCE;

  private final List<Class<?>> supportArincXmlClasses;

  public StreamingUnmarshaller(List<Class<?>> supportArincXmlClasses) {
    this.supportArincXmlClasses = supportArincXmlClasses;
  }

  @Override
  public Optional<ArincRecords> apply(InputStream inputStream) {
    ArincRecords records = ArincRecords.standard();
    try {
      XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
      XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(inputStream);

      JAXBContext context = JAXBContext.newInstance(supportArincXmlClasses.toArray(new Class[0]));
      Unmarshaller unmarshaller = context.createUnmarshaller();
      unmarshaller.setProperty(org.glassfish.jaxb.runtime.IDResolver.class.getName(), StringIdResolver.INSTANCE);

      try {
        XMLEvent event;
        while ((event = xmlEventReader.peek()) != null) {

          if (event.isStartElement() && event.asStartElement().getName().getLocalPart().equals("waypoint")) {
            Waypoint waypoint = unmarshaller.unmarshal(xmlEventReader, Waypoint.class).getValue();
            WAYPOINT_CONVERTER.apply(waypoint).ifPresent(records::addWaypoint);
          }

          if (event.isStartElement() && event.asStartElement().getName().getLocalPart().equals("airport")) {
            Airport airport = unmarshaller.unmarshal(xmlEventReader, Airport.class).getValue();
            AIRPORT_CONVERTER.apply(airport).ifPresent(records::addAirport);
          }

          if (event.isStartElement() && event.asStartElement().getName().getLocalPart().equals("ndb")) {
            Ndb ndb = unmarshaller.unmarshal(xmlEventReader, Ndb.class).getValue();
            NDB_CONVERTER.apply(ndb).ifPresent(records::addNdbNavaid);
          }

          if (event.isStartElement() && event.asStartElement().getName().getLocalPart().equals("vhfNavaid")) {
            Navaid navaid = unmarshaller.unmarshal(xmlEventReader, Navaid.class).getValue();
            VHF_CONVERTER.apply(navaid).ifPresent(records::addVhfNavaid);
          }

          if (event.isStartElement() && event.asStartElement().getName().getLocalPart().equals("airway")) {
            Airway airway = unmarshaller.unmarshal(xmlEventReader, Airway.class).getValue();
            AIRWAY_CONVERTER.apply(airway).ifPresent(records::addAirway);
          }

          if (event.isStartElement() && event.asStartElement().getName().getLocalPart().equals("holdingPattern")) {
            HoldingPattern holdingPattern = unmarshaller.unmarshal(xmlEventReader, HoldingPattern.class).getValue();
            HOLDING_PATTERN_CONVERTER.apply(holdingPattern).ifPresent(records::addHoldingPattern);
          }

          xmlEventReader.nextEvent();
        }
      } catch (Exception e) {
        LOG.info("could not parse an xml element: ", e);
      }
      return Optional.of(records);
    } catch (Exception e) {
      LOG.error("Could not parse XML file: ", e);
      return Optional.empty();
    }
  }
}
