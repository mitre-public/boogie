package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.arinc.ArincFileParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.model.ArincRecordConverterFactory;
import org.mitre.tdp.boogie.arinc.model.ConvertingArincRecordConsumer;
import org.mitre.tdp.boogie.arinc.v18.FirUirLegSpec;

public class TestV18FirUirAssembler {
  private static final File arincTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/arinc-fir-v18.txt"));
  private static final ArincFileParser PARSER = new ArincFileParser(new FirUirLegSpec());
  private static final ConvertingArincRecordConsumer V18_CONSUMER = ArincRecordConverterFactory.consumerForVersion(ArincVersion.V18);
  private static final FirUirAssembler<Airspace> assembler = FirUirAssembler.standard();

  @BeforeAll
  static void setup() {
    PARSER.apply(arincTestFile).forEach(V18_CONSUMER);
  }

  @Test
  void test() {
    Map<String, Airspace> airspaces = assembler.assemble(V18_CONSUMER.arincFirUirLegs()).collect(Collectors.toMap(i -> i.identifier().concat(i.airspaceType().name()), Function.identity()));
    assertAll(
        () -> assertEquals(3, airspaces.size(), "Split the FIR/UIR and then the next one"),
        () -> assertEquals(754, airspaces.get("KZAB-ZRZXFIR").sequences().size()),
        () -> assertEquals(754, airspaces.get("KZAB-ZRZXUIR").sequences().size()),
        () -> assertEquals(3, airspaces.get("KZAK-ZOZXFIR").sequences().size())
    );
  }
}
