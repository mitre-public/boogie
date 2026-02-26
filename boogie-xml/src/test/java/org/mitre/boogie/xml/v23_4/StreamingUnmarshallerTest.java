package org.mitre.boogie.xml.v23_4;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincVhfNavaid;
import org.mitre.boogie.xml.model.ArincRecords;
import org.mitre.boogie.xml.v23_4.generated.AeroPublication;

class StreamingUnmarshallerTest {
  static File input = new File(System.getProperty("user.dir").concat("/src/test/resources/v23_4/gibberish-sample.xml"));

  @Test
  void test() throws IOException {
    StreamingUnmarshaller streamer = new StreamingUnmarshaller(List.of(AeroPublication.class));
    ArincRecords data = streamer.apply(Files.newInputStream(input.toPath(), StandardOpenOption.READ)).orElseThrow();
    Set<String> vhfSubTypes = data.vhfNavaids().stream()
        .map(ArincVhfNavaid::navaidSubType)
        .flatMap(Optional::stream)
        .collect(Collectors.toSet());
    assertAll(
        () -> assertEquals(5, data.waypoints().size()),
        () -> assertEquals(5, data.airports().size()),
        () -> assertEquals(5, data.ndbNavaids().size()),
        () -> assertEquals(3, data.vhfNavaids().size()),
        () -> assertEquals(Set.of("DME", "TACAN", "MILITARY_TACAN"), vhfSubTypes),
        () -> assertEquals(5, data.arincAirways().size()),
        () -> assertEquals(5, data.holdingPatterns().size())
    );
  }
}