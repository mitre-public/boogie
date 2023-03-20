package org.mitre.tdp.boogie.contract.arinc;

import java.util.Optional;

import org.immutables.value.Value;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableArincNdbNavaid.class)
@JsonDeserialize(as = ImmutableArincNdbNavaid.class)
public interface ArincNdbNavaid {
  RecordType recordType();

  Optional<CustomerAreaCode> customerAreaCode();

  SectionCode sectionCode();

  Optional<String> subSectionCode();

  Optional<String> airportIdentifier();

  Optional<String> airportIcaoRegion();

  String ndbIdentifier();

  String ndbIcaoRegion();

  Optional<String> continuationRecordNumber();

  Optional<Double> ndbFrequency();

  Optional<String> navaidClass();

  double latitude();

  double longitude();

  Optional<Double> magneticVariation();

  Optional<String> datumCode();

  Optional<String> ndbNavaidName();

  int fileRecordNumber();

  String lastUpdateCycle();
}
