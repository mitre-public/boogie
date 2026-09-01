package org.mitre.tdp.boogie.arinc;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Helipad;
import org.mitre.tdp.boogie.Heliport;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.Runway;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.arinc.model.ArincHeaderOne;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

/**
 * End-to-end benchmark of reading, converting, and assembling an ARINC file with the OneShot parser.
 */
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 2)
@Measurement(iterations = 5)
@Fork(value = 2, jvmArgsAppend = {"-Xms3g", "-Xmx3g"})
@Threads(1)
@State(Scope.Benchmark)
public class OneshotRecordParserBenchmark {

  private static final Method CONTROLLED_AIRSPACES_METHOD = controlledAirspacesMethod();

  @Param({"CIFP", "LIDO"})
  public Dataset dataset;

  @Param({"FULL", "NAV"})
  public SpecSet specSet;

  private Path fixture;
  private OneshotRecordParser<Airport, Runway, Fix, Leg, Transition, Airway, Procedure, Airspace,
      AirspaceSequence, Helipad, Heliport> parser;

  @Setup(Level.Trial)
  public void locateFixture() {
    fixture = dataset.fixture();
    if (!Files.isRegularFile(fixture)) {
      throw new IllegalStateException(
          "Missing " + dataset + " benchmark fixture at " + fixture
              + ". Set " + dataset.environmentVariable() + " to its .gz file."
      );
    }
    parser = OneshotRecordParser.standard(dataset.version(specSet));
  }

  @Benchmark
  public OneshotRecordParser.ClientRecords<Airport, Fix, Airway, Procedure, Airspace, Heliport> assemble() throws IOException {
    try (InputStream input = new GZIPInputStream(Files.newInputStream(fixture))) {
      OneshotRecordParser.ClientRecords<Airport, Fix, Airway, Procedure, Airspace, Heliport> records =
          parser.assembleFrom(input);
      dataset.verify(specSet, records);
      return records;
    }
  }

  public enum SpecSet {
    FULL,
    NAV
  }

  public enum Dataset {
    CIFP(
        ArincVersion.V19,
        ArincVersion.V19_NAV,
        "BOOGIE_CIFP_BENCHMARK_FILE",
        "boogie-arinc/src/test/resources/cifp-2101.dat.gz",
        new ExpectedCounts(
            13_779,
            67_922,
            1_550,
            14_262,
            new AirspaceCounts(0, 1_258, 1_518),
            new AirspaceCounts(0, 0, 0),
            6_466,
            "FAACIFP18"
        )
    ),
    LIDO(
        ArincVersion.V22,
        ArincVersion.V22_NAV,
        "BOOGIE_LIDO_BENCHMARK_FILE",
        "boogie-arinc/src/test/resources/A424-22std.dat.gz",
        new ExpectedCounts(
            26_960,
            270_393,
            14_588,
            101_085,
            new AirspaceCounts(357, 13_232, 20_503),
            new AirspaceCounts(0, 0, 0),
            9_646,
            "A424-22std.dat"
        )
    );

    private final ArincVersion fullVersion;
    private final ArincVersion navVersion;
    private final String environmentVariable;
    private final String defaultFixture;
    private final ExpectedCounts expectedCounts;

    Dataset(ArincVersion fullVersion, ArincVersion navVersion, String environmentVariable, String defaultFixture,
        ExpectedCounts expectedCounts) {
      this.fullVersion = fullVersion;
      this.navVersion = navVersion;
      this.environmentVariable = environmentVariable;
      this.defaultFixture = defaultFixture;
      this.expectedCounts = expectedCounts;
    }

    private ArincVersion version(SpecSet specSet) {
      return specSet == SpecSet.FULL ? fullVersion : navVersion;
    }

    private String environmentVariable() {
      return environmentVariable;
    }

    private Path fixture() {
      String configuredFixture = System.getenv(environmentVariable);
      return Path.of(configuredFixture == null || configuredFixture.isBlank() ? defaultFixture : configuredFixture)
          .toAbsolutePath();
    }

    private void verify(SpecSet specSet,
        OneshotRecordParser.ClientRecords<Airport, Fix, Airway, Procedure, Airspace, Heliport> records) {
      expectedCounts.verify(this, specSet, records);
    }
  }

  private record ExpectedCounts(
      int airports,
      int fixes,
      int airways,
      int procedures,
      AirspaceCounts fullAirspaces,
      AirspaceCounts navAirspaces,
      int heliports,
      String headerFileName) {

    private void verify(
        Dataset dataset,
        SpecSet specSet,
        OneshotRecordParser.ClientRecords<Airport, Fix, Airway, Procedure, Airspace, Heliport> records) {
      AirspaceCounts expectedAirspaces = specSet == SpecSet.FULL ? fullAirspaces : navAirspaces;
      String actualHeader = records.headerOne()
          .flatMap(ArincHeaderOne::fileName)
          .orElse("");
      boolean valid = airports == records.airports().size()
          && fixes == records.fixes().size()
          && airways == records.airways().size()
          && procedures == records.procedures().size()
          && expectedAirspaces.firUirs() == records.firUirs().size()
          && expectedAirspaces.controlled() == controlledAirspaceCount(records)
          && expectedAirspaces.restrictive() == records.restrictiveAirspaces().size()
          && heliports == records.heliports().size()
          && headerFileName.equals(actualHeader);

      if (!valid) {
        throw new IllegalStateException(
            dataset + "/" + specSet + " benchmark output changed: airports=" + records.airports().size()
                + ", fixes=" + records.fixes().size()
                + ", airways=" + records.airways().size()
                + ", procedures=" + records.procedures().size()
                + ", FIR/UIRs=" + records.firUirs().size()
                + ", controlled=" + controlledAirspaceCount(records)
                + ", restrictive=" + records.restrictiveAirspaces().size()
                + ", heliports=" + records.heliports().size()
                + ", header=" + actualHeader
        );
      }
    }
  }

  private static Method controlledAirspacesMethod() {
    try {
      return OneshotRecordParser.ClientRecords.class.getMethod("controlledAirspaces");
    } catch (NoSuchMethodException correctedNameMissing) {
      try {
        // The base revision predates the spelling correction. Keeping one benchmark source for both revisions makes the
        // base-versus-candidate CI comparison measure production changes rather than different benchmark harnesses.
        return OneshotRecordParser.ClientRecords.class.getMethod("conrolledAirspaces");
      } catch (NoSuchMethodException legacyNameMissing) {
        legacyNameMissing.addSuppressed(correctedNameMissing);
        throw new IllegalStateException("ClientRecords exposes no controlled-airspace collection", legacyNameMissing);
      }
    }
  }

  private static int controlledAirspaceCount(
      OneshotRecordParser.ClientRecords<Airport, Fix, Airway, Procedure, Airspace, Heliport> records) {
    try {
      return ((Collection<?>) CONTROLLED_AIRSPACES_METHOD.invoke(records)).size();
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new IllegalStateException("Could not read the controlled-airspace collection", e);
    }
  }

  private record AirspaceCounts(int firUirs, int controlled, int restrictive) {}
}
