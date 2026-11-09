package org.mitre.boogie.cli;

import static java.util.Objects.requireNonNull;

import java.io.FilterInputStream;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Reads one selected record at a time without retaining the complete publication.
 */
final class XmlRecordReader {

  private XmlRecordReader() {
  }

  /**
   * Calls the consumer for each element whose local name matches one of {@code elementNames}, ignoring case. Returning
   * {@code false} stops reading immediately.
   *
   * <p>The consumer also receives the identifier of the nearest enclosing airport or heliport, or an empty string when the
   * record is not nested in one. The caller owns the source stream.
   */
  static void read(InputStream source, Set<String> elementNames, BiPredicate<XmlElement, String> consumer)
      throws XMLStreamException {
    requireNonNull(source, "source");
    requireNonNull(elementNames, "elementNames");
    requireNonNull(consumer, "consumer");
    Set<String> names = elementNames.stream()
        .map(name -> name.toLowerCase(Locale.ROOT))
        .collect(Collectors.toSet());

    XMLInputFactory factory = XMLInputFactory.newFactory();
    factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
    factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
    factory.setXMLResolver((publicId, systemId, baseUri, namespace) -> {
      throw new XMLStreamException("External XML entities are not supported.");
    });

    InputStream nonClosingSource = new FilterInputStream(source) {
      @Override
      public void close() {
        // The caller owns the source, including when a StAX provider closes its input.
      }
    };
    XMLStreamReader reader = factory.createXMLStreamReader(nonClosingSource);
    // Open elements outside any selected record. Ports record their own identifier once it has been read.
    Deque<Ancestor> ancestors = new ArrayDeque<>();
    try {
      while (reader.getEventType() != XMLStreamConstants.END_DOCUMENT) {
        rejectDtd(reader);
        if (reader.isStartElement()) {
          String name = reader.getLocalName();
          Ancestor parent = ancestors.peek();
          if (names.contains(name.toLowerCase(Locale.ROOT))) {
            if (!consumer.test(readElement(reader), port(ancestors))) {
              return;
            }
          } else if (name.equals("identifier") && parent != null && parent.isPort() && parent.identifier == null) {
            // Leaves the reader on the identifier's end event, so the identifier is neither pushed nor popped.
            parent.identifier = reader.getElementText().strip();
          } else {
            ancestors.push(new Ancestor(name));
          }
        } else if (reader.isEndElement()) {
          ancestors.poll();
        }
        reader.next();
      }
    } finally {
      reader.close();
    }
  }

  private static String port(Deque<Ancestor> ancestors) {
    for (Ancestor ancestor : ancestors) {
      if (ancestor.isPort()) {
        return ancestor.identifier == null ? "" : ancestor.identifier;
      }
    }
    return "";
  }

  /**
   * Leaves the reader on the selected element's end event.
   */
  private static XmlElement readElement(XMLStreamReader reader) throws XMLStreamException {
    Deque<ElementBuilder> elements = new ArrayDeque<>();
    elements.push(new ElementBuilder(reader));

    while (reader.hasNext()) {
      reader.next();
      rejectDtd(reader);
      switch (reader.getEventType()) {
        case XMLStreamConstants.START_ELEMENT -> elements.push(new ElementBuilder(reader));
        case XMLStreamConstants.CHARACTERS, XMLStreamConstants.CDATA, XMLStreamConstants.SPACE ->
            elements.peek().text.append(reader.getText());
        case XMLStreamConstants.END_ELEMENT -> {
          XmlElement element = elements.pop().build();
          if (elements.isEmpty()) {
            return element;
          }
          elements.peek().children.add(element);
        }
        default -> {
          // Comments and processing instructions are irrelevant to compact record views.
        }
      }
    }

    throw new XMLStreamException("Input ended before the XML record was complete.", reader.getLocation());
  }

  private static void rejectDtd(XMLStreamReader reader) throws XMLStreamException {
    if (reader.getEventType() == XMLStreamConstants.DTD) {
      throw new XMLStreamException("DTD declarations are not supported.", reader.getLocation());
    }
  }

  private static final class Ancestor {

    private final String name;
    private String identifier;

    private Ancestor(String name) {
      this.name = name;
    }

    private boolean isPort() {
      return name.equals("airport") || name.equals("heliport");
    }
  }

  private static final class ElementBuilder {

    private final String name;
    private final Map<String, String> attributes = new LinkedHashMap<>();
    private final List<XmlElement> children = new ArrayList<>();
    private final StringBuilder text = new StringBuilder();

    private ElementBuilder(XMLStreamReader reader) {
      name = reader.getLocalName();
      for (int i = 0; i < reader.getAttributeCount(); i++) {
        attributes.put(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
      }
    }

    private XmlElement build() {
      return new XmlElement(name, attributes, children, text.toString());
    }
  }
}
