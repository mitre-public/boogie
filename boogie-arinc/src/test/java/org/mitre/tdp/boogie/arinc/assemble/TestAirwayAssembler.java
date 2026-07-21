package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.util.Partitioners;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.TestArincFileParser;
import org.mitre.tdp.boogie.arinc.IsThisAPrimaryRecord;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.database.ArincFixDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;
import org.mitre.tdp.boogie.arinc.model.ArincRecordConverterFactory;
import org.mitre.tdp.boogie.arinc.model.ConvertingArincRecordConsumer;
import org.mitre.tdp.boogie.arinc.ArincVersion;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

class TestAirwayAssembler {
  private static ShouldSplitAirway shouldSplit = ShouldSplitAirway.INSTANCE;

  /**
   * This file contains all the airway legs for J121 + T222 plus any record which mentions any of the associated fixes or recommended
   * navaids therein.
   */
  private static final File arincTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/j121-t222-and-friends.txt"));

  private static Multimap<String, Airway> airwayMap;

  @BeforeAll
  static void setup() {
    IsThisAPrimaryRecord isThisAPrimaryRecord = new IsThisAPrimaryRecord();
    recordParser.parseAll(arincTestFile).stream().filter(isThisAPrimaryRecord).forEach(testV18Consumer);

    ArincFixDatabase arincFixDatabase = ArincDatabaseFactory.newFixDatabase(
        testV18Consumer.arincNdbNavaids(),
        testV18Consumer.arincVhfNavaids(),
        testV18Consumer.arincWaypoints(),
        testV18Consumer.arincAirports(),
        testV18Consumer.arincHoldingPatterns(),
        testV18Consumer.arincHeliports()
    );

    AirwayAssembler<Airway> assembler = AirwayAssembler.standard(arincFixDatabase);

    airwayMap = assembler.assemble(testV18Consumer.arincAirwayLegs()).collect(ArrayListMultimap::create, (m, i) -> m.put(i.airwayIdentifier(), i), Multimap::putAll);
  }

  @Test
  void testAssemblyOfJ121() {
    Airway J121 = airwayMap.get("J121").iterator().next();

    assertAll(
        () -> assertEquals("J121", J121.airwayIdentifier()),
        () -> assertEquals("CHS|JMACK|BARTL|ISO|WEAVR|ORF|SAWED|KALDA|DUNFE|SWL|RADDS|SIE|AVALO|BRIGS", associatedFixSequence(J121.legs())),
        () -> assertEquals("", recommendedNavaidSequence(J121.legs()))
    );
  }

  @Test
  void testAssemblyOfT222() {
    Airway T222 = airwayMap.get("T222").iterator().next();

    assertAll(
        () -> assertEquals("T222", T222.airwayIdentifier()),
        () -> assertEquals("BAERE|SPY|HYLEE|ALIEN|RUFVY|BET|HOLIN|CABOT|ANIAK|ZIDMU|UTICE|JOANY|MCG|MEFRA|SUCOD|HEMRO|ENN|FAI", associatedFixSequence(T222.legs())),
        () -> assertEquals("", recommendedNavaidSequence(T222.legs()))
    );
  }

  @Test
  void testSorting() {
    List<ArincAirwayLeg> sorted = AirwayMocks.legs().stream().sorted(new ArincAirwayLegComparator()).collect(Collectors.toList());
    assertEquals("first|second|third|fourth|fifth|sixth|seventh", sorted.stream().map(ArincAirwayLeg::fixIdentifier).collect(Collectors.joining("|")));
  }

  @Test
  void listSplitting() {
    List<List<ArincAirwayLeg>> legs = AirwayMocks.legs().stream().sorted(new ArincAirwayLegComparator()).collect(Partitioners.newListCollector((list, next) -> shouldSplit.negate().test(list.get(list.size() - 1), next)));
    assertAll(
        () -> assertEquals(3, legs.size(), "Two US airways and one CAN"),
        () -> assertEquals("first|second|third|fourth|fifth|sixth|seventh", legs.stream().flatMap(Collection::stream).map(ArincAirwayLeg::fixIdentifier).collect(Collectors.joining("|")), "Sorting should be the same just in different lists")
    );
  }

  private String associatedFixSequence(List<? extends Leg> legs) {
    return legs.stream().map(Leg::associatedFix).filter(Optional::isPresent).map(Optional::get).map(Fix::fixIdentifier).collect(Collectors.joining("|"));
  }

  private String recommendedNavaidSequence(List<? extends Leg> legs) {
    return legs.stream().map(Leg::recommendedNavaid).filter(Optional::isPresent).map(Optional::get).map(Fix::fixIdentifier).collect(Collectors.joining("|"));
  }

  private static final TestArincFileParser recordParser = new TestArincFileParser(ArincRecordParser.standard(ArincVersion.V19.specs()));

  private static final ConvertingArincRecordConsumer testV18Consumer = ArincRecordConverterFactory.consumerForVersion(ArincVersion.V19);
}
