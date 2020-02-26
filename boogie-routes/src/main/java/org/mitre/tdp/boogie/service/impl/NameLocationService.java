package org.mitre.tdp.boogie.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.collect.MetricTree;
import org.mitre.caasd.commons.collect.SearchResult;

/**
 * Service object for indexing the parameterized data type by both the name of the parameter
 * and by its physical location.
 */
public class NameLocationService<T> {

  private final Multimap<String, T> nameMap;
  private final MetricTree<LatLong, T> locationMap;

  private NameLocationService(Multimap<String, T> nm, MetricTree<LatLong, T> lm) {
    this.nameMap = nm;
    this.locationMap = lm;
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

  public static <T> NameLocationService<T> from(Iterable<T> objs, Function<T, String> nameFn, Function<T, LatLong> locFn) {
    Preconditions.checkNotNull(nameFn);
    Preconditions.checkNotNull(locFn);

    Multimap<String, T> nm = HashMultimap.create();
    MetricTree<LatLong, T> lm = new MetricTree<>(LatLong::distanceInNM);

    objs.forEach(o -> {
      nm.put(nameFn.apply(o), o);
      lm.put(locFn.apply(o), o);
    });

    return new NameLocationService<>(nm, lm);
  }
}
