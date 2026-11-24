package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.mitre.tdp.boogie.AltitudeLimit;
import org.mitre.tdp.boogie.AltitudeReference;
import org.mitre.tdp.boogie.arinc.ArincRecord;

/**
 * Reads ARINC 424 airspace limits (5.121), with their unit indicators (5.133), as {@link AltitudeLimit}s.
 *
 * <ul>
 *   <li>{@code GND} is the surface: zero feet above ground level.</li>
 *   <li>{@code MSL} is mean sea level: zero feet above mean sea level.</li>
 *   <li>{@code UNLTD} means no limit on that side.</li>
 *   <li>{@code NOTAM} means the limit is set by NOTAM.</li>
 *   <li>{@code NOTSP} (not specified), a blank field, or anything unreadable means the limit is not specified.</li>
 *   <li>{@code FLnnn} is flight level nnn: a pressure altitude of nnn hundred feet.</li>
 *   <li>A number is in feet from the surface its unit indicator names: {@code A} above ground level, {@code M} above
 *       mean sea level. Without a readable indicator it is not specified, since the number alone cannot be placed.</li>
 * </ul>
 *
 * <p>FIR and UIR limits have no unit indicator; ARINC gives their numbers above mean sea level.
 */
public final class AirspaceAltitudeLimits {

  /**
   * * The limits written as words, by their text.
   */
  private static final Map<String, AltitudeLimit> WORDS = Map.of(
      "GND", AltitudeLimit.ground(),
      "MSL", AltitudeLimit.msl(0.0),
      "UNLTD", AltitudeLimit.none(),
      "NOTAM", AltitudeLimit.byNotam(),
      "NOTSP", AltitudeLimit.notSpecified());

  /**
   * * The references named by unit indicators.
   */
  private static final Map<String, AltitudeReference> UNIT_INDICATORS = Map.of(
      "A", AltitudeReference.AGL,
      "M", AltitudeReference.MSL);

  /**
   * * Matches a flight level, such as {@code FL245}.
   */
  private static final Predicate<String> FLIGHT_LEVEL = Pattern.compile("FL\\d+").asMatchPredicate();

  /**
   * * Matches a signed or unsigned whole number of feet, such as {@code 05000} or {@code -0200}.
   */
  private static final Predicate<String> FEET = Pattern.compile("[+-]?\\d+").asMatchPredicate();

  /**
   * Not instantiable: the methods are static.
   */
  private AirspaceAltitudeLimits() {}

  /**
   * Reads a controlled or restrictive airspace limit from a record.
   *
   * @param record the record
   * @param limitField the name of the limit field (5.121)
   * @param unitIndicatorField the name of the field holding the limit's unit indicator (5.133)
   * @return the limit, or {@link AltitudeLimit#notSpecified()} if the field is blank or a number cannot be placed
   */
  public static AltitudeLimit limit(ArincRecord record, String limitField, String unitIndicatorField) {
    Optional<AltitudeReference> feetReference = record.<String>optionalField(unitIndicatorField).flatMap(AirspaceAltitudeLimits::reference);
    return record.<String>optionalField(limitField)
        .map(AirspaceAltitudeLimits::normalized)
        .flatMap(value -> independentOfUnit(value).or(() -> feetReference.flatMap(reference -> feet(value, reference))))
        .orElseGet(AltitudeLimit::notSpecified);
  }

  /**
   * Reads a FIR or UIR limit from a record, whose numbers are above mean sea level.
   *
   * @param record the record
   * @param limitField the name of the limit field (5.121)
   * @return the limit, or {@link AltitudeLimit#notSpecified()} if the field is blank
   */
  public static AltitudeLimit firUirLimit(ArincRecord record, String limitField) {
    return record.<String>optionalField(limitField)
        .map(text -> parse(text, AltitudeReference.MSL))
        .orElseGet(AltitudeLimit::notSpecified);
  }

  /**
   * Reads a limit's text whose numbers are measured from a known reference.
   *
   * @param text the limit field's text
   * @param feetReference the surface or datum a number of feet is measured from
   * @return the limit, or {@link AltitudeLimit#notSpecified()} if the text cannot be read
   */
  public static AltitudeLimit parse(String text, AltitudeReference feetReference) {
    String value = normalized(text);
    return independentOfUnit(value)
        .or(() -> feet(value, feetReference))
        .orElseGet(AltitudeLimit::notSpecified);
  }

  /**
   * Reads a limit's text without a unit indicator, so a number of feet cannot be placed.
   *
   * @param text the limit field's text
   * @return the limit, or {@link AltitudeLimit#notSpecified()} if the text is a number or cannot be read
   */
  public static AltitudeLimit parse(String text) {
    return independentOfUnit(normalized(text)).orElseGet(AltitudeLimit::notSpecified);
  }

  /**
   * Reads a unit indicator.
   *
   * @param indicator the unit indicator field's text
   * @return the reference it names, or empty if it names none
   */
  public static Optional<AltitudeReference> reference(String indicator) {
    return Optional.ofNullable(UNIT_INDICATORS.get(normalized(indicator)));
  }

  /**
   * Trims and upper-cases a field's text.
   *
   * @param text the text
   * @return the normalized text
   */
  private static String normalized(String text) {
    return text.trim().toUpperCase(Locale.ROOT);
  }

  /**
   * Reads the limits whose meaning does not depend on a unit indicator: the words, and flight levels.
   *
   * @param value the normalized text
   * @return the limit, or empty if the text is neither
   */
  private static Optional<AltitudeLimit> independentOfUnit(String value) {
    return Optional.ofNullable(WORDS.get(value)).or(() -> flightLevel(value));
  }

  /**
   * Reads a flight level.
   *
   * @param value the normalized text
   * @return the flight level, or empty if the text is not one
   */
  private static Optional<AltitudeLimit> flightLevel(String value) {
    return Optional.of(value)
        .filter(FLIGHT_LEVEL)
        .map(level -> AltitudeLimit.flightLevel(Integer.parseInt(level.substring(2))));
  }

  /**
   * Reads a number of feet measured from a reference.
   *
   * @param value the normalized text
   * @param reference the surface or datum the number is measured from
   * @return the limit, or empty if the text is not a number
   */
  private static Optional<AltitudeLimit> feet(String value, AltitudeReference reference) {
    return Optional.of(value)
        .filter(FEET)
        .map(feet -> new AltitudeLimit.Height(Double.parseDouble(feet), reference));
  }
}
