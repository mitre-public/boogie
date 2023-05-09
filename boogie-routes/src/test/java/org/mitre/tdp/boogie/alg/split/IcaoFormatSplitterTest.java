package org.mitre.tdp.boogie.alg.split;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class IcaoFormatSplitterTest {

  private static final SectionSplitter SPLITTER = SectionSplitter.icaoFormat();

  static final String plan1 = "DCT GREKI IFR DCT JUDDS DCT EBONY/M085F350 N381B ALLRY/M085F370 DCT 51N050W 53N040W 54N030W 54N020W DCT VENER/M085F370 DCT KOKIB DCT LUTOV/N0491F370 DCT GOW L983 CUTEL T55 TINAC DCT GELDA/N0479F390 DCT SORES M864 IGORO/K0889F390 M864 TU L169 IN M166 FE T568 OLUPI T573 PENIR T531 URL A108 GERLI T531 ODIVA A66 TMD A466 TOLIB/K0917F410 A466 AMDAR/N0495F410 M875 SITAX/N0494F410 A466 SULOM M890 LKN R460 CEA/M085F410 L507 BGO/N0485F410 M626 VKB M751 VPK B469 BIKTA PIBAP PAS3 MABA2A";
  static final String plan2 = "/ HTO354018 CCC MANTA J121 BRIGS JIIMS2";
  static final String plan3 = "WOOST/N0501F360 WXI/K0946S0890";

  @Test
  void apply() {
    List<SectionSplit> splits = SPLITTER.splits(plan1);
    assertAll(
        () -> assertEquals(62, splits.size(), "Count them its 62"),
        () -> assertEquals("IFR", splits.get(0).flightRules(), "Should see IFR added to the first fix"),
        () -> assertEquals("GREKI", splits.get(0).value(), "Should see DCT dropped up front"),
        () -> assertFalse(splits.stream().anyMatch(i -> i.value().equals("DCT")), "None of these should be there"),
        () -> assertEquals("M085F350", splits.get(2).speedLevel(), "The speed/alt should be added to EBONY"),
        () -> assertEquals("EBONY", splits.get(2).value(), "All the IFR/DCT stuff should be dropped"),
        () -> assertTrue(splits.stream().noneMatch(i -> i.value().contains("/")))
    );
  }

  @Test
  void apply2() {
    List<SectionSplit> splits = SPLITTER.splits(plan2);

    assertAll(
        () -> assertEquals("HTO354018", splits.get(0).value()),
        () -> assertEquals("/", splits.get(0).wildcards()),
        () -> assertTrue(splits.stream().noneMatch(i -> i.value().contains("/")))
    );
  }

  @Test
  void apply3() {
    List<SectionSplit> splits = SPLITTER.splits(plan3);

    assertAll(
        () -> assertEquals("WOOST", splits.get(0).value()),
        () -> assertEquals("WXI", splits.get(1).value()),
        () -> assertTrue(splits.stream().noneMatch(i -> i.value().contains("/")))
    );
  }
}
