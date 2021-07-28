package org.mitre.tdp.boogie.util;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.collect.MetricTree;
import org.mitre.caasd.commons.collect.SearchResult;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

/**
 * Service object for indexing the parameterized data type by both the name of the parameter and by its physical location.
 */
public final class NameLocationService<T> {
  /**
   * Multimap of all {Name, Object} pairs.
   */
  private final Multimap<String, T> nameMap;
  /**
   * {@link MetricTree} indexing all records by their relative locations.
   */
  private final MetricTree<LatLong, T> locationMap;

  private NameLocationService(Multimap<String, T> nm, MetricTree<LatLong, T> lm) {
    this.nameMap = nm;
    this.locationMap = lm;
  }

  public Collection<T> allConfigured() {
    return nameMap.values();
  }

  public Collection<T> matches(String name) {
    return nameMap.get(name);
  }

  public T nearest(LatLong loc) {
    return locationMap.getClosest(loc).value();
  }

  public List<T> allWithinRange(LatLong loc, double radius) {
    return locationMap.getAllWithinRange(loc, radius).stream().map(SearchResult::value).collect(Collectors.toList());
  }

  /**
   * Creates a new {@link NameLocationService} from the given set of object with the provided name and location extraction functions.
   */
  public static <T> NameLocationService<T> from(Iterable<T> objs, Function<T, String> nameFn, Function<T, LatLong> locFn) {
    requireNonNull(nameFn, "Name function cannot be null.");
    requireNonNull(locFn, "Location function cannot be null.");

    Multimap<String, T> nm = LinkedHashMultimap.create();
    MetricTree<LatLong, T> lm = new MetricTree<>(LatLong::distanceInNM);

    objs.forEach(o -> {
      nm.put(nameFn.apply(o), o);
      lm.put(locFn.apply(o), o);
    });

    return new NameLocationService<>(nm, lm);
  }
}
