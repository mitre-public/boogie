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
import org.mitre.tdp.boogie.arinc.ArincFileParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.IsThisAPrimaryRecord;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.database.ArincFixDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;
import org.mitre.tdp.boogie.arinc.model.ArincRecordConverterFactory;
import org.mitre.tdp.boogie.arinc.model.ConvertingArincRecordConsumer;
import org.mitre.tdp.boogie.arinc.v18.AirportConverter;
import org.mitre.tdp.boogie.arinc.v18.AirportPrimaryExtensionConverter;
import org.mitre.tdp.boogie.arinc.v18.AirportPrimaryExtensionValidator;
import org.mitre.tdp.boogie.arinc.v18.AirportSpec;
import org.mitre.tdp.boogie.arinc.v18.AirportValidator;
import org.mitre.tdp.boogie.arinc.v18.AirwayLegConverter;
import org.mitre.tdp.boogie.arinc.v18.AirwayLegSpec;
import org.mitre.tdp.boogie.arinc.v18.AirwayLegValidator;
import org.mitre.tdp.boogie.arinc.v18.ControlledAirspaceLegConverter;
import org.mitre.tdp.boogie.arinc.v18.ControlledAirspaceValidator;
import org.mitre.tdp.boogie.arinc.v18.FirUirLegConverter;
import org.mitre.tdp.boogie.arinc.v18.FirUirLegValidator;
import org.mitre.tdp.boogie.arinc.v18.GnssLandingSystemConverter;
import org.mitre.tdp.boogie.arinc.v18.GnssLandingSystemValidator;
import org.mitre.tdp.boogie.arinc.v18.Header01Converter;
import org.mitre.tdp.boogie.arinc.v18.Header01Validator;
import org.mitre.tdp.boogie.arinc.v18.HeliportConverter;
import org.mitre.tdp.boogie.arinc.v18.HeliportValidator;
import org.mitre.tdp.boogie.arinc.v18.HoldingPatternConverter;
import org.mitre.tdp.boogie.arinc.v18.HoldingPatternValidator;
import org.mitre.tdp.boogie.arinc.v18.LocalizerGlideSlopeConverter;
import org.mitre.tdp.boogie.arinc.v18.LocalizerGlideSlopeSpec;
import org.mitre.tdp.boogie.arinc.v18.LocalizerGlideSlopeValidator;
import org.mitre.tdp.boogie.arinc.v18.NdbNavaidConverter;
import org.mitre.tdp.boogie.arinc.v18.NdbNavaidSpec;
import org.mitre.tdp.boogie.arinc.v18.NdbNavaidValidator;
import org.mitre.tdp.boogie.arinc.v18.ProcedureLegConverter;
import org.mitre.tdp.boogie.arinc.v18.ProcedureLegValidator;
import org.mitre.tdp.boogie.arinc.v18.RunwayConverter;
import org.mitre.tdp.boogie.arinc.v18.RunwaySpec;
import org.mitre.tdp.boogie.arinc.v18.RunwayValidator;
import org.mitre.tdp.boogie.arinc.v18.VhfNavaidConverter;
import org.mitre.tdp.boogie.arinc.v18.VhfNavaidSpec;
import org.mitre.tdp.boogie.arinc.v18.VhfNavaidValidator;
import org.mitre.tdp.boogie.arinc.v18.WaypointConverter;
import org.mitre.tdp.boogie.arinc.v18.WaypointSpec;
import org.mitre.tdp.boogie.arinc.v18.WaypointValidator;
import org.mitre.tdp.boogie.arinc.v19.ProcedureLegSpec;
import org.mitre.tdp.boogie.arinc.v21.HelipadConverter;
import org.mitre.tdp.boogie.arinc.v21.HelipadValidator;

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
    fileParser.apply(arincTestFile).stream().filter(isThisAPrimaryRecord).forEach(testV18Consumer);

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

  /**
   * In implementation this could be done from {@link ArincVersion} - e.g. new ArincFileParser(ArincVersion.V19.parser());
   */
  private static final ArincFileParser fileParser = new ArincFileParser(
      new AirportSpec(),
      new AirwayLegSpec(),
      new LocalizerGlideSlopeSpec(),
      new NdbNavaidSpec(),
      // the V19 leg spec - thanks CIFP
      new ProcedureLegSpec(),
      new RunwaySpec(),
      new VhfNavaidSpec(),
      new WaypointSpec()
  );

  /**
   * In implementation this could be done from the factory class {@link ArincRecordConverterFactory}.
   */
  private static final ConvertingArincRecordConsumer testV18Consumer = new ConvertingArincRecordConsumer.Builder()
      .airportDelegator(new AirportValidator())
      .airportConverter(new AirportConverter())
      .airportContinuationConverter(new AirportPrimaryExtensionConverter())
      .airportContinuationDelegator(new AirportPrimaryExtensionValidator())
      .airwayLegDelegator(new AirwayLegValidator())
      .airwayLegConverter(new AirwayLegConverter())
      .localizerGlideSlopeDelegator(new LocalizerGlideSlopeValidator())
      .localizerGlideSlopeConverter(new LocalizerGlideSlopeConverter())
      .ndbNavaidDelegator(new NdbNavaidValidator())
      .ndbNavaidConverter(new NdbNavaidConverter())
      .procedureLegDelegator(new ProcedureLegValidator())
      .procedureLegConverter(new ProcedureLegConverter())
      .runwayDelegator(new RunwayValidator())
      .runwayConverter(new RunwayConverter())
      .vhfNavaidDelegator(new VhfNavaidValidator())
      .vhfNavaidConverter(new VhfNavaidConverter())
      .waypointDelegator(new WaypointValidator())
      .waypointConverter(new WaypointConverter())
      .gnssLandingSystemConverter(new GnssLandingSystemConverter())
      .gnssLandingSystemDelegator(new GnssLandingSystemValidator())
      .holdingPatternConverter(new HoldingPatternConverter())
      .holdingPatternDelegator(new HoldingPatternValidator())
      .firUirConverter(new FirUirLegConverter())
      .firUirDelegator(new FirUirLegValidator())
      .helipadDelegator(new HelipadValidator())
      .helipadConverter(new HelipadConverter())
      .arincControlledAirspaceConverter(new ControlledAirspaceLegConverter())
      .arincControlledAirspaceLegDelegator(new ControlledAirspaceValidator())
      .headerDelegator(new Header01Validator())
      .headerConverter(new Header01Converter())
      .heliportConverter(new HeliportConverter())
      .heliportDelegator(new HeliportValidator())
      .build();
}
