package org.mitre.tdp.boogie.alg.resolve.infer;

import java.util.function.Predicate;

import org.mitre.tdp.boogie.CategoryAndType;
import org.mitre.tdp.boogie.CategoryOrType;
import org.mitre.tdp.boogie.Transition;

public final class KeepTransition implements Predicate<Transition> {
  private final CategoryAndType categoryAndType;

  private KeepTransition(CategoryAndType categoryAndType) {
    this.categoryAndType = categoryAndType;
  }

  public static KeepTransition of(CategoryAndType categoryAndType) {
    return new KeepTransition(categoryAndType);
  }

  @Override
  public boolean test(Transition transition) {
    return transition.categoryOrTypes().isEmpty() //if its not set then just keep them
        || transition.categoryOrTypes().contains(CategoryOrType.NOT_SPECIFIED) //not specified just means all now
        || categoryAndType.type().isEmpty() && categoryAndType.category().isEmpty() //if we are not asking i guess just keep it
        || categoryAndType.type().filter(type -> transition.categoryOrTypes().contains(type)).isPresent() //we have a match
        || categoryAndType.category().filter(cat -> transition.categoryOrTypes().contains(cat)).isPresent(); //we have a match
  }
}
