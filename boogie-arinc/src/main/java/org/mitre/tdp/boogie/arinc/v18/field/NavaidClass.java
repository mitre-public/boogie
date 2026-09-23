package org.mitre.tdp.boogie.arinc.v18.field;

import static java.util.Objects.checkFromToIndex;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * Navaid class information. This is a 5 character code with each column mapping to a different meaning.
 * <br>
 * <b>Column 1: Navaid Type 1</b>
 * V - VOR
 * H - NDB
 * S - SABH
 * M - Marine beacon
 * <br>
 * <b>Column 2: Navaid Type 2</b>
 * D - DME
 * T - TACAN (channels 17-59 and 70-126)
 * M - MIL (Military) TACAN (channels 1-16 and 60-69) - VHF
 * --- Middle Marker - NDB
 * I - ILS/DME, ILS/TACAN - VHF
 * --- Inner Marker - NDB
 * N - MLS/DME/N
 * P - MLS/DME/P
 * O - Outer Marker
 * C - Back Marker
 * <br>
 * <b>Column 3: Range/Power</b>
 * Blank - NDB, generally usable within 50nm of the facility at all altitudes
 * M - Low-Powered NDB, generally usable within 25nm of the facility at all altitudes
 * T - Terminal, generally usable within 25nm of the facility and up to 18000ft
 * L - Low altitude, generally usable within 40nm of the facility and up to 18000ft - VHF
 * --- Locator, generally usable within 15nm of the facility at all altitudes - NDB
 * H - High altitude, generally usable within 130nm of the facility and up to 60000ft - VHF
 * --- High-Powered NDB, generally usable within 75nm of the facility at all altitudes - NDB
 * U - Undefined
 * C - ILS/TACAN, Full TACAN facility frequency paired with and operating with the same name as an ILS localizer. Coverage is Terminal.
 * <br>
 * <b>Column 4: Additional Info</b>
 * D - Biased ILS/DME or ILS/TACAN, the zero range reading of the DME facility is not at the transmitting antennae site.
 * A - Automatic transcribed weather broadcast, navaid frequency is used for continuous broadcast of some sort of automated weather system such as AWOS, ASOS, TWEB, etc.
 * B - Scheduled weather broadcast, requency is used for scheduled non-continuous broadcast of some sort of automated weather system such as VOLMET.
 * W - No voice on frequency, frequency of this navaid is not used to support 2-way communication between ground and aircraft.
 * Blank - Voice on frequency, navaid frequency is used to support two-way communication between a ground station and an aircraft.
 * <br>
 * <b>Column 5: Collocation Information</b>
 * Blank - Collocated
 * N - Non-Collocated, Lat/Lon of VOR or localizer portion and the DME or TACAN portion of a VORDME, WORTAC, ILSDME, or ILSTACAN are not identical. - VHF
 * --- Non-Collocated, Lat/Lon of the locator and marker are not identical. - NDB
 * B - BFO operation, Use of beat frequency oscillator type of equipment is required to receive an aural identification signal.
 * A - Locator/Marker collocated, the Lat/Lon of the locator and marker are identical.
 */
public final class NavaidClass implements FieldSpec<String> {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.35";
  }

  @Override
  public Optional<String> parse(String source, int startOffset, int endOffset) {
    checkFromToIndex(startOffset, endOffset, source.length());
    if (endOffset - startOffset != fieldLength()) {
      return Optional.empty();
    }
    return Optional.of(new String(new char[] {
        inSetOrBlank(source.charAt(startOffset), allowedType1),
        inSetOrBlank(source.charAt(startOffset + 1), allowedType2),
        inSetOrBlank(source.charAt(startOffset + 2), allowedRangePower),
        inSetOrBlank(source.charAt(startOffset + 3), allowedAdditionalInfo),
        inSetOrBlank(source.charAt(startOffset + 4), allowedCollocation)
    }));
  }

  private static char inSetOrBlank(char value, String allowed) {
    return allowed.indexOf(value) >= 0 ? value : ' ';
  }

  private static final String allowedType1 = "VHSM";

  private static final String allowedType2 = "DTIMOCNP";

  private static final String allowedRangePower = "TUHMLC ";

  private static final String allowedAdditionalInfo = "DABW ";

  private static final String allowedCollocation = "BAN ";
}
