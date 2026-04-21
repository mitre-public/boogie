package org.mitre.boogie.xml;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.mitre.boogie.xml.model.ArincRecords;

/**
 * Binds an XML element name to its JAXB class, converter function, and the method used to add the
 * converted result to {@link ArincRecords}.
 *
 * <p>Instances of this class are used by {@link StreamingUnmarshaller} to define how each supported
 * XML element type is unmarshalled, validated, converted, and collected.
 *
 * @param <T> the JAXB-generated type for the XML element
 */
public final class XmlRecordHandler<T> {

  private final String elementName;
  private final Class<T> jaxbClass;
  private final BiConsumer<T, ArincRecords> action;

  private XmlRecordHandler(String elementName, Class<T> jaxbClass, BiConsumer<T, ArincRecords> action) {
    this.elementName = requireNonNull(elementName);
    this.jaxbClass = requireNonNull(jaxbClass);
    this.action = requireNonNull(action);
  }

  /**
   * Create a new handler binding an XML element name to a JAXB type, converter, and record adder.
   *
   * @param elementName the local name of the XML element to match (e.g. "waypoint", "airport")
   * @param jaxbClass   the JAXB class to unmarshal the element into
   * @param converter   function to validate and convert the JAXB object to a model object
   * @param adder       method reference to add the converted record to {@link ArincRecords}
   * @param <T>         the JAXB-generated type
   * @param <R>         the Boogie model type produced by the converter
   */
  public static <T, R> XmlRecordHandler<T> of(
      String elementName,
      Class<T> jaxbClass,
      Function<T, Optional<R>> converter,
      BiConsumer<ArincRecords, R> adder) {
    return new XmlRecordHandler<>(elementName, jaxbClass, (value, records) ->
        converter.apply(value).ifPresent(r -> adder.accept(records, r)));
  }

  public String elementName() {
    return elementName;
  }

  public Class<T> jaxbClass() {
    return jaxbClass;
  }

  void accept(T value, ArincRecords records) {
    action.accept(value, records);
  }
}
