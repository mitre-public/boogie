package org.mitre.tdp.boogie.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.mitre.caasd.commons.Pair;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class Combinatorics {

  private Combinatorics() {
    throw new IllegalStateException("Utility Class");
  }

  public static <T> Iterator<Pair<T, T>> pairwiseCombos(Iterable<T> coll) {

    final LinkedList<T> collList = Lists.newLinkedList(coll);

    Iterator<int[]> iter;
    // commons math 3 method - scrape out for now - BOO - SPARK
    // iter = CombinatoricsUtils.combinationsIterator(collList.size(), 2);
    if (collList.size() == 2) {
      iter = new SingletonIterator(new int[] {0, 1});
    } else if (collList.size() > 2) {
      iter = new LexicographicIterator(collList.size(), 2);
    } else {
      iter = Collections.emptyIterator();
    }

    Function<int[], Pair<T, T>> function = input -> new Pair<>(collList.get(input[0]), collList.get(input[1]));
    return Iterators.transform(iter, function);
  }

  public static <U, V> Iterator<Pair<U, V>> cartesianProduct(Iterable<U> first, Iterable<V> second) {
    Set<List<Object>> sets = Sets.cartesianProduct(Sets.newHashSet(first), Sets.newHashSet(second));
    return Iterators.transform(sets.iterator(), list -> Pair.of((U) list.get(0), (V) list.get(1)));
  }

  public static <T, C extends Iterable<T>> Iterator<Pair<T, T>> pairsCartesianProduct(Iterable<C> coll) {

    List<Iterator<Pair<T, T>>> iters = new ArrayList<>();
    Iterator<Pair<C, C>> grpIter = pairwiseCombos(coll);
    while (grpIter.hasNext()) {
      Pair<C, C> pair = grpIter.next();
      Iterator<Pair<T, T>> subIter = cartesianProduct(pair.first(), pair.second());
      iters.add(subIter);
    }
    return Iterators.concat(iters.iterator());
  }

  private static class LexicographicIterator implements Iterator<int[]> {
    /**
     * Size of subsets returned by the iterator
     */
    private final int k;

    /**
     * c[1], ..., c[k] stores the next combination; c[k + 1], c[k + 2] are sentinels.
     * <p>
     * Note that c[0] is "wasted" but this makes it a little easier to follow the code.
     * </p>
     */
    private final int[] c;

    /**
     * Return value for {@link #hasNext()}
     */
    private boolean more = true;

    /**
     * Marker: smallest index such that c[j + 1] > j
     */
    private int j;

    /**
     * Construct a CombinationIterator to enumerate k-sets from n.
     * <p>
     * NOTE: If {@code k === 0} or {@code k >= n}, the Iterator will be empty (that is,
     * {@link #hasNext()} will return {@code false} immediately.
     * </p>
     * @param n size of the set from which subsets are enumerated
     * @param k size of the subsets to enumerate
     */
    public LexicographicIterator(int n, int k) {
      this.k = k;
      c = new int[k + 3];
      if (k == 0 || k >= n) {
        more = false;
        return;
      }
      // Initialize c to start with lexicographically first k-set
      for (int i = 1; i <= k; i++) {
        c[i] = i - 1;
      }
      // Initialize sentinels
      c[k + 1] = n;
      c[k + 2] = 0;
      j = k; // Set up invariant: j is smallest index such that c[j + 1] >
      // j
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasNext() {
      return more;
    }

    /**
     * {@inheritDoc}
     */
    public int[] next() {
      if (!more) {
        throw new NoSuchElementException();
      }
      // Copy return value (prepared by last activation)
      final int[] ret = new int[k];
      System.arraycopy(c, 1, ret, 0, k);

      // Prepare next iteration
      // T2 and T6 loop
      int x = 0;
      if (j > 0) {
        x = j;
        c[j] = x;
        j--;
        return ret;
      }
      // T3
      if (c[1] + 1 < c[2]) {
        c[1]++;
        return ret;
      } else {
        j = 2;
      }
      // T4
      boolean stepDone = false;
      while (!stepDone) {
        c[j - 1] = j - 2;
        x = c[j] + 1;
        if (x == c[j + 1]) {
          j++;
        } else {
          stepDone = true;
        }
      }
      // T5
      if (j > k) {
        more = false;
        return ret;
      }
      // T6
      c[j] = x;
      j--;
      return ret;
    }

    /**
     * Not supported.
     */
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  /**
   * Iterator with just one element to handle degenerate cases (full array, empty array) for
   * combination iterator.
   */
  private static class SingletonIterator implements Iterator<int[]> {
    /**
     * Singleton array
     */
    private final int[] singleton;
    /**
     * True on initialization, false after first call to next
     */
    private boolean more = true;

    /**
     * Create a singleton iterator providing the given array.
     * @param singleton array returned by the iterator
     */
    public SingletonIterator(final int[] singleton) {
      this.singleton = singleton;
    }

    /**
     * @return True until next is called the first time, then false
     */
    public boolean hasNext() {
      return more;
    }

    /**
     * @return the singleton in first activation; throws NSEE thereafter
     */
    public int[] next() {
      if (more) {
        more = false;
        return singleton;
      } else {
        throw new NoSuchElementException();
      }
    }

    /**
     * Not supported
     */
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
}