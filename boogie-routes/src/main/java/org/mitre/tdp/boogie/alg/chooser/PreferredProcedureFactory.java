package org.mitre.tdp.boogie.alg.chooser;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;

public final class PreferredProcedureFactory {

  private PreferredProcedureFactory() {
    throw new IllegalStateException("");
  }

  /**
   * Returns the first non-empty subset of the input procedures matching the provided equipage - if no procedures are matched
   * the empty list is returned.
   * <br>
   * That is to say this will return:
   * <br>
   * 1. The collection of procedures with equipages matching the first provided
   * 2. If no such procedures exist it will return the collection matching the second provided equipage
   * 3. And so on...
   * 4. If no procedures match the provided equipages then the empty list is returned
   */
  public static Function<Collection<Procedure>, Collection<Procedure>> withPreferredEquipage(RequiredNavigationEquipage... equipages) {
    return new PreferredProcedures(Stream.of(equipages).map(PreferredProcedureFactory::newEquipageFilter).collect(Collectors.toList()));
  }

  private static Predicate<Procedure> newEquipageFilter(RequiredNavigationEquipage equipage) {
    return procedure -> equipage.equals(procedure.requiredNavigationEquipage());
  }
}
