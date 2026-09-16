package org.mitre.boogie.xml.exi;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HexFormat;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Streaming assertions for EXI artifacts, without retaining whole documents in memory. */
public final class ExiAssertions {

  private ExiAssertions() {}

  /** The cookie is optional in EXI generally, but {@link ExiCodec} always writes it. */
  public static void assertCookie(Path exi) throws Exception {
    try (InputStream input = Files.newInputStream(exi)) {
      assertArrayEquals("$EXI".getBytes(StandardCharsets.US_ASCII), input.readNBytes(4),
          exi + ": EXI cookie");
    }
  }

  /**
   * Decode the complete artifact and compare ordered element names, attribute names and counts.
   * Names use namespace URIs rather than prefixes; attribute order is insignificant. Text and
   * attribute values are deliberately excluded because schema-informed EXI can normalize typed
   * values. Value preservation must be checked separately, for example by comparing parsed models.
   * The caller owns {@code xml}.
   */
  public static void assertSameStructure(InputStream xml, Path exi, ExiCodec decoder) throws Exception {
    XMLInputFactory factory = XMLInputFactory.newDefaultFactory();
    factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
    factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
    Structure expected = readStructure(factory.createXMLStreamReader(xml), exi + " source XML");
    Structure actual;
    try (InputStream input = new BufferedInputStream(Files.newInputStream(exi))) {
      actual = readStructure(decoder.createReader(input), exi.toString());
    }
    assertAll(exi + ": decoded document structure",
        () -> assertEquals(expected.root(), actual.root(), "Root element"),
        () -> assertEquals(expected.elements(), actual.elements(), "Element count"),
        () -> assertEquals(expected.attributes(), actual.attributes(), "Attribute count"),
        () -> assertEquals(expected.digest(), actual.digest(), "Ordered element and attribute names"));
  }

  private static Structure readStructure(XMLStreamReader reader, String source) throws Exception {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    Deque<QName> elements = new ArrayDeque<>();
    QName root = null;
    long elementCount = 0;
    long attributeCount = 0;
    int roots = 0;
    boolean endDocument = false;
    String unexpectedClose = source + ": unexpected closing element";
    String matchingClose = source + ": matching closing element";
    try {
      assertEquals(XMLStreamConstants.START_DOCUMENT, reader.getEventType(), source + ": document start");
      do {
        switch (reader.getEventType()) {
          case XMLStreamConstants.START_ELEMENT -> {
            QName name = reader.getName();
            if (elements.isEmpty()) {
              roots++;
              root = name;
            }
            elements.push(name);
            elementCount++;
            token(digest, "START");
            token(digest, name.toString());
            List<String> attributes = new ArrayList<>(reader.getAttributeCount());
            for (int i = 0; i < reader.getAttributeCount(); i++) {
              attributes.add(reader.getAttributeName(i).toString());
            }
            attributes.sort(String::compareTo);
            attributeCount += attributes.size();
            token(digest, Integer.toString(attributes.size()));
            attributes.forEach(attribute -> token(digest, attribute));
          }
          case XMLStreamConstants.END_ELEMENT -> {
            assertFalse(elements.isEmpty(), unexpectedClose);
            assertEquals(elements.pop(), reader.getName(), matchingClose);
            token(digest, "END");
            token(digest, reader.getName().toString());
          }
          case XMLStreamConstants.END_DOCUMENT -> endDocument = true;
          default -> { }
        }
        if (!reader.hasNext()) {
          break;
        }
        reader.next();
      } while (true);
      assertTrue(endDocument, source + ": decoder must reach END_DOCUMENT");
      assertTrue(elements.isEmpty(), source + ": all elements must close");
      assertEquals(1, roots, source + ": exactly one document root");
      return new Structure(root, elementCount, attributeCount, HexFormat.of().formatHex(digest.digest()));
    } finally {
      reader.close();
    }
  }

  private static void token(MessageDigest digest, String value) {
    byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
    // Length prefixes keep consecutive names and event markers unambiguous.
    digest.update((byte) (bytes.length >>> 24));
    digest.update((byte) (bytes.length >>> 16));
    digest.update((byte) (bytes.length >>> 8));
    digest.update((byte) bytes.length);
    digest.update(bytes);
  }

  private record Structure(QName root, long elements, long attributes, String digest) {}
}
