package org.mitre.tdp.boogie.arinc.v18.field;

import static java.util.Objects.checkFromToIndex;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.BooleanStringParser;

/**
 * This field is used in conjunction with Turn direction to indicate that a turn is required prior to capturing the path defined
 * in a terminal procedure.
 * <br>
 * The field contains the alpha character “Y” when a turn is required prior to beginning the leg defined by the Path Term. The
 * direction of the turn is specified in Section 5.20.
 */
public final class TurnDirectionValid implements FieldSpec<Boolean> {

  private static final Optional<Boolean> TRUE = Optional.of(true);
  private static final Optional<Boolean> FALSE = Optional.of(false);

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.22";
  }

  @Override
  public Optional<Boolean> parse(String source, int startOffset, int endOffset) {
    checkFromToIndex(startOffset, endOffset, source.length());
    if (endOffset - startOffset == 1) {
      char value = source.charAt(startOffset);
      return value == 'Y' || value == 'y' ? TRUE : FALSE;
    }
    // Standalone callers may supply padded or longer Boolean spellings.
    return BooleanStringParser.INSTANCE.test(source.substring(startOffset, endOffset)) ? TRUE : FALSE;
  }
}
