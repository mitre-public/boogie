package org.mitre.boogie.xml.v23_4;

import java.io.InputStream;
import java.util.Optional;
import java.util.function.Function;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;

import org.mitre.boogie.xml.model.ArincRecords;
import org.mitre.boogie.xml.v23_4.generated.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;

/**
 * This class does a series of partial unmarshalling events to build up the records from the xml doc
 * without reading the whole thing into memory.
 */
public final class AeroPublicationStreamer implements Function<InputStream, Optional<ArincRecords>> {

  private static final Logger LOG = LoggerFactory.getLogger(AeroPublicationStreamer.class);
  private final ArincRecords records = new ArincRecords();
  private static final ArincWaypointConverter WAYPOINT_CONVERTER = ArincWaypointConverter.INSTANCE;

  @Override
  public Optional<ArincRecords> apply(InputStream inputStream) {
    try {
      XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
      XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(inputStream);

      JAXBContext context = JAXBContext.newInstance(AeroPublication.class);
      Unmarshaller unmarshaller = context.createUnmarshaller();

      XMLEvent event;
      while ((event = xmlEventReader.peek()) != null) {

        if (event.isStartElement() && event.asStartElement().getName().getLocalPart().equals("waypoint")) {
          Waypoint waypoint = unmarshaller.unmarshal(xmlEventReader, Waypoint.class).getValue();
          WAYPOINT_CONVERTER.apply(waypoint).ifPresent(records::addWaypoint);
        }

        if (event.isStartElement() && event.asStartElement().getName().getLocalPart().equals("airport")) {
          Airport airport = unmarshaller.unmarshal(xmlEventReader, Airport.class).getValue();
          LOG.info("COULD HAVE DONE MORE"); //fixme do things
        }

        if (event.isStartElement() && event.asStartElement().getName().getLocalPart().equals("vhfNavaid")) {
          Navaid vhf = unmarshaller.unmarshal(xmlEventReader, Navaid.class).getValue();
          LOG.info("COULD HAVE DONE MORE"); //fixme do things
        }

        if (event.isStartElement() && event.asStartElement().getName().getLocalPart().equals("ndb")) {
          Ndb ndb = unmarshaller.unmarshal(xmlEventReader, Ndb.class).getValue();
          LOG.info("COULD HAVE DONE MORE"); //fixme do things
        }

        if (event.isStartElement() && event.asStartElement().getName().getLocalPart().equals("airway")) {
          Airway airway = unmarshaller.unmarshal(xmlEventReader, Airway.class).getValue();
          LOG.info("COULD HAVE DONE MORE"); //fixme do things
        }

        if (event.isStartElement() && event.asStartElement().getName().getLocalPart().equals("holdingPattern")) {
          HoldingPattern holdingPattern = unmarshaller.unmarshal(xmlEventReader, HoldingPattern.class).getValue();
          LOG.info("COULD HAVE DONE MORE"); //fixme do thigns
        }

        xmlEventReader.nextEvent();
      }
      return Optional.of(records);
    } catch (Exception e) {
      LOG.error("Could not parse XML");
      return Optional.empty();
    }
  }
}
