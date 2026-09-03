package org.mitre.tdp.boogie.arinc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * Interface for 424 parser implementations which return generic {@link ArincRecord} objects providing named access to the fields
 * of each supported 424 record type.
 *
 * <p>Parsers are allowed to optionally return when given a record. Parsers should not be required to support all record types and
 * clients should be able to pick and choose what record types they want to extract.
 */
@FunctionalInterface
public interface ArincRecordParser {

  /**
   * Standard parser implementation for 424 records. The parser is configured with a collection of provided {@link RecordSpec}s
   * that together define the collection of record types that will be extracted in the parsing.
   * <p>
   * Additionally this implementation (by design) allows multiple provided specs to match the same record. In that case, the
   * first matching spec in the supplied order is applied.
   * <p>
   * All configured specs must declare the same fixed record length. A parser supports between 1 and 64 specs. Inputs shorter
   * than the configured length are rejected; longer inputs are accepted only when the remaining characters are ASCII spaces and
   * are normalized to the configured length.
   * @param recordSpecs the collection of {@link RecordSpec}s to support for parsing, standard collections of specs tied to 424
   *                    versions can be found in {@link ArincVersion}
   */
  static ArincRecordParser standard(RecordSpec... recordSpecs) {
    return standard(List.of(recordSpecs));
  }

  /**
   * Alternative entrypoint for creating a parser implementation, see full documentation on {@link #standard(RecordSpec...)}.
   */
  static ArincRecordParser standard(List<RecordSpec> recordSpecs) {
    return new Standard(recordSpecs);
  }

  /**
   * Converts an incoming 424 record string (representing a single line from a 424 file) to a generic {@link ArincRecord} given
   * the parser supports extraction of records of that type.
   *
   * <p>The return is optional to allow some input records to be ignored by parser implementations (e.g. if a client only cares
   * about a subset of record types).
   */
  Optional<ArincRecord> parse(String rawRecord);

  final class Standard implements ArincRecordParser {

    private final List<CompiledRecordSpec> recordSpecs;
    private final RecordSpecDispatch dispatch;
    private final int recordLength;

    private Standard(List<RecordSpec> recordSpecs) {
      List<RecordSpec> specs = List.copyOf(recordSpecs);
      checkArgument(!specs.isEmpty(), "A parser requires at least one record specification");
      checkArgument(specs.size() <= Long.SIZE, "A parser supports at most 64 record specifications");
      specs.forEach(RecordSpecValidator.INSTANCE);
      this.recordLength = specs.get(0).recordLength();
      checkArgument(specs.stream().allMatch(spec -> spec.recordLength() == recordLength), "All record specifications in a parser must use the same record length");
      this.recordSpecs = specs.stream().map(CompiledRecordSpec::new).toList();
      this.dispatch = new RecordSpecDispatch(specs);
    }

    @Override
    public Optional<ArincRecord> parse(String rawRecord) {
      requireNonNull(rawRecord, "Supplied ARINC-424 record should be non-null.");
      String normalizedRecord = normalizeRecordLength(rawRecord);
      return parseCandidates(normalizedRecord, dispatch.candidates(normalizedRecord));
    }

    private String normalizeRecordLength(String rawRecord) {
      int actualLength = rawRecord.length();
      if (actualLength == recordLength) {
        return rawRecord;
      }

      boolean containsOnlyTrailingSpaces = actualLength > recordLength;
      for (int index = recordLength; containsOnlyTrailingSpaces && index < actualLength; index++) {
        containsOnlyTrailingSpaces = rawRecord.charAt(index) == ' ';
      }

      checkArgument(
          containsOnlyTrailingSpaces,
          "Expected an ARINC-424 record of length %s, optionally followed by ASCII spaces, but received %s record: %s",
          recordLength,
          actualLength,
          rawRecord
      );
      return rawRecord.substring(0, recordLength);
    }

    private Optional<ArincRecord> parseCandidates(String rawRecord, long candidates) {
      // Clearing the lowest set bit after each iteration walks candidates by their original list index. This preserves the
      // caller's first-matching-spec-wins order without scanning specs whose discriminators cannot match this record.
      for (long remaining = candidates; remaining != 0; remaining &= remaining - 1) {
        CompiledRecordSpec recordSpec = recordSpecs.get(Long.numberOfTrailingZeros(remaining));
        if (recordSpec.matchesRecord(rawRecord)) {
          return Optional.of(new ArincRecord(rawRecord, recordSpec.fieldLayout()));
        }
      }

      return Optional.empty();
    }

    private static final class CompiledRecordSpec {

      private final RecordSpec recordSpec;
      private final ArincRecord.FieldLayout fieldLayout;

      private CompiledRecordSpec(RecordSpec recordSpec) {
        this.recordSpec = recordSpec;
        this.fieldLayout = ArincRecord.FieldLayout.from(recordSpec.recordFields());
      }

      private boolean matchesRecord(String rawRecord) {
        return recordSpec.matchesRecord(rawRecord);
      }

      private ArincRecord.FieldLayout fieldLayout() {
        return fieldLayout;
      }
    }

    /**
     * Precomputes a coarse index from each discriminator to a 64-bit set of record specifications. Bit {@code n} always
     * represents {@code recordSpecs.get(n)}. For example, if specs 0 and 3 both declare the same address, that address maps to
     * {@code 0b1001}.
     *
     * <p>A lookup combines the masks for the record's column-6 address, column-13 address, and matching prefixes. Combining both
     * address columns is important because a record can satisfy a discriminator at each location. OR-ing the masks also ensures
     * a spec selected by more than one discriminator is evaluated only once.
     *
     * <p>The two address indexes are sparse ASCII tables. The outer index is the section code from column 5, and a row of
     * subsection codes is allocated only for sections used by the configured specs. Prefixes are stored separately because they
     * can have different lengths. The resulting mask is only a candidate set: {@link RecordSpec#matchesRecord(String)} still
     * makes the final decision in original spec order.
     */
    private static final class RecordSpecDispatch {

      private static final int ASCII_CHARACTER_COUNT = 128;

      private final long[][] column6Candidates = new long[ASCII_CHARACTER_COUNT][];
      private final long[][] column13Candidates = new long[ASCII_CHARACTER_COUNT][];
      private final PrefixCandidates[] prefixCandidates;

      private RecordSpecDispatch(List<RecordSpec> recordSpecs) {
        Map<String, Long> candidatesByPrefix = new LinkedHashMap<>();

        for (int index = 0; index < recordSpecs.size(); index++) {
          // The spec's configured position is its bit under every discriminator it declares.
          long candidate = 1L << index;
          for (RecordDiscriminator discriminator : recordSpecs.get(index).recordDiscriminators()) {
            if (discriminator instanceof RecordDiscriminator.SectionSubsection sectionSubsection) {
              long[][] candidates = sectionSubsection.subsectionColumn() == RecordDiscriminator.SubsectionColumn.COLUMN_6 ? column6Candidates : column13Candidates;
              addCandidate(candidates, sectionSubsection.sectionCode(), sectionSubsection.subsectionCode(), candidate);
            } else if (discriminator instanceof RecordDiscriminator.Prefix prefix) {
              candidatesByPrefix.merge(prefix.value(), candidate, (left, right) -> left | right);
            } else {
              throw new IllegalStateException("Unsupported record discriminator: " + discriminator.getClass().getName());
            }
          }
        }

        this.prefixCandidates = candidatesByPrefix.entrySet().stream()
            .map(entry -> new PrefixCandidates(entry.getKey(), entry.getValue()))
            .toArray(PrefixCandidates[]::new);
      }

      private long candidates(String rawRecord) {
        // Discriminators are alternatives, so every applicable source contributes to one mask. OR also deduplicates specs
        // selected through multiple alternatives.
        long candidates = 0;

        // Custom fixed-width specs may be too short to contain one or both standard ARINC address columns.
        if (rawRecord.length() > 5) {
          candidates |= candidatesAt(column6Candidates, rawRecord.charAt(4), rawRecord.charAt(5));
        }
        if (rawRecord.length() > 12) {
          candidates |= candidatesAt(column13Candidates, rawRecord.charAt(4), rawRecord.charAt(12));
        }

        for (PrefixCandidates prefix : prefixCandidates) {
          if (rawRecord.startsWith(prefix.value())) {
            candidates |= prefix.candidates();
          }
        }
        return candidates;
      }

      private static void addCandidate(long[][] candidates, char sectionCode, char subsectionCode, long candidate) {
        long[] sectionCandidates = candidates[sectionCode];
        if (sectionCandidates == null) {
          // Most ASCII section codes are unused, so allocate a subsection row only when a configured spec needs it.
          sectionCandidates = new long[ASCII_CHARACTER_COUNT];
          candidates[sectionCode] = sectionCandidates;
        }
        sectionCandidates[subsectionCode] |= candidate;
      }

      private static long candidatesAt(long[][] candidates, char sectionCode, char subsectionCode) {
        // Discriminator metadata is ASCII-validated, but an input record can still contain non-ASCII characters.
        if (sectionCode >= ASCII_CHARACTER_COUNT || subsectionCode >= ASCII_CHARACTER_COUNT) {
          return 0;
        }
        long[] sectionCandidates = candidates[sectionCode];
        return sectionCandidates == null ? 0 : sectionCandidates[subsectionCode];
      }

      private record PrefixCandidates(String value, long candidates) {
      }
    }
  }
}
