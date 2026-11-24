package org.mitre.tdp.boogie.dafif.assemble;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import org.mitre.tdp.boogie.AltitudeLimit;
import org.mitre.tdp.boogie.AltitudeLimits;
import org.mitre.tdp.boogie.AltitudeReference;

/**
 * Reads DAFIF airspace limits as {@link AltitudeLimits}.
 *
 * <ul>
 *   <li>{@code nnnnnAMSL} is feet above mean sea level, and {@code nnnnnAGL} feet above ground level.</li>
 *   <li>{@code FLnnn} is flight level nnn: a pressure altitude of nnn hundred feet.</li>
 *   <li>{@code GND} and {@code SURFACE} are the surface: zero feet above ground level.</li>
 *   <li>{@code UNLTD} means no limit on that side.</li>
 *   <li>{@code BY NOTAM} means the limit is set by NOTAM.</li>
 *   <li>{@code U} (unknown) and a blank field mean the limit is not specified.</li>
 * </ul>
 *
 * <p>Limits that contradict each other are not specified (see {@link AltitudeLimits#published}).
 */
public final class DafifAltitudeLimits implements BiFunction<String, String, AltitudeLimits> {

  /**
   * The shared, stateless instance.
   */
  public static final DafifAltitudeLimits INSTANCE = new DafifAltitudeLimits();

  /**
   * * The limits written as words, by their text.
   */
  private static final Map<String, AltitudeLimit> WORDS = Map.of(
      "GND", AltitudeLimit.ground(),
      "SURFACE", AltitudeLimit.ground(),
      "UNLTD", AltitudeLimit.none(),
      "BY NOTAM", AltitudeLimit.byNotam(),
      "U", AltitudeLimit.notSpecified(),
      "", AltitudeLimit.notSpecified());

  /**
   * * The references named by the suffix of a number of feet.
   */
  private static final Map<String, AltitudeReference> FEET_SUFFIXES = Map.of(
      "AMSL", AltitudeReference.MSL,
      "AGL", AltitudeReference.AGL);

  /**
   * * Matches a flight level, such as {@code FL180}, capturing its number.
   */
  private static final Pattern FLIGHT_LEVEL = Pattern.compile("FL(\\d+)");

  /**
   * * Matches a number of feet with its reference, such as {@code 00500AGL}, capturing the number and the suffix.
   */
  private static final Pattern FEET = Pattern.compile("(\\d+)(AMSL|AGL)");

  /**
   * Not instantiable outside this class: use {@link #INSTANCE}.
   */
  private DafifAltitudeLimits() {
  }

  /**
   * Reads an airspace's limits.
   *
   * @param lowerAltitude the DAFIF lower altitude, or null
   * @param upperAltitude the DAFIF upper altitude, or null
   * @return the limits
   * @throws IllegalArgumentException if a limit is not a DAFIF altitude
   */
  @Override
  public AltitudeLimits apply(@Nullable String lowerAltitude, @Nullable String upperAltitude) {
    return AltitudeLimits.published(limit(lowerAltitude), limit(upperAltitude));
  }

  /**
   * Reads one DAFIF altitude.
   *
   * @param raw the DAFIF altitude, or null
   * @return the limit
   * @throws IllegalArgumentException if the value is not a DAFIF altitude
   */
  public static AltitudeLimit limit(@Nullable String raw) {
    String value = raw == null ? "" : raw.trim();
    return Optional.ofNullable(WORDS.get(value))
        .or(() -> match(FLIGHT_LEVEL, value).map(level -> AltitudeLimit.flightLevel(Integer.parseInt(level.group(1)))))
        .or(() -> match(FEET, value).map(feet -> new AltitudeLimit.Height(Double.parseDouble(feet.group(1)), FEET_SUFFIXES.get(feet.group(2)))))
        .orElseThrow(() -> new IllegalArgumentException("Unsupported DAFIF airspace altitude: " + value));
  }

  /**
   * Matches a whole value against a pattern.
   *
   * @param pattern the pattern
   * @param value the value
   * @return the match, or empty if the whole value does not match
   */
  private static Optional<Matcher> match(Pattern pattern, String value) {
    Matcher matcher = pattern.matcher(value);
    return matcher.matches() ? Optional.of(matcher) : Optional.empty();
  }
}
