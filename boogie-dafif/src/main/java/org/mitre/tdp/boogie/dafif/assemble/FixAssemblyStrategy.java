package org.mitre.tdp.boogie.dafif.assemble;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.AiracCycle;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.dafif.database.DafifFixDatabase;
import org.mitre.tdp.boogie.dafif.database.DafifTerminalAreaDatabase;
import org.mitre.tdp.boogie.dafif.model.DafifAirport;
import org.mitre.tdp.boogie.dafif.model.DafifIls;
import org.mitre.tdp.boogie.dafif.model.DafifNavaid;
import org.mitre.tdp.boogie.dafif.model.DafifRunway;
import org.mitre.tdp.boogie.dafif.model.DafifWaypoint;
import org.mitre.tdp.boogie.dafif.utils.DafifMagVars;

import com.google.common.annotations.Beta;

/**
 * This class converts DAFIF objects to standard boogie froms or a user can define a strategy.
 * @param <F> the output class
 */
@Beta
public interface FixAssemblyStrategy<F> {

  static FixAssemblyStrategy<Fix> standard(DafifTerminalAreaDatabase terminalAreaDatabase, DafifFixDatabase fixDatabase) {
    return new Standard(terminalAreaDatabase, fixDatabase);
  }

  Collection<F> convertIls(DafifIls record);
  Collection<F> convertRunway(DafifRunway record);
  Collection<F> convertWaypoint(DafifWaypoint record);
  Collection<F> convertNavaid(DafifNavaid record);
  Collection<F> convertAirport(DafifAirport record);

  final class Standard implements FixAssemblyStrategy<Fix> {

    private final DafifTerminalAreaDatabase terminalAreaDatabase;
    private final DafifFixDatabase fixDatabase;

    private Standard(DafifTerminalAreaDatabase terminalAreaDatabase, DafifFixDatabase fixDatabase) {
      this.terminalAreaDatabase = terminalAreaDatabase;
      this.fixDatabase = fixDatabase;
    }

    @Override
    public Collection<Fix> convertIls(DafifIls ils) {
      Supplier<Instant> cycleDate = () -> AiracCycle.startDate(ils.cycleDate().toString().substring(2));
      MagneticVariation magneticVariation = Optional.of(ils)
          .flatMap(DafifIls::ilsSlaveVariation)
          .map(DafifMagVars::fromDynamic)
          .orElseGet(() -> MagneticVariation.from(ils.degreesLatitude().orElseThrow(IllegalArgumentException::new), ils.degreesLongitude().orElseThrow(IllegalArgumentException::new), cycleDate.get()));
      Fix fix = Fix.builder()
          .fixIdentifier(ils.ilsNavaidIdentifier().orElseThrow(IllegalArgumentException::new))
          .latLong(LatLong.of(ils.degreesLatitude().orElseThrow(IllegalArgumentException::new), ils.degreesLongitude().orElseThrow(IllegalArgumentException::new)))
          .magneticVariation(magneticVariation)
          .build();
      return List.of(fix);
    }

    @Override
    public Collection<Fix> convertRunway(DafifRunway runway) {
      LatLong lowLatLong = terminalAreaDatabase.addRunway(runway.airportIdentification(), runway.lowEndIdentifier())
          .filter(a -> a.lowEndDisplacedThresholdDegreesLatitude().isPresent() && a.lowEndDisplacedThresholdDegreesLongitude().isPresent())
          .map(a -> LatLong.of(a.lowEndDisplacedThresholdDegreesLatitude().get(), a.lowEndDisplacedThresholdDegreesLongitude().get()))
          .orElseGet(() -> Optional.of(runway)
              .filter(r -> r.lowEndDegreesLatitude().isPresent() && r.lowEndDegreesLongitude().isPresent())
              .map(r -> LatLong.of(r.lowEndDegreesLatitude().get(), r.lowEndDegreesLongitude().get()))
              .orElseThrow(() -> new IllegalArgumentException("Runway " + runway.lowEndIdentifier() + " has no low end latitude or longitude"))
          );
      Supplier<Instant> cycleDate = () ->AiracCycle.startDate(runway.cycleDate().toString().substring(2));
      MagneticVariation magvar = terminalAreaDatabase.airport(runway.airportIdentification())
          .flatMap(DafifAirport::magVarOfRecord)
          .map(DafifMagVars::fromRecord)
          .orElseGet(() -> MagneticVariation.from(lowLatLong.latitude(), lowLatLong.longitude(), cycleDate.get()));

      Fix lowEnd = Fix.builder()
          .fixIdentifier(runway.lowEndIdentifier())
          .latLong(lowLatLong)
          .magneticVariation(magvar)
          .build();

      LatLong highLatLong = terminalAreaDatabase.addRunway(runway.airportIdentification(), runway.highEndIdentifier())
          .filter(a -> a.highEndDisplacedThresholdDegreesLatitude().isPresent() && a.highEndDisplacedThresholdDegreesLongitude().isPresent())
          .map(a -> LatLong.of(a.highEndDisplacedThresholdDegreesLatitude().get(), a.highEndDisplacedThresholdDegreesLongitude().get()))
          .orElseGet(() -> Optional.of(runway)
              .filter(r -> r.highEndDegreesLatitude().isPresent() && r.highEndDegreesLongitude().isPresent())
              .map(r -> LatLong.of(r.highEndDegreesLatitude().get(), r.highEndDegreesLongitude().get()))
              .orElseThrow(() -> new IllegalArgumentException("Runway " + runway.highEndIdentifier() + " has no high end latitude or longitude"))
          );

      Fix highEnd = Fix.builder()
          .fixIdentifier(runway.highEndIdentifier())
          .latLong(highLatLong)
          .magneticVariation(magvar)
          .build();

      return List.of(lowEnd, highEnd);
    }

    @Override
    public Collection<Fix> convertWaypoint(DafifWaypoint waypoint) {
      Optional<Collection<Fix>> fromNavaid = fixDatabase.navaidFor(waypoint).map(this::convertNavaid);

      Supplier<LatLong> latLongSupplier = () -> Optional.of(waypoint)
          .filter(w -> w.degreesLatitude().isPresent() && w.degreesLongitude().isPresent())
          .map(w -> LatLong.of(w.degreesLatitude().get(), w.degreesLongitude().get()))
          .orElseThrow(() -> new IllegalArgumentException("Waypoint " + waypoint.waypointIdentifier() + " has no latitude or longitude"));

      Supplier<MagneticVariation> magneticVariationSupplier = () -> MagneticVariation.from(
          latLongSupplier.get().latitude(),
          latLongSupplier.get().longitude(),
          AiracCycle.startDate(waypoint.cycleDate().toString().substring(2))
      );

      return fromNavaid.orElseGet(() -> List.of(Fix.builder()
          .fixIdentifier(waypoint.waypointIdentifier())
          .latLong(latLongSupplier.get())
          .magneticVariation(magneticVariationSupplier.get())
          .build()));
    }

    @Override
    public Collection<Fix> convertNavaid(DafifNavaid navaid) {
      LatLong location = Optional.of(navaid)
          .filter(i -> i.dmeDegreesLatitude().isPresent() && i.dmeDegreesLongitude().isPresent())
          .map(i -> LatLong.of(i.dmeDegreesLatitude().get(), i.dmeDegreesLongitude().get()))
          .or(() -> Optional.of(navaid)
              .filter(n -> n.degreesLatitude().isPresent() && n.degreesLongitude().isPresent())
              .map(n -> LatLong.of(n.degreesLatitude().get(), n.degreesLongitude().get())))
          .orElseThrow(() -> new IllegalArgumentException("Navaid " + navaid.name() + " has no latitude or longitude"));

      Supplier<Instant> cycleDate = () -> AiracCycle.startDate(navaid.cycleDate().toString().substring(2));
      MagneticVariation magneticVariation = Optional.of(navaid)
          .flatMap(DafifNavaid::navaidSlavedVariation)
          .map(DafifMagVars::fromDynamic)
          .orElseGet(() -> MagneticVariation.from(location.latitude(), location.longitude(), cycleDate.get()));
      return List.of(Fix.builder()
          .fixIdentifier(navaid.navaidIdentifier())
          .latLong(location)
          .magneticVariation(magneticVariation)
          .build());
    }

    @Override
    public Collection<Fix> convertAirport(DafifAirport airport) {
      LatLong location = LatLong.of(airport.degreesLatitude().orElseThrow(IllegalArgumentException::new), airport.degreesLongitude().orElseThrow(IllegalArgumentException::new));

      String ident = Optional.of(airport).map(DafifAirport::icaoCode).filter(i -> i.length() > 2)
          .or(airport::faaHostCountryIdentifier)
          .orElseThrow(() -> new IllegalArgumentException("Airport " + airport.name() + " has no icaoCode"));

      MagneticVariation magneticVariation = airport.magVarOfRecord().map(DafifMagVars::fromRecord)
          .or(() -> Optional.of(MagneticVariation.from(location.latitude(), location.longitude(), AiracCycle.startDate(airport.cycleDate().toString().substring(2)))))
          .orElseThrow(IllegalArgumentException::new);

      Fix fix = Fix.builder()
          .fixIdentifier(ident)
          .latLong(location)
          .magneticVariation(magneticVariation)
          .build();

      return List.of(fix);
    }
  }
}
