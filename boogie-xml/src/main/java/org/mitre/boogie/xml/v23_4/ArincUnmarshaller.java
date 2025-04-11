package org.mitre.boogie.xml.v23_4;

import java.io.InputStream;
import java.util.function.Function;

import org.mitre.boogie.xml.v23_4.generated.AeroPublication;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

/**
 * This class uses JAXB to unmarshall an XML instance document to an AeroPublication generated object.
 */
public final class ArincUnmarshaller implements Function<InputStream, AeroPublication> {
  public static final ArincUnmarshaller INSTANCE = new ArincUnmarshaller();

  private ArincUnmarshaller() {}

  @Override
  public AeroPublication apply(InputStream inputStream) {
    try {
      JAXBContext context = JAXBContext.newInstance(AeroPublication.class);
      Unmarshaller unmarshaller = context.createUnmarshaller();
      return (AeroPublication) unmarshaller.unmarshal(inputStream);
    } catch (JAXBException e) {
      throw new RuntimeException("Could not unmarshall the xml file");
    }
  }
}