package org.mitre.tdp.boogie.util;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.mitre.caasd.commons.Pair;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Utilities for enumerating combinations and Cartesian products.
 */
public final class Combinatorics {

  private Combinatorics() {
    throw new IllegalStateException("Utility Class");
  }

  public static <T> Iterator<Pair<T, T>> pairwiseCombos(Iterable<T> coll) {

    final LinkedList<T> collList = Lists.newLinkedList(coll);

    Iterator<int[]> iter;
    if (collList.size() == 2) {
      iter = new SingletonIterator(new int[] {0, 1});
    } else if (collList.size() > 2) {
      iter = new LexicographicIterator(collList.size(), 2);
    } else {
      iter = Collections.emptyIterator();
    }

    return Iterators.transform(iter, input -> new Pair<>(collList.get(input[0]), collList.get(input[1])));
  }

  /**
   * Returns a mutable collection of pairs, iterating the second input for each value in the first.
   * Inputs are snapshotted and duplicates are removed by equality, retaining their first occurrence.
   */
  public static <U, V> Collection<Pair<U, V>> cartesianProduct(Collection<U> first, Collection<V> second) {
    CartesianProductIterator<U, V> iterator = new CartesianProductIterator<>(first::iterator, second::iterator);
    List<Pair<U, V>> pairList = new ArrayList<>(iterator.size);
    iterator.forEachRemaining(pairList::add);
    return pairList;
  }

  /**
   * Returns an iterator producing pairs in the same order as the collection overload.
   * Inputs are snapshotted and deduplicated immediately; pairs are created as the iterator advances.
   */
  public static <U, V> Iterator<Pair<U, V>> cartesianProduct(Iterable<U> first, Iterable<V> second) {
    return new CartesianProductIterator<>(first, second);
  }

  /**
   * Returns a sequential stream of pairs, iterating the second collection for each value in the first.
   * Pairs are created on demand, preserving the inputs' encounter order and duplicate occurrences.
   * <p>
   * This method neither snapshots nor deduplicates the inputs and does not build an intermediate pair list.
   * Neither input may be modified while the stream is in use. Use {@link #cartesianProduct(Collection, Collection)}
   * when snapshotting and equality-based duplicate removal are required.
   */
  public static <U, V> Stream<Pair<U, V>> cartesianProductStream(Collection<U> first, Collection<V> second) {
    requireNonNull(first);
    requireNonNull(second);
    if (first.isEmpty() || second.isEmpty()) {
      return Stream.empty();
    }

    Iterator<Pair<U, V>> pairs = new Iterator<>() {
      private final Iterator<U> firstIterator = first.iterator();
      private Iterator<V> secondIterator = Collections.emptyIterator();
      private U currentFirst;

      @Override
      public boolean hasNext() {
        return secondIterator.hasNext() || firstIterator.hasNext();
      }

      @Override
      public Pair<U, V> next() {
        if (!secondIterator.hasNext()) {
          currentFirst = firstIterator.next();
          secondIterator = second.iterator();
        }
        return Pair.of(currentFirst, secondIterator.next());
      }
    };

    long size = (long) first.size() * second.size();
    return StreamSupport.stream(Spliterators.spliterator(pairs, size, Spliterator.ORDERED | Spliterator.NONNULL), false);
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

  private static final class CartesianProductIterator<U, V> implements Iterator<Pair<U, V>> {

    private final List<U> first;
    private final List<V> second;
    private final int size;

    private int firstIndex;
    private int secondIndex;

    private CartesianProductIterator(Iterable<U> first, Iterable<V> second) {
      Set<U> uniqueFirst = Sets.newLinkedHashSet(first);
      Set<V> uniqueSecond = Sets.newLinkedHashSet(second);
      this.first = List.copyOf(uniqueFirst);
      // Preserve the existing behavior: an empty first input skips validation of second-input elements.
      this.second = this.first.isEmpty() ? List.of() : List.copyOf(uniqueSecond);

      long productSize = (long) this.first.size() * this.second.size();
      if (productSize > Integer.MAX_VALUE) {
        throw new IllegalArgumentException("Cartesian product too large; must have size at most Integer.MAX_VALUE");
      }
      this.size = (int) productSize;
    }

    @Override
    public boolean hasNext() {
      return firstIndex < first.size() && !second.isEmpty();
    }

    @Override
    public Pair<U, V> next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      Pair<U, V> pair = Pair.of(first.get(firstIndex), second.get(secondIndex));
      if (++secondIndex == second.size()) {
        secondIndex = 0;
        firstIndex++;
      }
      return pair;
    }
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
    @Override
    public boolean hasNext() {
      return more;
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
    @Override
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
    @Override
    public boolean hasNext() {
      return more;
    }

    /**
     * @return the singleton in first activation; throws NSEE thereafter
     */
    @Override
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
    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
}
