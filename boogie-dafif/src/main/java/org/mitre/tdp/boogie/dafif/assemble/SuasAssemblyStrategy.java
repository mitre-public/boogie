package org.mitre.tdp.boogie.dafif.assemble;

import java.util.List;

import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.AirspaceType;
import org.mitre.tdp.boogie.dafif.model.DafifSuasParent;
import org.mitre.tdp.boogie.dafif.model.DafifSuasSegment;

/**
 * Strategy for converting DAFIF special use airspaces and their ordered segments.
 * The standard strategy uses the ICAO code as the area namespace and appends a nonempty sector to
 * the identifier with a slash. All special use airspace types become RESTRICTIVE.
 * Generalized (G) boundaries use great-circle edges between their supplied approximation points.
 */
public interface SuasAssemblyStrategy<A, S> {

  static SuasAssemblyStrategy<Airspace, AirspaceSequence> standard() {
    return new Standard();
  }

  A convertSuas(DafifSuasParent parent, List<S> sequences);

  /**
   * Receives all source endpoints, including shapes unsupported by the standard Airspace model.
   */
  List<S> convertSuasSequences(List<DafifSuasSegment> segments);

  final class Standard implements SuasAssemblyStrategy<Airspace, AirspaceSequence> {

    private Standard() {
    }

    @Override
    public Airspace convertSuas(DafifSuasParent parent, List<AirspaceSequence> sequences) {
      return Airspace.builder()
          .area(parent.icaoCode())
          .identifier(parent.suasIdentification() + parent.sector().filter(sector -> !sector.isEmpty()).map(sector -> "/" + sector).orElse(""))
          .airspaceType(AirspaceType.RESTRICTIVE)
          .altitudeLimit(AirspaceAltitudeRange.INSTANCE.apply(parent.lowerAltitude(), parent.upperAltitude()))
          .sequences(sequences)
          .build();
    }

    @Override
    public List<AirspaceSequence> convertSuasSequences(List<DafifSuasSegment> segments) {
      return AirspaceGeometry.convert(segments.stream().map(AirspaceGeometry::from).toList());
    }
  }
}
