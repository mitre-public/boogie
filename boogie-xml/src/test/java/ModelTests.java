import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincAirport;
import org.mitre.boogie.xml.model.ArincAirportCommunications;
import org.mitre.boogie.xml.model.ArincAirportGate;
import org.mitre.boogie.xml.model.ArincAirway;
import org.mitre.boogie.xml.model.ArincAirwayLeg;
import org.mitre.boogie.xml.model.ArincGnssLandingSystem;
import org.mitre.boogie.xml.model.ArincHelipad;
import org.mitre.boogie.xml.model.ArincHoldingPattern;
import org.mitre.boogie.xml.model.ArincLocalizerGlideSlope;
import org.mitre.boogie.xml.model.ArincLocalizerGlideslopeMarker;
import org.mitre.boogie.xml.model.ArincMsa;
import org.mitre.boogie.xml.model.ArincNdbNavaid;
import org.mitre.boogie.xml.model.ArincProcedure;
import org.mitre.boogie.xml.model.ArincProcedureLeg;
import org.mitre.boogie.xml.model.ArincRunway;
import org.mitre.boogie.xml.model.ArincTransition;
import org.mitre.boogie.xml.model.ArincVhfNavaid;
import org.mitre.boogie.xml.model.ArincWaypoint;
import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.ArincFraInfo;
import org.mitre.boogie.xml.model.fields.ArincMsaSector;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincPortInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.ArincTaa;
import org.mitre.boogie.xml.model.fields.ArincTaaSectorDetails;
import org.mitre.boogie.xml.model.fields.ArincWaypointType;
import org.mitre.boogie.xml.model.fields.ArincWaypointUsage;
import org.mitre.boogie.xml.model.fields.SupplementalData;
import org.mitre.boogie.xml.model.fields.UtcOffset;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class ModelTests {
  @Test
  void testArincModels() {
    EqualsVerifier.forClass(ArincAirport.class).verify();
    EqualsVerifier.forClass(ArincAirportCommunications.class).verify();
    EqualsVerifier.forClass(ArincAirportGate.class).verify();
    EqualsVerifier.forClass(ArincAirway.class).verify();
    EqualsVerifier.forClass(ArincAirwayLeg.class).verify();
    EqualsVerifier.forClass(ArincGnssLandingSystem.class).verify();
    EqualsVerifier.forClass(ArincHelipad.class).verify();
    EqualsVerifier.forClass(ArincLocalizerGlideSlope.class).verify();
    EqualsVerifier.forClass(ArincLocalizerGlideslopeMarker.class).verify();
    EqualsVerifier.forClass(ArincMsa.class).verify();
    EqualsVerifier.forClass(ArincMsaSector.class).verify();
    EqualsVerifier.forClass(ArincNdbNavaid.class).verify();
    EqualsVerifier.forClass(ArincProcedure.class)
        .suppress(Warning.BIGDECIMAL_EQUALITY)
        .verify();
    EqualsVerifier.forClass(ArincProcedureLeg.class).verify();
    EqualsVerifier.forClass(ArincRunway.class).verify();
    EqualsVerifier.forClass(ArincTransition.class).verify();
    EqualsVerifier.forClass(ArincTaaSectorDetails.class).verify();
    EqualsVerifier.forClass(ArincVhfNavaid.class).verify();
    EqualsVerifier.forClass(ArincWaypoint.class).verify();
    EqualsVerifier.forClass(ArincHoldingPattern.class).verify();
  }

  @Test
  void testArincModelFields() {
    EqualsVerifier.forClass(ArincBaseInfo.class).verify();
    EqualsVerifier.forClass(ArincFraInfo.class).verify();
    EqualsVerifier.forClass(ArincPointInfo.class).verify();
    EqualsVerifier.forClass(ArincPortInfo.class).verify();
    EqualsVerifier.forClass(ArincRecordInfo.class).verify();
    EqualsVerifier.forClass(ArincTaa.class).verify();
    EqualsVerifier.forClass(ArincWaypointType.class).verify();
    EqualsVerifier.forClass(ArincWaypointUsage.class).verify();
    EqualsVerifier.forClass(SupplementalData.Record.class).verify();
    EqualsVerifier.forClass(UtcOffset.class).verify();
  }
}
