package org.mitre.tdp.boogie.arinc.model;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.AirportConverter;
import org.mitre.tdp.boogie.arinc.v18.AirportPrimaryExtensionConverter;
import org.mitre.tdp.boogie.arinc.v18.AirportPrimaryExtensionValidator;
import org.mitre.tdp.boogie.arinc.v18.AirportValidator;
import org.mitre.tdp.boogie.arinc.v18.AirwayLegConverter;
import org.mitre.tdp.boogie.arinc.v18.AirwayLegValidator;
import org.mitre.tdp.boogie.arinc.v18.FirUirLegConverter;
import org.mitre.tdp.boogie.arinc.v18.FirUirLegValidator;
import org.mitre.tdp.boogie.arinc.v18.GnssLandingSystemConverter;
import org.mitre.tdp.boogie.arinc.v18.GnssLandingSystemValidator;
import org.mitre.tdp.boogie.arinc.v18.HoldingPatternValidator;
import org.mitre.tdp.boogie.arinc.v18.LocalizerGlideSlopeConverter;
import org.mitre.tdp.boogie.arinc.v18.LocalizerGlideSlopeValidator;
import org.mitre.tdp.boogie.arinc.v18.NdbNavaidConverter;
import org.mitre.tdp.boogie.arinc.v18.NdbNavaidValidator;
import org.mitre.tdp.boogie.arinc.v18.ProcedureLegConverter;
import org.mitre.tdp.boogie.arinc.v18.ProcedureLegValidator;
import org.mitre.tdp.boogie.arinc.v18.RunwayConverter;
import org.mitre.tdp.boogie.arinc.v18.RunwayValidator;
import org.mitre.tdp.boogie.arinc.v18.VhfNavaidConverter;
import org.mitre.tdp.boogie.arinc.v18.VhfNavaidValidator;
import org.mitre.tdp.boogie.arinc.v18.WaypointConverter;
import org.mitre.tdp.boogie.arinc.v18.WaypointValidator;
import org.mitre.tdp.boogie.arinc.v21.HelipadConverter;
import org.mitre.tdp.boogie.arinc.v21.HelipadValidator;

/**
 * Factory class for common instantiations of {@link ConvertingArincRecordConsumer}s.
 * <br>
 * Note that the consumer classes do allow hybridization of generic-to-specific record converter implementations (with some smart
 * functional composition) - however we provide as standard parts of the API only single-version factory implementations.
 */
public final class ArincRecordConverterFactory {

  private ArincRecordConverterFactory() {
    throw new IllegalStateException("Unable to instantiate static factory class");
  }

  private static ConvertingArincRecordConsumer.Builder standardConsumer() {
    return new ConvertingArincRecordConsumer.Builder()
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
        .gnssLandingSystemDelegator(new GnssLandingSystemValidator())
        .gnssLandingSystemConverter(new GnssLandingSystemConverter())
        .holdingPatternDelegator(new HoldingPatternValidator())
        .firUirDelegator(new FirUirLegValidator())
        .firUirConverter(new FirUirLegConverter())
        .helipadDelegator(new HelipadValidator())
        .helipadConverter(new HelipadConverter());
  }

  private static ConvertingArincRecordMapper.Builder standardMapper() {
    return new ConvertingArincRecordMapper.Builder()
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
        .gnssLandingSystemDelegator(new GnssLandingSystemValidator())
        .gnssLandingSystemConverter(new GnssLandingSystemConverter())
        .holdingPatternDelegator(new GnssLandingSystemValidator())
        .firUirDelegator(new FirUirLegValidator())
        .firUirConverter(new FirUirLegConverter())
        .helipadDelegator(new HelipadValidator())
        .helipadConverter(new HelipadConverter());
  }

  /**
   * Factory instantiation of a {@link ConvertingArincRecordConsumer} using all ARINC Version 18 adapter and conversion classes
   * for the {@link ArincRecord} to boogie.arinc.model data classes.
   * <br>
   * The V18 converters work perfectly fine with the V19 record specifications in {@link ArincVersion#V19} (as they share the same
   * fieldsets and overall structure - albeit with slightly different contents and holding patterns). However, because
   * the newer holding pattern has new fields, a new consumer bust me bade for 18 vs 19. However, the same validator works
   * for both 19/18 holds as only optional fields were added.
   * <br>
   * The V20 runway converters are ok, because the arinc record just moved the field to a place it fits at full res.
   * The procedure leg needs more processing for new data.
   * <br>
   * The V21 needed new fields processed for holding, gnss, and procedureLegs.
   */
  public static ConvertingArincRecordConsumer consumerForVersion(ArincVersion version) {
    return switch (version) {
      case V18, V18_NAV, V18_AIRSPACE -> standardConsumer()
          .holdingPatternConverter(new org.mitre.tdp.boogie.arinc.v18.HoldingPatternConverter())
          .build();
      case V19, V19_NAV -> standardConsumer()
          .airwayLegConverter(new org.mitre.tdp.boogie.arinc.v19.AirwayLegConverter())
          .holdingPatternConverter(new org.mitre.tdp.boogie.arinc.v19.HoldingPatternConverter())
          .build();
      case V20, V20_NAV -> standardConsumer()
          .airwayLegConverter(new org.mitre.tdp.boogie.arinc.v19.AirwayLegConverter())
          .holdingPatternConverter(new org.mitre.tdp.boogie.arinc.v20.HoldingPatternConverter())
          .procedureLegConverter(new org.mitre.tdp.boogie.arinc.v20.ProcedureLegConverter())
          .build();
      case V21, V21_NAV -> standardConsumer()
          .airwayLegConverter(new org.mitre.tdp.boogie.arinc.v19.AirwayLegConverter())
          .holdingPatternConverter(new org.mitre.tdp.boogie.arinc.v20.HoldingPatternConverter())
          .gnssLandingSystemConverter(new org.mitre.tdp.boogie.arinc.v21.GnssLandingSystemConverter())
          .procedureLegConverter(new org.mitre.tdp.boogie.arinc.v21.ProcedureLegConverter())
          .build();
      case V22, V22_NAV -> standardConsumer()
          .airwayLegConverter(new org.mitre.tdp.boogie.arinc.v22.AirwayLegConverter())
          .holdingPatternConverter(new org.mitre.tdp.boogie.arinc.v20.HoldingPatternConverter())
          .gnssLandingSystemConverter(new org.mitre.tdp.boogie.arinc.v21.GnssLandingSystemConverter())
          .procedureLegConverter(new org.mitre.tdp.boogie.arinc.v22.ProcedureLegConverter())
          .runwayConverter(new org.mitre.tdp.boogie.arinc.v22.RunwayConverter())
          .build();
    };
  }

  /**
   * Factory instantiation of a {@link ConvertingArincRecordMapper} using all ARINC Version 18 adapter and conversion classes for
   * the {@link ArincRecord} to boogie.arinc.model data classes.
   * <br>
   * The V18 converters work perfectly fine with the V19 record specifications in {@link ArincVersion#V19} (as they share the same
   * fieldsets and overall structure - albeit with slightly different contents and holding patterns). However, because
   * the newer holding pattern has new fields, a new consumer bust me bade for 18 vs 19. However, the same validator works
   * for both 19/18 holds as only optional fields were added.
   * <br>
   * The V20 runway converters are ok, because the arinc record just moved the field to a place it fits at full res.
   * The procedure leg needs more processing for new data.
   * <br>
   * The V21 needed new fields processed for holding, gnss, and procedureLegs.
   */
  public static ConvertingArincRecordMapper mapperForVersion(ArincVersion version) {
    return switch (version) {
      case V18, V18_NAV, V18_AIRSPACE -> standardMapper()
          .holdingPatternConverter(new org.mitre.tdp.boogie.arinc.v18.HoldingPatternConverter())
          .build();
      case V19, V19_NAV -> standardMapper()
          .airwayLegConverter(new org.mitre.tdp.boogie.arinc.v19.AirwayLegConverter())
          .holdingPatternConverter(new org.mitre.tdp.boogie.arinc.v19.HoldingPatternConverter())
          .build();
      case V20, V20_NAV -> standardMapper()
          .airwayLegConverter(new org.mitre.tdp.boogie.arinc.v19.AirwayLegConverter())
          .holdingPatternConverter(new org.mitre.tdp.boogie.arinc.v20.HoldingPatternConverter())
          .procedureLegConverter(new org.mitre.tdp.boogie.arinc.v20.ProcedureLegConverter())
          .build();
      case V21, V21_NAV -> standardMapper()
          .airwayLegConverter(new org.mitre.tdp.boogie.arinc.v19.AirwayLegConverter())
          .holdingPatternConverter(new org.mitre.tdp.boogie.arinc.v20.HoldingPatternConverter())
          .gnssLandingSystemConverter(new org.mitre.tdp.boogie.arinc.v21.GnssLandingSystemConverter())
          .procedureLegConverter(new org.mitre.tdp.boogie.arinc.v21.ProcedureLegConverter())
          .build();
      case V22, V22_NAV -> standardMapper()
          .airwayLegConverter(new org.mitre.tdp.boogie.arinc.v22.AirwayLegConverter())
          .holdingPatternConverter(new org.mitre.tdp.boogie.arinc.v20.HoldingPatternConverter())
          .gnssLandingSystemConverter(new org.mitre.tdp.boogie.arinc.v21.GnssLandingSystemConverter())
          .procedureLegConverter(new org.mitre.tdp.boogie.arinc.v21.ProcedureLegConverter())
          .runwayConverter(new org.mitre.tdp.boogie.arinc.v22.RunwayConverter())
          .build();
    };
  }
}
