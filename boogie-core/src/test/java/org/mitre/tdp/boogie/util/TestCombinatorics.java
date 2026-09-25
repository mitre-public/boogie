package org.mitre.tdp.boogie.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.util.Combinatorics.cartesianProduct;
import static org.mitre.tdp.boogie.util.Combinatorics.cartesianProductStream;
import static org.mitre.tdp.boogie.util.Combinatorics.pairwiseCombos;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Pair;

class TestCombinatorics {

  @Test
  void testPairwiseCombos() {
    List<Integer> ints = IntStream.rangeClosed(0, 10).boxed().collect(Collectors.toList());
    Iterator<Pair<Integer, Integer>> pairs = pairwiseCombos(ints);

    List<Pair<Integer, Integer>> pairList = new ArrayList<>();
    pairs.forEachRemaining(pairList::add);

    assertEquals(55, pairList.size());
  }

  @Test
  void productsKeepFirstEqualObjectsAndLeftMajorEncounterOrder() {
    Value firstB = new Value("b");
    Value a = new Value("a");
    Value firstY = new Value("y");
    Value x = new Value("x");
    List<Value> left = List.of(firstB, a, new Value("b"), firstB);
    List<Value> right = List.of(firstY, x, new Value("y"));
    List<Pair<Value, Value>> expected = List.of(
        Pair.of(firstB, firstY), Pair.of(firstB, x), Pair.of(a, firstY), Pair.of(a, x)
    );

    List<List<Pair<Value, Value>>> products = List.of(
        new ArrayList<>(cartesianProduct(left, right)),
        drain(cartesianProduct(left::iterator, right::iterator))
    );
    for (List<Pair<Value, Value>> actual : products) {
      assertEquals(expected, actual);
      for (int index = 0; index < expected.size(); index++) {
        assertSame(expected.get(index).first(), actual.get(index).first());
        assertSame(expected.get(index).second(), actual.get(index).second());
      }
    }
  }

  @Test
  void productsDeduplicateByEqualityEvenForIdentitySets() {
    Set<Value> left = Collections.newSetFromMap(new IdentityHashMap<>());
    left.add(new Value("same"));
    left.add(new Value("same"));
    Value first = left.iterator().next();
    List<Integer> right = List.of(1);

    List<List<Pair<Value, Integer>>> products = List.of(
        new ArrayList<>(cartesianProduct(left, right)),
        drain(cartesianProduct(left::iterator, right::iterator))
    );
    for (List<Pair<Value, Integer>> actual : products) {
      assertEquals(1, actual.size());
      assertSame(first, actual.get(0).first());
    }
  }

  @Test
  void collectionProductIsAnIndependentMutableSnapshot() {
    List<Integer> left = new ArrayList<>(List.of(2, 1));
    List<String> right = new ArrayList<>(List.of("b", "a"));
    Collection<Pair<Integer, String>> product = cartesianProduct(left, right);

    left.clear();
    right.set(0, "changed");
    assertEquals(List.of(Pair.of(2, "b"), Pair.of(2, "a"), Pair.of(1, "b"), Pair.of(1, "a")), new ArrayList<>(product));

    assertTrue(product.remove(Pair.of(2, "b")));
    assertTrue(product.add(Pair.of(3, "c")));
    product.clear();
    assertTrue(product.isEmpty());
    assertTrue(left.isEmpty());
    assertEquals(List.of("changed", "a"), right);
  }

  @Test
  void iterableProductSnapshotsBothInputsBeforeIteration() {
    List<Integer> left = new ArrayList<>(List.of(2, 1));
    List<String> right = new ArrayList<>(List.of("b", "a"));
    Iterator<Pair<Integer, String>> product = cartesianProduct(left::iterator, right::iterator);

    left.clear();
    right.clear();
    assertEquals(List.of(Pair.of(2, "b"), Pair.of(2, "a"), Pair.of(1, "b"), Pair.of(1, "a")), drain(product));
  }

  @Test
  void iterableProductSupportsInputsThatCannotBeRestarted() {
    Iterator<Integer> left = List.of(1, 2).iterator();
    Iterator<String> right = List.of("a", "b").iterator();

    Iterator<Pair<Integer, String>> product = cartesianProduct(() -> left, () -> right);

    assertEquals(List.of(Pair.of(1, "a"), Pair.of(1, "b"), Pair.of(2, "a"), Pair.of(2, "b")), drain(product));
  }

  @Test
  void emptyCollectionProductsRemainMutable() {
    List<Collection<Pair<Integer, String>>> products = List.of(
        cartesianProduct(List.<Integer>of(), List.of("a")),
        cartesianProduct(List.of(1), List.<String>of()),
        cartesianProduct(List.<Integer>of(), List.<String>of())
    );
    for (Collection<Pair<Integer, String>> product : products) {
      assertTrue(product.isEmpty());
      assertTrue(product.add(Pair.of(1, "a")));
      assertEquals(1, product.size());
    }
  }

  @Test
  void emptyIterableProductsAreExhaustedAndDoNotSupportRemoval() {
    List<Iterator<Pair<Integer, String>>> products = List.of(
        cartesianProduct(List.<Integer>of()::iterator, List.of("a")::iterator),
        cartesianProduct(List.of(1)::iterator, List.<String>of()::iterator),
        cartesianProduct(List.<Integer>of()::iterator, List.<String>of()::iterator)
    );
    for (Iterator<Pair<Integer, String>> product : products) {
      assertFalse(product.hasNext());
      assertThrows(NoSuchElementException.class, product::next);
      assertThrows(UnsupportedOperationException.class, product::remove);
    }
  }

  @Test
  void iterableProductHonorsIteratorExhaustionAndRemovalContracts() {
    Iterator<Pair<Integer, String>> product = cartesianProduct(List.of(1, 2)::iterator, List.of("a")::iterator);

    assertTrue(product.hasNext());
    assertTrue(product.hasNext());
    assertEquals(Pair.of(1, "a"), product.next());
    assertThrows(UnsupportedOperationException.class, product::remove);
    assertEquals(Pair.of(2, "a"), product.next());
    assertFalse(product.hasNext());
    assertThrows(NoSuchElementException.class, product::next);
  }

  @Test
  void productsRejectNullInputsEvenWhenTheOtherInputIsEmpty() {
    assertThrows(NullPointerException.class, () -> cartesianProduct((Collection<Integer>) null, List.<String>of()));
    assertThrows(NullPointerException.class, () -> cartesianProduct(List.<Integer>of(), (Collection<String>) null));
    assertThrows(NullPointerException.class, () -> cartesianProduct((Iterable<Integer>) null, List.<String>of()::iterator));
    assertThrows(NullPointerException.class, () -> cartesianProduct(List.<Integer>of()::iterator, (Iterable<String>) null));
  }

  @Test
  void productsPreserveNullElementValidationOrder() {
    List<Integer> nullLeft = Collections.singletonList(null);
    List<String> nullRight = Collections.singletonList(null);

    assertThrows(NullPointerException.class, () -> cartesianProduct(nullLeft, List.<String>of()));
    assertThrows(NullPointerException.class, () -> cartesianProduct(nullLeft::iterator, List.<String>of()::iterator));
    assertThrows(NullPointerException.class, () -> cartesianProduct(List.of(1), nullRight));
    assertThrows(NullPointerException.class, () -> cartesianProduct(List.of(1)::iterator, nullRight::iterator));

    // The original product stops validating axes when it encounters an empty left axis.
    assertTrue(cartesianProduct(List.<Integer>of(), nullRight).isEmpty());
    assertFalse(cartesianProduct(List.<Integer>of()::iterator, nullRight::iterator).hasNext());
  }

  @Test
  void productsRejectSizesBeyondTheIntegerRangeBeforeEnumeratingPairs() {
    List<Integer> axis = IntStream.range(0, 46_341).boxed().toList();

    assertThrows(IllegalArgumentException.class, () -> cartesianProduct(axis, axis));
    assertThrows(IllegalArgumentException.class, () -> cartesianProduct(axis::iterator, axis::iterator));
  }

  @Test
  void productSizeLimitUsesDistinctInputs() {
    List<Integer> axis = Collections.nCopies(50_000, 7);

    assertEquals(List.of(Pair.of(7, 7)), new ArrayList<>(cartesianProduct(axis, axis)));
    assertEquals(List.of(Pair.of(7, 7)), drain(cartesianProduct(axis::iterator, axis::iterator)));
  }

  @Test
  void streamProductsPreserveDuplicateObjectsAndLeftMajorOrder() {
    Value firstB = new Value("b");
    Value a = new Value("a");
    Value equalB = new Value("b");
    Value firstY = new Value("y");
    Value equalY = new Value("y");
    List<Pair<Value, Value>> expected = List.of(
        Pair.of(firstB, firstY), Pair.of(firstB, equalY),
        Pair.of(a, firstY), Pair.of(a, equalY),
        Pair.of(equalB, firstY), Pair.of(equalB, equalY)
    );

    List<Pair<Value, Value>> actual = cartesianProductStream(
        List.of(firstB, a, equalB), List.of(firstY, equalY)).toList();

    assertEquals(expected, actual);
    for (int index = 0; index < expected.size(); index++) {
      assertSame(expected.get(index).first(), actual.get(index).first());
      assertSame(expected.get(index).second(), actual.get(index).second());
    }
  }

  @Test
  void streamProductsHandleEmptyInputs() {
    assertTrue(cartesianProductStream(List.<Integer>of(), List.of("a")).findAny().isEmpty());
    assertTrue(cartesianProductStream(List.of(1), List.<String>of()).findAny().isEmpty());
    assertTrue(cartesianProductStream(List.<Integer>of(), List.<String>of()).findAny().isEmpty());
  }

  @Test
  void streamProductsTraverseOnlyOnDemand() {
    CountingCollection<Integer> left = new CountingCollection<>(List.of(1, 2));
    CountingCollection<String> right = new CountingCollection<>(List.of("a", "b"));

    var pairs = cartesianProductStream(left, right);

    assertEquals(0, left.visited);
    assertEquals(0, right.visited);
    assertEquals(Pair.of(1, "a"), pairs.findFirst().orElseThrow());
    assertEquals(1, left.visited);
    assertEquals(1, right.visited);
  }

  @Test
  void streamProductsStopAtTheRequestedPrefix() {
    CountingCollection<Integer> left = new CountingCollection<>(List.of(2, 1, 3));
    CountingCollection<String> right = new CountingCollection<>(List.of("b", "a"));

    List<Pair<Integer, String>> prefix = cartesianProductStream(left, right).limit(3).toList();

    assertEquals(List.of(Pair.of(2, "b"), Pair.of(2, "a"), Pair.of(1, "b")), prefix);
    assertEquals(2, left.visited);
    assertEquals(3, right.visited);
  }

  @Test
  void streamProductsAllowLargeProductsWithSmallPrefixes() {
    List<Integer> axis = IntStream.range(0, 46_341).boxed().toList();

    assertEquals(List.of(Pair.of(0, 0), Pair.of(0, 1)), cartesianProductStream(axis, axis).limit(2).toList());
    assertEquals(2_147_488_281L, cartesianProductStream(axis, axis).count());
  }

  @Test
  void streamProductsPreserveOrderAndDuplicatesWhenParallel() {
    List<Integer> left = List.of(2, 1, 2);
    List<Integer> right = IntStream.range(0, 600).boxed().toList();
    List<Pair<Integer, Integer>> expected = new ArrayList<>();
    for (Integer first : left) {
      for (Integer second : right) {
        expected.add(Pair.of(first, second));
      }
    }

    assertEquals(expected, cartesianProductStream(left, right).parallel().toList());
  }

  @Test
  void streamProductIteratorReportsExhaustion() {
    Iterator<Pair<Integer, String>> pairs = cartesianProductStream(List.of(1, 2), List.of("a")).iterator();

    assertEquals(Pair.of(1, "a"), pairs.next());
    assertEquals(Pair.of(2, "a"), pairs.next());
    assertFalse(pairs.hasNext());
    assertThrows(NoSuchElementException.class, pairs::next);
  }

  @Test
  void streamProductsRejectNullInputsBeforeTraversal() {
    assertThrows(NullPointerException.class, () -> cartesianProductStream((Collection<Integer>) null, List.<String>of()));
    assertThrows(NullPointerException.class, () -> cartesianProductStream(List.<Integer>of(), (Collection<String>) null));
  }

  private static <T> List<T> drain(Iterator<T> iterator) {
    List<T> values = new ArrayList<>();
    iterator.forEachRemaining(values::add);
    return values;
  }

  private static final class CountingCollection<T> extends AbstractCollection<T> {

    private final List<T> values;
    private int visited;

    private CountingCollection(List<T> values) {
      this.values = values;
    }

    @Override
    public Iterator<T> iterator() {
      Iterator<T> iterator = values.iterator();
      return new Iterator<>() {
        @Override
        public boolean hasNext() {
          return iterator.hasNext();
        }

        @Override
        public T next() {
          T value = iterator.next();
          visited++;
          return value;
        }
      };
    }

    @Override
    public int size() {
      return values.size();
    }
  }

  private record Value(String name) {
  }
}
