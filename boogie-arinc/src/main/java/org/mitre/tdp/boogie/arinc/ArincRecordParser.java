package org.mitre.tdp.boogie.arinc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import com.google.common.collect.ImmutableList;

import org.mitre.caasd.commons.Pair;

import static java.util.Objects.requireNonNull;


/**
 * Interface for 424 parser implementations which return generic {@link ArincRecord} objects (essentially maps of named features
 * per 424 record type).
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

    private final List<RecordSpec> recordSpecs;

    private Standard(List<RecordSpec> recordSpecs) {
      this.recordSpecs = ImmutableList.copyOf(recordSpecs);
      this.recordSpecs.forEach(RecordSpecValidator.INSTANCE);
    }

    @Override
    public Optional<ArincRecord> parse(String rawRecord) {
      requireNonNull(rawRecord, "Supplied ARINC-424 record should be non-null.");

      // at the expense of more operations... how strongly do we want to enforce none of our specs both match the same record...
      Optional<RecordSpec> recordSpec = recordSpecs.stream().filter(rspec -> rspec.matchesRecord(rawRecord)).findFirst();

      return recordSpec.map(spec -> createParsedRecord(rawRecord, spec));
    }

    /**
     * Creates a new fully parsed {@link ArincRecord} based on the provided {@link RecordSpec}.
     *
     * <p>This method leverages the ordering of the declared fields within the record spec and the length of those fields to extract
     * substrings from the input raw record string which will be associated with those field names in the final {@link ArincRecord}.
     */
    ArincRecord createParsedRecord(String rawRecord, RecordSpec recordSpec) {
      int capacity = recordSpec.recordFields().size();

      LinkedHashMap<String, Pair<FieldSpec<?>, String>> namedData = new LinkedHashMap<>(capacity);

      int i = 0;
      int offset = 0;

      while (i < recordSpec.recordFields().size()) {

        RecordField<?> field = recordSpec.recordFields().get(i);

        String value = rawRecord.substring(offset, offset + field.fieldSpec().fieldLength()).intern();
        namedData.put(field.fieldName(), Pair.of(field.fieldSpec(), value));

        i++;
        offset += field.fieldSpec().fieldLength();
      }

      return new ArincRecord(namedData);
    }
  }
}
