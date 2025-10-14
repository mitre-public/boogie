package org.mitre.boogie.xml.v23_4;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincRecords;
import org.mitre.boogie.xml.v23_4.generated.AeroPublication;

import jakarta.xml.bind.JAXBException;

class AeroPublicationStreamerTest {
  static File input = new File(System.getProperty("user.dir").concat("/src/test/resources/v23_4/gibberish-sample.xml"));

  @Test
  void test() throws IOException {
    AeroPublicationStreamer streamer = new AeroPublicationStreamer(List.of(AeroPublication.class));
    ArincRecords data = streamer.apply(Files.newInputStream(input.toPath(), StandardOpenOption.READ)).orElseThrow();
    assertAll(
        () -> assertEquals(5, data.waypoints().size()),
        () -> assertEquals(5, data.airports().size()),
        () -> assertEquals(5, data.ndbNavaids().size()),
        () -> assertEquals(5, data.vhfNavaids().size()),
        () -> assertEquals(5, data.arincAirways().size()),
        () -> assertEquals(5, data.holdingPatterns().size())
    );
  }
}