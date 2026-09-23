package org.mitre.tdp.boogie;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;

import com.google.common.collect.Range;
import nl.jqno.equalsverifier.EqualsVerifier;

class AirspaceTest {

  @Test
  void verifyStandard() {
    EqualsVerifier.forClass(Airspace.Standard.class)
        .withCachedHashCode("hashCode", "computeHashCode", createStandard())
        .verify();
  }

  @Test
  void verifyStandardLeg() {
    EqualsVerifier.forClass(AirspaceSequence.Standard.class)
        .withCachedHashCode("hashCode", "computeHashCode", createSequence())
        .verify();
  }

  @Test
  void verifyRecord() {
    EqualsVerifier.forClass(Airspace.Record.class)
        .withCachedHashCode("hashCode", "computeHashCode", Airspace.record("WHATEVER", createStandard()))
        .verify();
  }

  @Test
  void verifySequenceRecord() {
    EqualsVerifier.forClass(AirspaceSequence.Record.class)
        .withCachedHashCode("hashCode", "computeHashCode", AirspaceSequence.record("WHATEVER", createSequence()))
        .verify();
  }

  @Test
  void testModel() {
    Airspace airspace = createStandard();
    assertAll(
        () -> assertEquals("name", airspace.identifier()),
        () -> assertEquals(AirspaceType.CONTROLLED, airspace.airspaceType()),
        () -> assertEquals("N", airspace.center().get().fixIdentifier()),
        () -> assertEquals(Range.all(), airspace.altitudeLimit())
    );
  }

  @Test
  void testSequenceModel() {
    AirspaceSequence sequence = createSequence();
    assertEquals(10, sequence.sequenceNumber());
    assertEquals(Geometry.CIRCLE, sequence.geometry());
  }

  @Test
  void testCenterIdentificationDoesNotRequireAResolvedFix() {
    CenterIdentification identification = createCenterIdentification();
    Airspace.Standard airspace = createStandard().toBuilder()
        .center(null)
        .centerIdentification(identification)
        .build();

    assertAll(
        () -> assertEquals(Optional.of(identification), airspace.centerIdentification()),
        () -> assertEquals(Optional.of(BoogieType.AIRSPACE), airspace.centerIdentification().flatMap(CenterIdentification::type)),
        () -> assertTrue(airspace.center().isEmpty()),
        () -> assertEquals(airspace, airspace.toBuilder().build()),
        () -> assertEquals(airspace.hashCode(), airspace.toBuilder().build().hashCode()),
        () -> assertTrue(airspace.toString().contains(identification.toString()))
    );
  }

  @Test
  void testClearingCenterIdentificationPreservesResolvedFix() {
    Airspace.Standard airspace = createStandard().toBuilder().centerIdentification(createCenterIdentification()).build();
    Airspace cleared = airspace.toBuilder().centerIdentification(null).build();

    assertAll(
        () -> assertTrue(cleared.centerIdentification().isEmpty()),
        () -> assertEquals(airspace.center(), cleared.center())
    );
  }

  @Test
  void testCenterMetadataParticipatesInValueSemantics() {
    Airspace.Standard airspace = createStandard().toBuilder().centerIdentification(createCenterIdentification()).build();
    Airspace identifierOnly = airspace.toBuilder()
        .centerIdentification(CenterIdentification.builder("DGAC").build())
        .build();

    assertAll(
        () -> assertEquals(airspace.centerIdentification().map(CenterIdentification::identifier),
            identifierOnly.centerIdentification().map(CenterIdentification::identifier)),
        () -> assertNotEquals(airspace, identifierOnly)
    );
  }

  @Test
  void testRecordDelegatesCenterIdentification() {
    Airspace.Standard airspace = createStandard().toBuilder().centerIdentification(createCenterIdentification()).build();
    Airspace record = Airspace.record("source", airspace);

    assertAll(
        () -> assertEquals(airspace.centerIdentification(), record.centerIdentification()),
        () -> assertEquals(airspace.center(), record.center())
    );
  }

  @Test
  void testDefaultCenterIdentificationIsEmpty() {
    Airspace airspace = new Airspace() {
      @Override public String area() { return "USA"; }
      @Override public String identifier() { return "name"; }
      @Override public AirspaceType airspaceType() { return AirspaceType.CONTROLLED; }
      @Override public List<? extends AirspaceSequence> sequences() { return List.of(); }
      @Override public Range<Double> altitudeLimit() { return Range.all(); }
      @Override public Optional<? extends Fix> center() { return Optional.empty(); }
      @Override public void accept(Visitor visitor) { }
    };

    assertAll(
        () -> assertTrue(airspace.centerIdentification().isEmpty()),
        () -> assertTrue(Airspace.record("source", airspace).centerIdentification().isEmpty())
    );
  }

  private CenterIdentification createCenterIdentification() {
    return CenterIdentification.builder("DGAC")
        .area("AFR")
        .icaoRegion("DG")
        .type(BoogieType.AIRSPACE)
        .build();
  }

  private Airspace.Standard createStandard() {
    return Airspace.builder()
        .identifier("name")
        .center(Fix.builder().fixIdentifier("N").latLong(LatLong.of(0., 1.)).build())
        .airspaceType(AirspaceType.CONTROLLED)
        .sequences(List.of())
        .altitudeLimit(Range.all())
        .build();
  }

  private AirspaceSequence.Standard createSequence() {
    return AirspaceSequence.builder(Geometry.CIRCLE, 10)
        .centerFix(LatLong.of(45.0, 45.0))
        .build();
  }
}
