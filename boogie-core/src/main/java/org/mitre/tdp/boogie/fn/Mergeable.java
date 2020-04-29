package org.mitre.tdp.boogie.fn;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

/**
 * A merge is an object which knows how to combine two objects of the same type into a
 * composite object containing features from both inputs.
 */
public interface Mergeable<T extends Mergeable<T>> {

  /**
   * Takes a collection of {@link Mergeable}s and allows one to add new elements
   * to a running collection of objects. If the a new object which doesn't meet the
   * {@link Mergeable#mergeable(Mergeable)} criteria the window is flushed via
   * reducing against {@link Mergeable#mergeLeft(Mergeable)}. The element is then
   * inserted into the now empty window.
   */
  static <T extends Mergeable<T>> List<T> reduce(List<T> mergables) {
    List<T> reduced = new ArrayList<>();
    if (!mergables.isEmpty()) {
      T ele = mergables.get(0);
      for (int i = 1; i < mergables.size(); i++) {
        T next = mergables.get(i);
        if (ele.mergeable(next)) {
          ele = ele.mergeLeft(next);
        } else {
          reduced.add(ele);
          ele = next;
        }
      }
      reduced.add(ele);
    }
    return reduced;
  }

  /**
   * Merge check respecting one side or the other being null.
   *
   * This is to say this method checks whether either the left or right element is null
   * or if the elements are {@link Mergeable#mergeable(Mergeable)}.
   */
  static <T extends Mergeable<T>> boolean nullMergeable(T m1, T m2) {
    Preconditions.checkArgument(!(null == m2 && null == m1));
    return null == m1 || null == m2 || m1.mergeable(m2);
  }

  /**
   * For a pair of {@link Mergeable} data objects this method returns either the left
   * if the right is null, the right if the left is null or the output of.
   */
  static <T extends Mergeable<T>> T nullableMerge(T m1, T m2) {
    Preconditions.checkArgument(nullMergeable(m1, m2));
    return null == m1 ? m2 : null == m2 ? m1 : m1.mergeLeft(m2);
  }

  boolean mergeable(T obj);

  T mergeLeft(T obj);
}
