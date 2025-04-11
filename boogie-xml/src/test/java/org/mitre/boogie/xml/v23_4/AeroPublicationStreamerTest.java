package org.mitre.boogie.xml.v23_4;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import javax.xml.stream.XMLStreamException;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincRecords;

import jakarta.xml.bind.JAXBException;

class AeroPublicationStreamerTest {
  static File input = new File(System.getProperty("user.dir").concat("/src/test/resources/v23_4/gibberish-sample.xml"));

  @Test
  void test() throws IOException, XMLStreamException, JAXBException {
    AeroPublicationStreamer streamer = new AeroPublicationStreamer();
    ArincRecords data = streamer.apply(Files.newInputStream(input.toPath(), StandardOpenOption.READ)).orElseThrow();
    assertAll(
        () -> assertEquals(5, data.waypoints().size())
    );
  }
}