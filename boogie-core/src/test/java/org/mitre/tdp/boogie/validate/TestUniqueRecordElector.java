package org.mitre.tdp.boogie.validate;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;

class TestUniqueRecordElector {

  private static final UniqueRecordElector<String> elector = new UniqueRecordElector<>(s -> s.substring(0, 1), list -> list.get(0));

  @Test
  void testUniqueRecordCollector() {
    List<String> strings = newArrayList("A1", "A2", "A3", "B1", "B2", "C1", "D1");

    Collection<String> actual = elector.apply(strings);
    assertEquals(newArrayList("A1", "B1", "C1", "D1"), actual);
  }
}
