package org.mitre.boogie.xml.v23_4;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincRecords;
import org.mitre.boogie.xml.v23_4.generated.AeroPublication;

class UnmarshallerTest {
  static File input = new File(System.getProperty("user.dir").concat("/src/test/resources/v23_4/gibberish-sample.xml"));

  @Test
  void test() throws IOException {
    Unmarshaller unmarshaller = new Unmarshaller(List.of(AeroPublication.class));
    ArincRecords data = unmarshaller.apply(new FileInputStream(input)).get();
    assertAll(
        () -> assertEquals(5, data.waypoints().size())
    );
  }
}