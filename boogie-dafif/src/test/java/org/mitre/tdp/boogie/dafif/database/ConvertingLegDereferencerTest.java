package org.mitre.tdp.boogie.dafif.database;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.dafif.assemble.FixAssemblyStrategy;
import org.mitre.tdp.boogie.dafif.model.DafifAddRunway;
import org.mitre.tdp.boogie.dafif.model.DafifAirport;
import org.mitre.tdp.boogie.dafif.model.DafifIls;
import org.mitre.tdp.boogie.dafif.model.DafifRunway;

class ConvertingLegDereferencerTest {

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void resolvesLocalizerAndDmeWithTheSameIdentifierRegardlessOfOrder(boolean dmeFirst) {
    DafifIls localizer = ils("Z", 24.884694, 55.182667);
    DafifIls dme = ils("D", 24.906694, 55.145222);
    var dereferencer = dereferencer(dmeFirst ? List.of(dme, localizer) : List.of(localizer, dme), List.of());

    assertAll(
        () -> assertEquals(LatLong.of(24.884694, 55.182667), dereferencer.nav1("US00001", "IABC", "Z", "US", 1).latLong()),
        () -> assertEquals(LatLong.of(24.906694, 55.145222), dereferencer.nav1("US00001", "IABC", "D", "US", 1).latLong())
    );
  }

  @Test
  void doesNotSubstituteAnIlsComponentWhenTheRequestedTypeIsAbsent() {
    var onlyDme = dereferencer(List.of(ils("D", 40.0, -70.0)), List.of());
    var onlyLocalizer = dereferencer(List.of(ils("Z", 40.01, -70.01)), List.of());

    assertAll(
        () -> assertNull(onlyDme.nav1("US00001", "IABC", "Z", "US", 1)),
        () -> assertNull(onlyLocalizer.nav1("US00001", "IABC", "D", "US", 1))
    );
  }

  @Test
  void usesPhysicalRunwayEndsForSidsAndDisplacedThresholdsForApproaches() {
    var supplement = DafifAddRunway.builder().airportIdentification("US00001")
        .lowEndRunwayIdentifier("09").highEndRunwayIdentifier("27")
        .lowEndDisplacedThresholdDegreesLatitude(40.002).lowEndDisplacedThresholdDegreesLongitude(-70.002)
        .highEndDisplacedThresholdDegreesLatitude(40.008).highEndDisplacedThresholdDegreesLongitude(-70.008)
        .build();
    var dereferencer = dereferencer(List.of(), List.of(supplement));

    assertAll(
        () -> assertEquals(LatLong.of(40.0, -70.0), dereferencer.fix("G", "US00001", "RW09", "US", 2).latLong()),
        () -> assertEquals(LatLong.of(40.01, -70.01), dereferencer.fix("G", "US00001", "RW27", "US", 2).latLong()),
        () -> assertEquals(LatLong.of(40.002, -70.002), dereferencer.fix("G", "US00001", "RW09", "US", 3).latLong()),
        () -> assertEquals(LatLong.of(40.008, -70.008), dereferencer.fix("G", "US00001", "RW27", "US", 3).latLong()),
        () -> assertEquals(LatLong.of(40.002, -70.002), dereferencer.fix("G", "US00001", "RW09", "US").latLong())
    );
  }

  @Test
  void usesPhysicalRunwayEndsForApproachesWithoutDisplacedThresholds() {
    var dereferencer = dereferencer(List.of(), List.of());

    assertAll(
        () -> assertEquals(LatLong.of(40.0, -70.0), dereferencer.fix("G", "US00001", "RW09", "US", 3).latLong()),
        () -> assertEquals(LatLong.of(40.01, -70.01), dereferencer.fix("G", "US00001", "RW27", "US", 3).latLong())
    );
  }

  private static ConvertingLegDereferencer<Fix> dereferencer(List<DafifIls> components, List<DafifAddRunway> supplements) {
    var airport = DafifAirport.builder().airportIdentification("US00001").icaoCode("KAAA").magVarOfRecord("W010020").build();
    var runway = DafifRunway.builder().airportIdentification("US00001").lowEndIdentifier("09").highEndIdentifier("27")
        .lowEndDegreesLatitude(40.0).lowEndDegreesLongitude(-70.0)
        .highEndDegreesLatitude(40.01).highEndDegreesLongitude(-70.01).build();
    var tad = DafifDatabaseFactory.newTerminalAreaDatabase(List.of(airport), List.of(runway), supplements, components, List.of());
    var fdb = DafifDatabaseFactory.newFixDatabase(List.of(), List.of());
    return ConvertingLegDereferencer.from(fdb, tad, FixAssemblyStrategy.standard());
  }

  private static DafifIls ils(String component, double latitude, double longitude) {
    return DafifIls.builder().airportIdentification("US00001").runwayIdentifier("09").componentType(component)
        .ilsNavaidIdentifier("IABC").degreesLatitude(latitude).degreesLongitude(longitude).ilsSlaveVariation("W010 0120").build();
  }
}
