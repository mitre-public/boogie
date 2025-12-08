package org.mitre.tdp.boogie.conformance.alg.assign;

import java.util.List;

public record Track(String key, List<TrackPoint> trackPoints) {
  public static Track from(String key, List<TrackPoint> trackPoints) {
    return new Track(key, trackPoints);
  }
}
