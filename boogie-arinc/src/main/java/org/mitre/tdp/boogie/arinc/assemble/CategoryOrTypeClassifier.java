package org.mitre.tdp.boogie.arinc.assemble;

import static org.mitre.tdp.boogie.CategoryOrType.*;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.mitre.tdp.boogie.CategoryOrType;
import org.mitre.tdp.boogie.arinc.v22.field.ProcedureDesignAircraftCategoryOrType;

/**
 * This returns a mapping of the category or type in the procedure leg to the boogie category or types.
 * E.g., if the arinc enum is set of things, then this will make the value into an actual set of those things.
 * The use of this is to allow for checking or just ignoring category or type of things.
 * Returns null if nothing maps out.
 */
public final class CategoryOrTypeClassifier implements Function<String, Set<CategoryOrType>> {
  public static final CategoryOrTypeClassifier INSTANCE = new CategoryOrTypeClassifier();
  private CategoryOrTypeClassifier() {}
  @Override
  public Set<CategoryOrType> apply(String  s) {
    return Optional.ofNullable(s)
        .filter(ProcedureDesignAircraftCategoryOrType.VALID::contains)
        .map(ProcedureDesignAircraftCategoryOrType::valueOf)
        .filter(i -> !ProcedureDesignAircraftCategoryOrType.SPEC.equals(i))
        .map(CategoryOrTypeClassifier::from)
        .orElse(null);
  }

  private static Set<CategoryOrType> from(ProcedureDesignAircraftCategoryOrType val) {
    return switch (val) {
      case SPEC -> throw new IllegalStateException("Can't have spec as a value");
      case A -> Set.of(CAT_A);
      case B -> Set.of(CAT_B);
      case C -> Set.of(CAT_C);
      case D -> Set.of(CAT_D);
      case E -> Set.of(CAT_E);
      case F -> Set.of(CAT_A, CAT_B);
      case G -> Set.of(CAT_C, CAT_D);
      case I -> Set.of(CAT_A, CAT_B, CAT_C);
      case J -> Set.of(CAT_A, CAT_B, CAT_C, CAT_D);
      case K -> Set.of(CAT_A, CAT_B, CAT_C, CAT_D, CAT_E);
      case L -> Set.of(CAT_D, CAT_E);
      case H -> Set.of(CAT_H);
      case M -> Set.of(CAT_B, CAT_C);
      case N -> Set.of(CAT_C, CAT_D, CAT_E);
      case O -> Set.of(CAT_B, CAT_C, CAT_D, CAT_E);
      case W -> Set.of(JET);
      case X -> Set.of(PISTON, TURBOPROP, PROP);
      case Y -> Set.of(PISTON);
      case P -> Set.of(JET, PISTON, TURBOJET, TURBOPROP, PROP);
      case Q -> Set.of(TURBOJET, TURBOPROP);
      case R -> Set.of(TURBOJET);
      case S -> Set.of(TURBOPROP);
      case T -> Set.of(PROP);
      case U -> Set.of(TURBOPROP, PROP);
      case V -> Set.of(JET, PISTON, TURBOPROP, PROP);
    };
  }
}
