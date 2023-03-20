package org.mitre.tdp.boogie.contract.arinc;

import java.util.Optional;

import org.immutables.value.Value;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableArincVhfNavaid.class)
@JsonDeserialize(as = ImmutableArincVhfNavaid.class)
public interface ArincVhfNavaid {
  RecordType recordType();

  Optional<CustomerAreaCode> customerAreaCode();

  SectionCode sectionCode();

  Optional<String> subSectionCode();

  Optional<String> airportIdentifier();

  Optional<String> airportIcaoRegion();

  String vhfIdentifier();

  String vhfIcaoRegion();

  Optional<String> continuationRecordNumber();

  Optional<Double> vhfFrequency();

  Optional<String> navaidClass();

  double latitude();

  double longitude();

  Optional<String> dmeIdentifier();

  Optional<Double> dmeLatitude();

  Optional<Double> dmeLongitude();

  Optional<Double> stationDeclination();

  Optional<Double> dmeElevation();

  Optional<Integer> figureOfMerit();

  Optional<Double> ilsDmeBias();

  Optional<Double> frequencyProtectionDistance();

  Optional<String> datumCode();

  Optional<String> vhfNavaidName();

  Integer fileRecordNumber();

  String lastUpdateCycle();
}
