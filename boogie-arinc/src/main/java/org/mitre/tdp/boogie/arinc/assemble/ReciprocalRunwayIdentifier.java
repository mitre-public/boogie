package org.mitre.tdp.boogie.arinc.assemble;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.isNumeric;

import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Returns the expected identifier of the reciprocal runway given and input runway identifier.
 * <br>
 * If a reciprocal identifier cannot be constructed from the input runway name this class returns {@link Optional#empty()}.
 */
public final class ReciprocalRunwayIdentifier implements Function<String, Optional<String>> {

  public static final ReciprocalRunwayIdentifier INSTANCE = new ReciprocalRunwayIdentifier();

  /**
   * Regex for common runway ids (00-36)(L,C,R).
   * <br>
   * This includes additional codes seen in the CIFP data:
   * <br>
   * 1. 'S' - soft-surface
   * 2. 'W' - water
   * 3. 'U' - ultralight
   * 4. 'G' - glider
   * <br>
   * The above are consistently called out in the CIFP cycle release notes/readme.
   */
  public static final Pattern RUNWAY_ID = Pattern.compile("(0[1-9]|[1-2]\\d|3[0-6])[LCRSWUG]?");

  private ReciprocalRunwayIdentifier() {
  }

  @Override
  public Optional<String> apply(String runwayId) {
    requireNonNull(runwayId);

    Matcher matcher = RUNWAY_ID.matcher(runwayId);
    if (matcher.find()) {
      String id = matcher.group();

      String prefix = matcher.start() == 0 ? "" : runwayId.substring(0, matcher.start());
      String postfix = matcher.end() == runwayId.length() - 1 ? "" : runwayId.substring(matcher.end());

      return Optional.of(String.join("", prefix, invertIdentifier(id), postfix));
    } else {
      return Optional.empty();
    }
  }

  /**
   * Inverts the runway ids of the format 03L, 23R, 06C, etc. by inverting the magnetic bearing of the runway as declared in the
   * name and then flipping the runway character designator.
   */
  String invertIdentifier(String identifier) {
    checkArgument((identifier.length() == 2
        && isNumeric(identifier))
        || (identifier.length() == 3 && isNumeric(identifier.substring(0, 2))), "Bad identifier: " + identifier);
    return inverseBearing(identifier).concat(inverseCharacter(identifier));
  }

  /**
   * Returns the 180deg inverse of the runway bearing from the id e.g. 03 -> 21, etc.
   */
  String inverseBearing(String runwayId) {
    int bearing = Integer.parseInt(runwayId.substring(0, 2));
    int invBearing;
    //special case where pairer would map 18->0 instead of 18->36 due to logic
    if (bearing == 18) {
      invBearing = 36;
    } else {
      invBearing = (bearing + 18) % 36;
    }
    return String.format("%02d", invBearing);
  }

  /**
   * Returns the inverse of the runway character if L/R otherwise leaves it unchanged.
   */
  String inverseCharacter(String runwayId) {
    if (runwayId.length() < 3) {
      return "";
    }
    String lastChar = runwayId.substring(2);
    return lastChar.equals("R") ? "L" : lastChar.equals("L") ? "R" : lastChar;
  }
}
