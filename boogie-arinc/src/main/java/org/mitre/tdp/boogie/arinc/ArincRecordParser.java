package org.mitre.tdp.boogie.arinc;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;


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
   *
   * <p>Additionally this implementation (by design) doesn't care whether the same spec is applied across multiple record types or
   * if multiple of the provided specs potentially match to the same record type (which could happen if you want to delegate to
   * different specs based on some feature other than section/subsection).
   *
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

    private Standard(List<RecordSpec> recordSpecs) {
      List<RecordSpec> specs = List.copyOf(recordSpecs);
      specs.forEach(RecordSpecValidator.INSTANCE);
      this.recordSpecs = specs.stream().map(CompiledRecordSpec::new).toList();
    }

    @Override
    public Optional<ArincRecord> parse(String rawRecord) {
      requireNonNull(rawRecord, "Supplied ARINC-424 record should be non-null.");
      //performance optimized loop
      for (int i = 0; i < recordSpecs.size(); i++) {
        CompiledRecordSpec recordSpec = recordSpecs.get(i);
        if (recordSpec.matchesRecord(rawRecord)) {
          return Optional.of(new ArincRecord(rawRecord, recordSpec.fieldLayout()));
        }
      }

      return Optional.empty();
    }

    /**
     * Creates a new lazily decoded {@link ArincRecord} based on the provided {@link RecordSpec}.
     *
     * <p>This method leverages the ordering and length of the declared fields to create an indexed layout. Substrings are extracted
     * only when the corresponding field is requested from the final {@link ArincRecord}.
     */
    ArincRecord createParsedRecord(String rawRecord, RecordSpec recordSpec) {
      return new ArincRecord(rawRecord, ArincRecord.FieldLayout.from(recordSpec.recordFields()));
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
  }
}
