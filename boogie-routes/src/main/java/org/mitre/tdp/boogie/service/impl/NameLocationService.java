package org.mitre.tdp.boogie.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.collect.MetricTree;
import org.mitre.caasd.commons.collect.SearchResult;

import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

/**
 * Service object for indexing the parameterized data type by both the name of the parameter and by its physical location.
 */
public class NameLocationService<T> {
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

  /**
   * Creates a new {@link NameLocationService} from the given set of object with the provided name and location extraction functions.
   */
  public static <T> NameLocationService<T> from(Iterable<T> objs, Function<T, String> nameFn, Function<T, LatLong> locFn) {
    Preconditions.checkNotNull(nameFn);
    Preconditions.checkNotNull(locFn);

    Multimap<String, T> nm = LinkedHashMultimap.create();
    MetricTree<LatLong, T> lm = new MetricTree<>(LatLong::distanceInNM);

    objs.forEach(o -> {
      nm.put(nameFn.apply(o), o);
      lm.put(locFn.apply(o), o);
    });

    return new NameLocationService<>(nm, lm);
  }

  /**
   * Returns the collection of all configured values.
   */
  public Collection<T> allConfigured() {
    return nameMap.values();
  }

  /**
   * Returns all elements who's name matches the one provided.
   */
  public Collection<T> matches(String name) {
    return nameMap.get(name);
  }

  /**
   * Returns the object closest to the provided location.
   */
  public T nearest(LatLong loc) {
    return locationMap.getClosest(loc).value();
  }

  /**
   * Returns a list of all objects within the given radius of the provided location.
   */
  public List<T> allWithinRange(LatLong loc, double radius) {
    return locationMap.getAllWithinRange(loc, radius).stream().map(SearchResult::value).collect(Collectors.toList());
  }
}
