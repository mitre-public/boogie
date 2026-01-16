package org.mitre.tdp.boogie.conformance.alg.assign;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.mitre.tdp.boogie.ConformablePoint;

public class TrackParser {
  public static List<TrackPoint> parse(String path) {
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
      return br.lines()
          .skip(1)
          .map(l -> l.split(","))
          .map(a -> {
            String key = a[1];
            Instant time = Instant.ofEpochSecond(Long.parseLong(a[2]));
            Double lat = Double.parseDouble(a[3]);
            Double lon = Double.parseDouble(a[4]);
            Double alt = Optional.ofNullable(a[5]).filter(i -> !i.isEmpty()).map(Double::parseDouble).orElse(null);
            Double course = Optional.ofNullable(a[6]).filter(i -> !i.isEmpty()).map(Double::parseDouble).orElse(null);
            Double speed = Optional.ofNullable(a[7]).filter(i -> !i.isEmpty()).map(Double::parseDouble).orElse(null);
            Double climb = a.length > 8 ? Double.parseDouble(a[8]) : null;
            return TrackPoint.from(key, time, lat, lon, alt, course, speed, climb);
          })
          .sorted(Comparator.comparing(ConformablePoint::time))
          .toList();
    } catch (Exception io) {
      return Collections.emptyList();
    }
  }

}
