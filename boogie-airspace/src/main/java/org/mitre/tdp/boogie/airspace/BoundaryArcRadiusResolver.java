package org.mitre.tdp.boogie.airspace;

import static java.util.stream.Collectors.toUnmodifiableMap;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.Spherical;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.airspace.BoundaryArcRadiusIdentifier.Mismatch;

import com.google.common.geometry.S2;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2Point;

/**
 * Repairs inconsistent arc radii by moving the center while preserving both endpoint fixes.
 * Apply this opt-in policy to ordered source sequences before building an index with {@link AirspaceIndexFactory}.
 * Inputs must be in traversal order with unique sequence numbers, including across boundary closure.
 *
 * <p>For each selected mismatch, the mean of the two coordinate-derived radii becomes the shared
 * radius. Intersecting equal-radius spherical circles around the endpoints gives two candidate
 * centers (one when tangent); the candidate nearest the original center is chosen. Geometry,
 * sequence numbers, published arc parameters, and {@link AirspaceSequence.Record} source data are
 * preserved. Published radius and bearing fields are not used to calculate the replacement center.
 *
 * <p>All eligible selected arcs are repaired in one pass. Coincident or nearly antipodal endpoints
 * do not supply a stable pair of candidate centers and are left unchanged. Center correction does
 * not establish that an arc was the cause of a crossing; the factory still validates the polygon.
 * Source lists and records are never mutated.
 */
public final class BoundaryArcRadiusResolver extends IssueResolver<List<AirspaceSequence>, Mismatch> {
  /**
   * @param toleranceNm finite, nonnegative maximum accepted difference between endpoint radii, in nautical miles
   */
  public BoundaryArcRadiusResolver(double toleranceNm) {
    this(new BoundaryArcRadiusIdentifier(toleranceNm));
  }

  private BoundaryArcRadiusResolver(BoundaryArcRadiusIdentifier identifier) {
    super(identifier, (sequences, issues) -> repair(sequences, issues, identifier));
  }

  private static List<AirspaceSequence> repair(List<AirspaceSequence> sequences, List<Mismatch> issues, BoundaryArcRadiusIdentifier identifier) {
    Set<Mismatch> selected = Set.copyOf(issues);
    Map<Integer, AirspaceSequence> byNumber = sequences.stream()
        .collect(toUnmodifiableMap(AirspaceSequence::sequenceNumber, Function.identity()));
    Map<Integer, LatLong> centers = identifier.apply(sequences).stream()
        .filter(selected::contains)
        .flatMap(issue -> correctedCenter(byNumber.get(issue.arcSequenceNumber()), byNumber.get(issue.endingSequenceNumber()), issue)
            .map(center -> Map.entry(issue.arcSequenceNumber(), center)).stream())
        .collect(toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    return Optional.of(centers)
        .filter(replacements -> !replacements.isEmpty())
        .map(replacements -> sequences.stream()
            .map(sequence -> Optional.ofNullable(replacements.get(sequence.sequenceNumber()))
                .map(sequence::withCenterFix)
                .orElse(sequence))
            .toList())
        .orElse(sequences);
  }

  private static Optional<LatLong> correctedCenter(AirspaceSequence arc, AirspaceSequence next, Mismatch issue) {
    S2Point start = BoundaryCompiler.point(arc.associatedFix().orElseThrow());
    S2Point end = BoundaryCompiler.point(next.associatedFix().orElseThrow());
    S2Point original = BoundaryCompiler.point(arc.centerFix().orElseThrow());
    double angle = start.angle(end);
    double radius = (issue.startRadiusNm() + issue.endRadiusNm()) / (2.0 * Spherical.EARTH_RADIUS_NM);
    return Optional.of(angle)
        .filter(separation -> separation > 0.0 && Math.PI - separation > 1e-12)
        .flatMap(separation -> candidateCenters(start, end, separation / 2.0, radius)
            .min(Comparator.comparingDouble(original::angle)))
        .map(S2LatLng::new)
        .map(position -> LatLong.of(position.latDegrees(), position.lngDegrees()));
  }

  private static Stream<S2Point> candidateCenters(S2Point start, S2Point end, double halfAngle, double radius) {
    // The mean radius satisfies the spherical triangle bounds; clamp distance-rounding error at tangency.
    double boundedRadius = Math.max(halfAngle, Math.min(Math.PI - halfAngle, radius));
    double cosine = Math.cos(halfAngle);
    S2Point midpoint = start.add(end).normalize();
    S2Point baseline = midpoint.mul(Math.cos(boundedRadius) / cosine);
    // sin(r+h)*sin(r-h) avoids cancellation in cos(h)^2-cos(r)^2 for small or nearly tangent arcs.
    double height = Math.sqrt(Math.max(0.0, Math.sin(boundedRadius + halfAngle) * Math.sin(boundedRadius - halfAngle))) / cosine;
    S2Point offset = S2.robustCrossProd(start, end).normalize().mul(height);
    return Stream.of(baseline.add(offset).normalize(), baseline.sub(offset).normalize());
  }
}
