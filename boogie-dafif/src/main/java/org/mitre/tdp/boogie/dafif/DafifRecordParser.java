package org.mitre.tdp.boogie.dafif;

import com.google.common.collect.ImmutableList;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifAddRuwaySpec;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifAirportSpec;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifAtsSpec;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifIlsSpec;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifNavaidSpec;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifRunwaySpec;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifTerminalParentSpec;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifTerminalSegmentSpec;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifWaypointSpec;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface DafifRecordParser {

  static DafifRecordParser standard(DafifRecordSpec... recordSpecs) {

    return standard(List.of(recordSpecs));
  }

  static DafifRecordParser all() {
    return standard(
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
  }

  static DafifRecordParser standard(List<DafifRecordSpec> recordSpecs) {
    return new Standard(recordSpecs);
  }

  Optional<DafifRecord> parse(DafifRecordType recordType, String rawRecord);

  final class Standard implements DafifRecordParser {

    private final List<DafifRecordSpec> recordSpecs;

    private Standard(List<DafifRecordSpec> recordSpecs) {
      this.recordSpecs = ImmutableList.copyOf(recordSpecs);
      this.recordSpecs.forEach(RecordSpecValidator.INSTANCE);
    }

    @Override
    public Optional<DafifRecord> parse(DafifRecordType recordType, String rawRecord) {
      requireNonNull(rawRecord, "Supplied Dafif record should be non-null.");

      Optional<DafifRecordSpec> recordSpec = recordSpecs.stream().filter(spec -> spec.recordType().equals(recordType)).findFirst();

      return recordSpec.map(spec -> createParsedRecord(rawRecord, spec, recordType));
    }

    DafifRecord createParsedRecord(String rawRecord, DafifRecordSpec recordSpec, DafifRecordType recordType) {
      LinkedHashMap<String, Pair<DafifFieldSpec<?>, String>> namedData = new LinkedHashMap<>();

      int i = 0;

      List<String> rawFields = List.of(rawRecord.replaceAll("\n", "").split("\t"));

      while (i < recordSpec.recordFields().size()) {

        DafifRecordField<?> field = recordSpec.recordFields().get(i);

        String value = i < rawFields.size() ? rawFields.get(i) : "";

        if(!Pattern.compile(field.fieldSpec().regex()).matcher(value).matches()) {
          throw new RuntimeException("Unexpected value for " + field.fieldName() + " inside " + recordType.toString() + ". Does not match regex: " + value);
        }

        namedData.put(field.fieldName(), Pair.of(field.fieldSpec(), value));

        i++;
      }

      return new DafifRecord(namedData, recordType);
    }
  }
}
