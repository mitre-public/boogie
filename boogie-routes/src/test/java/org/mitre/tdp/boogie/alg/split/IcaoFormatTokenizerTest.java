package org.mitre.tdp.boogie.alg.split;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.Test;


class IcaoFormatTokenizerTest {

  private static final RouteTokenizer TOKENIZER = RouteTokenizer.icaoFormat();

  private static final Function<String, List<RouteToken.Icao>> SPLITTER = s -> TOKENIZER.tokenize(s)
      .stream().map(t -> (RouteToken.Icao) t).collect(toList());

  static final String plan1 = "DCT GREKI IFR DCT JUDDS DCT EBONY/M085F350 N381B ALLRY/M085F370 DCT 51N050W 53N040W 54N030W 54N020W DCT VENER/M085F370 DCT KOKIB DCT LUTOV/N0491F370 DCT GOW L983 CUTEL T55 TINAC DCT GELDA/N0479F390 DCT SORES M864 IGORO/K0889F390 M864 TU L169 IN M166 FE T568 OLUPI T573 PENIR T531 URL A108 GERLI T531 ODIVA A66 TMD A466 TOLIB/K0917F410 A466 AMDAR/N0495F410 M875 SITAX/N0494F410 A466 SULOM M890 LKN R460 CEA/M085F410 L507 BGO/N0485F410 M626 VKB M751 VPK B469 BIKTA PIBAP PAS3 MABA2A";
  static final String plan2 = "/ HTO354018 CCC MANTA J121 BRIGS JIIMS2";
  static final String plan3 = "WOOST/N0501F360 WXI/K0946S0890";
  static final String plan4 = "-N0200A050 RECHI PONJO WSD4 PONJO RECHI N0200A050";
  static final String plan5 = "C/48N050W/M082F290F350 C/RECHI/M082F290PLUS";
  static final String plan6 = "VOBL BLI DCT VOBL MMW";

  @Test
  void directTo() {
    List<RouteToken.Icao> splits = SPLITTER.apply(plan6);
    assertEquals(" ", splits.get(2).wildcards().orElse(""));
  }

  @Test
  void cruiseClimb() {
    List<RouteToken.Icao> splits = SPLITTER.apply(plan5);
    assertAll(
        () -> assertEquals("48N050W", splits.get(0).infrastructureName()),
        () -> assertEquals("RECHI", splits.get(1).infrastructureName()),
        () -> assertTrue(splits.stream().allMatch(i -> i.wildcards().get().equals("C/"))),
        () -> assertEquals("M082F290F350", splits.get(0).speedLevel().get()),
        () -> assertEquals("M082F290PLUS", splits.get(1).speedLevel().get())
    );
  }

  @Test
  void speedAltInFront() {
    List<RouteToken.Icao> splits = SPLITTER.apply(plan4);
    assertTrue(splits.stream().noneMatch(i -> i.infrastructureName().contains("N0200A050")));
  }

  @Test
  void apply() {
    List<RouteToken.Icao> splits = SPLITTER.apply(plan1);
    assertAll(
        () -> assertEquals(62, splits.size(), "Count them its 62"),
        () -> assertEquals("IFR", splits.get(0).flightRules().orElse(null), "Should see IFR added to the first fix"),
        () -> assertEquals("GREKI", splits.get(0).infrastructureName(), "Should see DCT dropped up front"),
        () -> assertFalse(splits.stream().anyMatch(i -> i.infrastructureName().equals("DCT")), "None of these should be there"),
        () -> assertEquals("M085F350", splits.get(2).speedLevel().orElse(null), "The speed/alt should be added to EBONY"),
        () -> assertEquals("EBONY", splits.get(2).infrastructureName(), "All the IFR/DCT stuff should be dropped"),
        () -> assertTrue(splits.stream().noneMatch(i -> i.infrastructureName().contains("/")))
    );
  }

  @Test
  void apply2() {
    List<RouteToken.Icao> splits = SPLITTER.apply(plan2);

    assertAll(
        () -> assertEquals("HTO354018", splits.get(0).infrastructureName()),
        () -> assertEquals("/", splits.get(0).wildcards().orElse(null)),
        () -> assertTrue(splits.stream().noneMatch(i -> i.infrastructureName().contains("/")))
    );
  }

  @Test
  void apply3() {
    List<RouteToken.Icao> splits = SPLITTER.apply(plan3);

    assertAll(
        () -> assertEquals("WOOST", splits.get(0).infrastructureName()),
        () -> assertEquals("WXI", splits.get(1).infrastructureName()),
        () -> assertTrue(splits.stream().noneMatch(i -> i.infrastructureName().contains("/")))
    );
  }
}
