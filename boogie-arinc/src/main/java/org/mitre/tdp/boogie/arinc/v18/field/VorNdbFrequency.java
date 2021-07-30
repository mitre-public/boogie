package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ArincDecimalParser;

/**
 * The “VOR/NDB Frequency” field specifies the frequency of the NAVAID identified in the “VOR/NDB Identifier” field of the record.
 * <br>
 * Frequencies are derived from official government sources. VHF NAVAID frequencies contain characters for hundreds, tens, units,
 * tenths and hundredths of megahertz. NDB frequencies contain characters for thousands, hundreds, tens, units and tenths of
 * kilohertz. The decimal point following the unit entry is suppressed in both cases.
 * <br>
 * e.g.
 * VHF - 11630, 11795 (MHz)
 * NDB - 03620, 17040 (KHz)
 */
public final class VorNdbFrequency implements FieldSpec<Double> {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.34";
  }

  @Override
  public Optional<Double> apply(String fieldValue) {
    return Optional.of(fieldValue).map(String::trim).flatMap(ArincDecimalParser.INSTANCE::parseDoubleWithTenths);
  }
}
