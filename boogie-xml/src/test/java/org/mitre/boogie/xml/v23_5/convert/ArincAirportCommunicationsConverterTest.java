package org.mitre.boogie.xml.v23_5.convert;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincAirportCommunications;
import org.mitre.boogie.xml.model.fields.CommunicationClass;
import org.mitre.boogie.xml.model.fields.CommunicationType;
import org.mitre.boogie.xml.model.fields.DistanceDescription;
import org.mitre.boogie.xml.model.fields.FreqUnitOfMeasure;
import org.mitre.boogie.xml.model.fields.FrequencyUnits;
import org.mitre.boogie.xml.model.fields.H24Indicator;
import org.mitre.boogie.xml.model.fields.Modulation;
import org.mitre.boogie.xml.model.fields.RadarService;
import org.mitre.boogie.xml.model.fields.SignalEmission;
import org.mitre.boogie.xml.v23_5.generated.PortCommunication;

class ArincAirportCommunicationsConverterTest {
  private final ArincAirportCommunicationsConverter converter = ArincAirportCommunicationsConverter.INSTANCE;

  @Test
  void nullCommReturnsEmpty() {
    assertEquals(Optional.empty(), converter.apply(null));
  }

  @Test
  void invalidCommReturnsEmpty() {
    assertEquals(Optional.empty(), converter.apply(new PortCommunication()));
  }

  @Test
  void validCommConverts() {
    Optional<ArincAirportCommunications> result = converter.apply(TestGeneratedObjects.newValidPortCommunication());
    assertTrue(result.isPresent());

    ArincAirportCommunications comm = result.get();

    assertAll(
        () -> assertNotNull(comm.recordInfo()),
        () -> assertNotNull(comm.location().orElse(null)),

        () -> assertEquals(Optional.of("REMOTE-FAC-1"), comm.remoteFacilityRef()),
        () -> assertEquals(Optional.of("DCA TWR"), comm.callSign()),
        () -> assertEquals(Optional.of(CommunicationClass.ATCF), comm.communicationClass()),
        () -> assertEquals(Optional.of(CommunicationType.TWR), comm.communicationType()),
        () -> assertEquals(Optional.of(FrequencyUnits.VHF_NON_STANDARD), comm.frequencyUnits()),
        () -> assertEquals(Optional.of(RadarService.PRIMARY_OR_SECONDARY), comm.radarService()),
        () -> assertEquals(Optional.of(H24Indicator.CONTINUOUS), comm.h24Indicator()),
        () -> assertEquals(Optional.of(Modulation.AM_FREQ), comm.modulation()),
        () -> assertEquals(1, comm.sequenceNumber()),
        () -> assertEquals(Optional.of(SignalEmission.USB_CARRIER_UNK), comm.signalEmission()),

        () -> assertEquals(120.75, comm.transmitFrequencyValue().orElse(null), 0.001),
        () -> assertEquals(Optional.of(FreqUnitOfMeasure.MEGA_HERTZ), comm.transmitFreqUnitOfMeasure()),
        () -> assertEquals(120.75, comm.receiveFrequencyValue().orElse(null), 0.001),
        () -> assertEquals(Optional.of(FreqUnitOfMeasure.MEGA_HERTZ), comm.receiveFreqUnitOfMeasure()),

        () -> assertEquals(Optional.of(300), comm.transmitterSiteElevation()),
        () -> assertEquals(Optional.of(25L), comm.communicationDistance()),
        () -> assertEquals(Optional.of(DistanceDescription.OUT_TO_SPECIFIED_DISTANCE), comm.distanceDescription())
    );
  }

  @Test
  void convertsWithNullOptionalFields() {
    PortCommunication pc = TestGeneratedObjects.newValidPortCommunication();
    pc.setLocation(null);
    pc.setCallSign(null);
    pc.setCommunicationClass(null);
    pc.setTransmitFrequency(null);
    pc.setReceiveFrequency(null);
    pc.setCommunicationDistance(null);
    pc.setDistanceDescription(null);

    Optional<ArincAirportCommunications> result = converter.apply(pc);
    assertTrue(result.isPresent());

    ArincAirportCommunications comm = result.get();
    assertAll(
        () -> assertEquals(Optional.empty(), comm.location()),
        () -> assertEquals(Optional.empty(), comm.callSign()),
        () -> assertEquals(Optional.empty(), comm.communicationClass()),
        () -> assertEquals(Optional.empty(), comm.transmitFrequencyValue()),
        () -> assertEquals(Optional.empty(), comm.transmitFreqUnitOfMeasure()),
        () -> assertEquals(Optional.empty(), comm.receiveFrequencyValue()),
        () -> assertEquals(Optional.empty(), comm.receiveFreqUnitOfMeasure()),
        () -> assertEquals(Optional.empty(), comm.communicationDistance()),
        () -> assertEquals(Optional.empty(), comm.distanceDescription())
    );
  }
}
