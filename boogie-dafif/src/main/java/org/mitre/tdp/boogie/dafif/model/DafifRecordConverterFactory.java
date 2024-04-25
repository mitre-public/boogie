package org.mitre.tdp.boogie.dafif.model;

import org.mitre.tdp.boogie.dafif.DafifVersion;
import org.mitre.tdp.boogie.dafif.v81.converter.DafifAddRunwayConverter;
import org.mitre.tdp.boogie.dafif.v81.converter.DafifAirportConverter;
import org.mitre.tdp.boogie.dafif.v81.converter.DafifAtsConverter;
import org.mitre.tdp.boogie.dafif.v81.validator.DafifAddRunwayValidator;
import org.mitre.tdp.boogie.dafif.v81.validator.DafifAirportValidator;
import org.mitre.tdp.boogie.dafif.v81.converter.DafifIlsConverter;
import org.mitre.tdp.boogie.dafif.v81.validator.DafifAtsValidator;
import org.mitre.tdp.boogie.dafif.v81.validator.DafifIlsValidator;
import org.mitre.tdp.boogie.dafif.v81.converter.DafifNavaidConverter;
import org.mitre.tdp.boogie.dafif.v81.validator.DafifNavaidValidator;
import org.mitre.tdp.boogie.dafif.v81.converter.DafifRunwayConverter;
import org.mitre.tdp.boogie.dafif.v81.validator.DafifRunwayValidator;
import org.mitre.tdp.boogie.dafif.v81.converter.DafifTerminalParentConverter;
import org.mitre.tdp.boogie.dafif.v81.validator.DafifTerminalParentValidator;
import org.mitre.tdp.boogie.dafif.v81.converter.DafifTerminalSegmentConverter;
import org.mitre.tdp.boogie.dafif.v81.validator.DafifTerminalSegmentValidator;
import org.mitre.tdp.boogie.dafif.v81.converter.DafifWaypointConverter;
import org.mitre.tdp.boogie.dafif.v81.validator.DafifWaypointValidator;

public final class DafifRecordConverterFactory {

  private DafifRecordConverterFactory() {
    throw new IllegalStateException("Unable to instantiate static factory class");
  }

  private static ConvertingDafifRecordConsumer.Builder standardConsumer() {
    return new ConvertingDafifRecordConsumer.Builder()
        .airportDelegator(new DafifAirportValidator())
        .airportConverter(new DafifAirportConverter())
        .runwayDelegator(new DafifRunwayValidator())
        .runwayConverter(new DafifRunwayConverter())
        .ilsDelegator(new DafifIlsValidator())
        .ilsConverter(new DafifIlsConverter())
        .navaidDelegator(new DafifNavaidValidator())
        .navaidConverter(new DafifNavaidConverter())
        .terminalParentDelegator(new DafifTerminalParentValidator())
        .terminalParentConverter(new DafifTerminalParentConverter())
        .terminalSegmentDelegator(new DafifTerminalSegmentValidator())
        .terminalSegmentConverter(new DafifTerminalSegmentConverter())
        .waypointDelegator(new DafifWaypointValidator())
        .waypointConverter(new DafifWaypointConverter())
        .atsDelegator(new DafifAtsValidator())
        .atsConverter(new DafifAtsConverter())
        .addRunwayDelegator(new DafifAddRunwayValidator())
        .addRunwayConverter(new DafifAddRunwayConverter());
  }

  public static ConvertingDafifRecordConsumer consumerForVersion(DafifVersion version) {
    switch (version) {
      case V81:
        return standardConsumer().build();
      default:
        throw new UnsupportedOperationException("No factory method supposed for provided version: " + version);
    }
  }
}
