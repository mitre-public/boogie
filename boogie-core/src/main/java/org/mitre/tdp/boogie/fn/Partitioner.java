package org.mitre.tdp.boogie.fn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * A collection of convenience collection partitioners based on predicates.
 *
 * In general these work in one of two ways:
 *
 * 1) They take a {@link Predicate}, splitting the collection in order into
 * sub-collections based on whether the predicate evaluates to the same thing
 * for successive elements.
 *
 * E.g Given a collection [a,b,c,d,e] that evaluates to [true, true, false, false,
 * true], this would produce a nested set of collections [[a,b],[c,d],[e]]
 *
 * 2) They take a {@link BiPredicate}, splitting the collection in order into
 * sub-collections based on whether the predicate evaluates to true on successive
 * elements. Or to reword this, it groups subsequent elements when the predicate
 * evaluates to true, and splits subsequent elements where it evaluates to false.
 *
 * E.g Given a collection [a,a,b,c,b,b] and using equality as the BiPredicate this
 * would produce a nested set of collections [[a,a],[b],[c],[b,b]]
 */
public interface Partitioner<T, C extends Collection<T>, CC extends Collection<C>> extends Collector<T, CC, CC> {

  interface Lists<T> extends Partitioner<T, List<T>, List<List<T>>> {
    @Override
    default Supplier<List<List<T>>> supplier() {
      return ArrayList::new;
    }

    @Override
    default BinaryOperator<List<List<T>>> combiner() {
      return (r1, r2) -> {
        r1.addAll(r2);
        return r1;
      };
    }

    @Override
    default Function<List<List<T>>, List<List<T>>> finisher() {
      return Function.identity();
    }

    @Override
    default Set<Characteristics> characteristics() {
      return Collections.singleton(Characteristics.IDENTITY_FINISH);
    }
  }

  static <T> Partitioner<T, List<T>, List<List<T>>> listByPredicate(Predicate<T> pred) {
    return new Lists<T>() {
      @Override
      public BiConsumer<List<List<T>>, T> accumulator() {
        return (x, y) -> {
          if (x.isEmpty()) {
            x.add(new ArrayList<>(Collections.singleton(y)));
            return;
          }

          List<T> last = x.get(x.size() - 1);
          if (pred.test(last.get(last.size() - 1)) != pred.test(y)) {
            x.add(new ArrayList<>(Collections.singleton(y)));
          } else {
            last.add(y);
          }
        };
      }
    };
  }

  static <T> Partitioner<T, List<T>, List<List<T>>> listByPredicate(BiPredicate<T, T> pred) {
    return listByPredicate(pred, false);
  }

  /**
   * Takes a BiPredicate splitter as well as a boolean on whether to use the first or last element
   * of the grouping being evaluated for partitioning when the split is being checked.
   *
   * E.g. Use firstLast=false when you want to split a group when points are >30s apart, while you
   * would use firstLast=true when the you want to split a group of points who's first and last points
   * are over 5 minutes apart (i.e. 5min subsegments).
   */
  static <T> Partitioner<T, List<T>, List<List<T>>> listByPredicate(BiPredicate<T, T> pred, boolean firstNext) {
    return new Lists<T>() {
      @Override
      public BiConsumer<List<List<T>>, T> accumulator() {
        return (x, y) -> {
          if (x.isEmpty()) {
            x.add(new ArrayList<>(Collections.singleton(y)));
            return;
          }

          List<T> last = x.get(x.size() - 1);
          T comp = firstNext ? last.get(0) : last.get(last.size() - 1);
          if (pred.negate().test(comp, y)) {
            x.add(new ArrayList<>(Collections.singleton(y)));
          } else {
            last.add(y);
          }
        };
      }
    };
  }

  interface TreeSets<T extends Comparable<? super T>> extends Partitioner<T, TreeSet<T>, TreeSet<TreeSet<T>>> {
    @Override
    default Supplier<TreeSet<TreeSet<T>>> supplier() {
      return () -> new TreeSet<>(Comparator.comparing(TreeSet::first));
    }

    @Override
    default BinaryOperator<TreeSet<TreeSet<T>>> combiner() {
      return (r1, r2) -> {
        r1.addAll(r2);
        return r1;
      };
    }

    @Override
    default Function<TreeSet<TreeSet<T>>, TreeSet<TreeSet<T>>> finisher() {
      return Function.identity();
    }

    @Override
    default Set<Characteristics> characteristics() {
      return Collections.singleton(Characteristics.IDENTITY_FINISH);
    }
  }

  static <T extends Comparable<? super T>> Partitioner<T, TreeSet<T>, TreeSet<TreeSet<T>>> navigableByPredicate(Predicate<T> pred) {
    return new TreeSets<T>() {
      @Override
      public BiConsumer<TreeSet<TreeSet<T>>, T> accumulator() {
        return (x, y) -> {
          if (x.isEmpty()) {
            x.add(new TreeSet<>(Collections.singleton(y)));
            return;
          }

          TreeSet<T> last = x.last();
          if (pred.test(last.last()) != pred.test(y)) {
            x.add(new TreeSet<>(Collections.singleton(y)));
          } else {
            last.add(y);
          }
        };
      }
    };
  }

  static <T extends Comparable<? super T>> Partitioner<T, TreeSet<T>, TreeSet<TreeSet<T>>> navigableByPredicate(BiPredicate<T, T> pred) {
    return navigableByPredicate(pred, false);
  }

  /**
   * Behavior is similar to {@link Partitioner#listByPredicate(BiPredicate, boolean)}, see there for
   * examples on when to use.
   */
  static <T extends Comparable<? super T>> Partitioner<T, TreeSet<T>, TreeSet<TreeSet<T>>> navigableByPredicate(BiPredicate<T, T> pred, boolean firstNext) {
    return new TreeSets<T>() {
      @Override
      public BiConsumer<TreeSet<TreeSet<T>>, T> accumulator() {
        return (x, y) -> {
          if (x.isEmpty()) {
            x.add(new TreeSet<>(Collections.singleton(y)));
            return;
          }

          TreeSet<T> last = x.last();
          T comp = firstNext ? last.first() : last.last();
          if (pred.negate().test(comp, y)) {
            x.add(new TreeSet<>(Collections.singleton(y)));
          } else {
            last.add(y);
          }
        };
      }
    };
  }
}
