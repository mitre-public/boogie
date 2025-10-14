package org.mitre.boogie.xml.v23_4;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.boogie.xml.model.ArincRecords;
import org.mitre.boogie.xml.model.ArincWaypoint;
import org.mitre.boogie.xml.v23_4.convert.ArincWaypointConverter;
import org.mitre.boogie.xml.v23_4.generated.AeroPublication;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

/**
 * This class uses JAXB to unmarshall an XML instance document to an AeroPublication generated object.
 */
public final class ArincUnmarshaller implements Function<InputStream, Optional<ArincRecords>> {
  private static final ArincWaypointConverter WAYPOINT_CONVERTER = ArincWaypointConverter.INSTANCE;

  private final List<Class<?>> supportArincXmlClasses;

  public ArincUnmarshaller(List<Class<?>> supportArincXmlClasses) {
    this.supportArincXmlClasses = supportArincXmlClasses;
  }

  @Override
  public Optional<ArincRecords> apply(InputStream inputStream) {
    try {
      JAXBContext context = JAXBContext.newInstance(supportArincXmlClasses.toArray(new Class[0]));
      Unmarshaller unmarshaller = context.createUnmarshaller();
      AeroPublication pubs = (AeroPublication) unmarshaller.unmarshal(inputStream);
      Set<ArincWaypoint> enrts = pubs.getEnrouteWaypoints().getWaypoint().stream()
          .map(WAYPOINT_CONVERTER)
          .flatMap(Optional::stream)
          .collect(Collectors.toSet());
      ArincRecords records = ArincRecords.standard()
          .waypoints(enrts);
      return Optional.of(records);
    } catch (JAXBException e) {
      throw new RuntimeException("Could not unmarshall the xml file");
    }
  }
}