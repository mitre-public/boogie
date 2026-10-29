package org.mitre.tdp.boogie.alg.facade;

import org.mitre.tdp.boogie.alg.resolve.ElementType;

import java.util.Optional;
import java.util.function.BiFunction;

import static org.mitre.tdp.boogie.alg.resolve.ElementType.*;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.AIRWAY;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.AIRWAY_TO_APPROACH;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.APPROACH;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.SID;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.SID_TO_APPROACH;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.SID_TO_STAR;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.STAR;
import static org.mitre.tdp.boogie.alg.resolve.ElementType.STAR_TO_APPROACH;

/**
 * Returns one of the special {@link ElementType} classes if the previous and next match the appropriate criteria.
 * <br>
 * These special classes are to indicate that certain legs (post combination) may contain a hybrid of information from distinct
 * pieces of infrastructure.
 * <br>
 * e.g. for a TF leg where an airway joins a STAR at some fix - the actual leg geometry (the path that the aircraft will fly)
 * is a property of the airway (as it defines the previous fix to the STAR join). However the restriction information and the
 * required navigational performance values for the leg come from the STAR (as thats what's being joined).
 * <br>
 * These need to be special-cased so that metrics downstream like "airway segment usage" can still identify these legs as having
 * been defined by the airway geometrically (and so we should track usage for them) even if the constraints come from the STAR
 * (and from the perspective of the FMS the leg is really then "sourced from" the STAR).
 */
public final class CollapsedElementType implements BiFunction<ElementType, ElementType, Optional<ElementType>> {
  public static final CollapsedElementType INSTANCE = new CollapsedElementType();
  private CollapsedElementType() {
  }
  @Override
  public Optional<ElementType> apply(ElementType previous, ElementType next) {
    if (AIRWAY.equals(previous) && STAR.equals(next)) {
      return Optional.of(AIRWAY_TO_STAR);
    } else if (SID.equals(previous) && STAR.equals(next)) {
      return Optional.of(SID_TO_STAR);
    } else if (AIRWAY.equals(previous) && APPROACH.equals(next)) {
      return Optional.of(AIRWAY_TO_APPROACH);
    } else if (STAR.equals(previous) && APPROACH.equals(next)) {
      return Optional.of(STAR_TO_APPROACH);
    } else if (SID.equals(previous) && APPROACH.equals(next)) {
      return Optional.of(SID_TO_APPROACH);
    }
    return Optional.empty();
  }
}
