package org.mitre.tdp.boogie.alg.resolve.infer;

import java.util.List;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.CategoryOrType;
import org.mitre.tdp.boogie.Transition;

public final class KeepTransition implements Predicate<Transition> {
  private final List<CategoryOrType> categoryAndTypes;

  private KeepTransition(List<CategoryOrType> categoryAndTypes) {
    this.categoryAndTypes = categoryAndTypes;
  }

  public static KeepTransition of(List<CategoryOrType> categoryAndType) {
    return new KeepTransition(categoryAndType);
  }

  @Override
  public boolean test(Transition transition) {
    return transition.categoryOrTypes().isEmpty() //if its not set then just keep them
        || transition.categoryOrTypes().contains(CategoryOrType.NOT_SPECIFIED) //not specified just means all now
        || categoryAndTypes.isEmpty()  //if we are not asking i guess just keep it
        || categoryAndTypes.stream().anyMatch(category -> transition.categoryOrTypes().contains(category));
  }
}
