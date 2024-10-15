package org.mitre.tdp.boogie.dafif;

import java.util.List;
import java.util.Optional;

import org.mitre.tdp.boogie.dafif.v81.spec.DafifAddRuwaySpec;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifAirportSpec;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifAtsSpec;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifIlsSpec;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifNavaidSpec;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifRunwaySpec;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifTerminalParentSpec;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifTerminalSegmentSpec;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifWaypointSpec;

import com.google.common.collect.ImmutableMap;

public enum DafifVersion {
  V81(
      new DafifAirportSpec(),
      new DafifRunwaySpec(),
      new DafifIlsSpec(),
      new DafifNavaidSpec(),
      new DafifTerminalParentSpec(),
      new DafifTerminalSegmentSpec(),
      new DafifWaypointSpec(),
      new DafifAtsSpec(),
      new DafifAddRuwaySpec()
  );

  private static final ImmutableMap<String, DafifVersion> LOOKUP = ImmutableMap.of(
      V81.name(), V81
  );

  private final List<DafifRecordSpec> specs;

  DafifVersion(DafifRecordSpec... specs) {
    this.specs = List.of(specs);
  }

  public List<DafifRecordSpec> specs() {
    return specs;
  }

  public static Optional<DafifVersion> parse(String version) {
    return Optional.ofNullable(version).map(String::toUpperCase).map(LOOKUP::get);
  }
}
