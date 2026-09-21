package org.mitre.tdp.boogie;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class CenterIdentificationTest {

  @Test
  void verifyValueSemantics() {
    EqualsVerifier.forClass(CenterIdentification.class).verify();
  }

  @Test
  void testIdentifierOnly() {
    CenterIdentification identification = CenterIdentification.builder("KJSD").build();

    assertAll(
        () -> assertEquals("KJSD", identification.identifier()),
        () -> assertTrue(identification.area().isEmpty()),
        () -> assertTrue(identification.icaoRegion().isEmpty()),
        () -> assertTrue(identification.type().isEmpty())
    );
  }

  @Test
  void testCompleteIdentificationAndCopy() {
    CenterIdentification identification = CenterIdentification.builder("KJSD")
        .area("USA")
        .icaoRegion("K6")
        .type(BoogieType.HELIPORT)
        .build();
    CenterIdentification copy = identification.toBuilder().build();

    assertAll(
        () -> assertEquals("KJSD", identification.identifier()),
        () -> assertEquals(Optional.of("USA"), identification.area()),
        () -> assertEquals(Optional.of("K6"), identification.icaoRegion()),
        () -> assertEquals(Optional.of(BoogieType.HELIPORT), identification.type()),
        () -> assertEquals(identification, copy),
        () -> assertEquals(identification.hashCode(), copy.hashCode()),
        () -> assertEquals(identification.toString(), copy.toString()),
        () -> assertNotEquals(identification, identification.toBuilder().type(BoogieType.AIRPORT).build())
    );
  }

  @Test
  void testNullMetadataIsAbsent() {
    CenterIdentification identifierOnly = CenterIdentification.builder("KJSD").build();
    CenterIdentification nullMetadata = identifierOnly.toBuilder()
        .area(null).icaoRegion(null).type(null).build();
    assertEquals(identifierOnly, nullMetadata);
  }

  @Test
  void testTypeCanBeCleared() {
    CenterIdentification identification = CenterIdentification.builder("KJSD").type(BoogieType.HELIPORT).build();
    CenterIdentification cleared = identification.toBuilder().type(null).build();

    assertAll(
        () -> assertTrue(cleared.type().isEmpty()),
        () -> assertEquals(CenterIdentification.builder("KJSD").build(), cleared),
        () -> assertEquals(Optional.of(BoogieType.HELIPORT), identification.type())
    );
  }

  @Test
  void testIdentifierIsRequired() {
    assertThrows(NullPointerException.class, () -> CenterIdentification.builder(null).build());
  }
}
