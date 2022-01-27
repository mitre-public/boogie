package org.mitre.tdp.boogie.arinc;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.caasd.commons.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

/**
 * Functional parser class for ARINC-424 records. The parser is configured against a collection of provided {@link RecordSpec}s
 * which together work to extract structured content from 424 record strings.
 * <br>
 * This class explicitly <i>does not</i> expect to have a provided {@link RecordSpec} to match against each record type that it
 * may encounter within an ARINC 424 file - this is because there are a lot of record types the typical user wont care about...
 * and therefore it's nice to have the parser be a bit flexible.
 * <br>
 * This class also explicitly doesn't care whether the same spec is applied across multiple record types or if multiple of the
 * provided specs potentially match to the same record type (which could happen if you want to delegate to different specs based
 * on some feature other than section/subsection).
 * <br>
 * If users would like to perform some (rudimentary) spec conflict-checking they can use the {@link ParserSpecValidator} - which
 * uses some sample data for at least a couple of the spec types to do some simple checking for overlapping spec definitions.
 */
public final class ArincRecordParser implements Function<String, Optional<ArincRecord>> {

  private static final Logger LOG = LoggerFactory.getLogger(ArincRecordParser.class);

  /**
   * The collection of valid {@link RecordSpec}s which have been provided to the parser instance. These will be applied to the
   * incoming ARINC-424 record strings and used to return structured content.
   */
  private final List<RecordSpec> recordSpecs;

  public ArincRecordParser(RecordSpec... recordSpecs) {
    this(ImmutableList.copyOf(recordSpecs));
  }

  public ArincRecordParser(List<RecordSpec> recordSpecs) {
    checkArgument(!recordSpecs.isEmpty(), "The parse requires provided specs to run and generate output...");
    this.recordSpecs = ImmutableList.copyOf(recordSpecs);
    this.recordSpecs.forEach(RecordSpecValidator.INSTANCE);
  }

  @Override
  public Optional<ArincRecord> apply(String rawRecord) {
    requireNonNull(rawRecord, "Supplied ARINC-424 record should be non-null.");

    // at the expense of more operations... how strongly do we want to enforce none of our specs both match the same record...
    Optional<RecordSpec> recordSpec = recordSpecs.stream().filter(rspec -> rspec.matchesRecord(rawRecord)).findFirst();

    return recordSpec.map(spec -> createParsedRecord(rawRecord, spec));
  }

  /**
   * Creates a new fully parsed {@link ArincRecord} based on the provided {@link RecordSpec}.
   * <br>
   * This method leverages the ordering of the declared fields within the record spec and the length of those fields to extract
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
