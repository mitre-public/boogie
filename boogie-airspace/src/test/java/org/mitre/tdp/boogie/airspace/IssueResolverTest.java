package org.mitre.tdp.boogie.airspace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

class IssueResolverTest {
  @Test
  void identifiesWithoutResolvingAndPassesThoseFindingsToAnExplicitResolution() {
    AtomicInteger resolutions = new AtomicInteger();
    IssueResolver<String, Integer> repair = new IssueResolver<>(
        value -> value.endsWith(" ") ? List.of(value.length() - 1) : List.of(),
        (value, issues) -> {
          resolutions.incrementAndGet();
          return value.substring(0, issues.get(0));
        });

    String source = "boundary ";
    List<Integer> issues = repair.identify(source);
    assertEquals(List.of(8), issues);
    assertEquals(0, resolutions.get(), "Inspection alone must not repair data");
    assertEquals("boundary", repair.resolve(source, issues));
    assertEquals("boundary ", source);
    assertEquals(1, resolutions.get());
  }

  @Test
  void leavesCleanInputsAloneWithoutCallingTheResolver() {
    IssueResolver<List<String>, String> repair = IssueResolver.of(
        value -> List.of(),
        (value, issues) -> fail("A clean value must not need resolution"));

    List<String> source = List.of("clean");
    assertSame(source, repair.apply(source));
  }

  @Test
  void snapshotsFindingsBeforeGivingThemToTheResolver() {
    List<String> findings = new ArrayList<>(List.of("issue"));
    IssueResolver<String, String> repair = IssueResolver.of(
        value -> findings,
        (value, issues) -> {
          findings.clear();
          assertEquals(List.of("issue"), issues, "Changes to detector state must not change this resolution's findings");
          assertThrows(UnsupportedOperationException.class, issues::clear);
          return "resolved";
        });

    assertEquals("resolved", repair.apply("source"));
  }

  @Test
  void composedPoliciesInspectThePrecedingResult() {
    List<Integer> inspected = new ArrayList<>();
    IssueResolver<Integer, String> removeNegative = IssueResolver.of(
        value -> {
          inspected.add(value);
          return value < 0 ? List.of("negative") : List.of();
        },
        (value, issues) -> 0);
    IssueResolver<Integer, String> removeZero = IssueResolver.of(
        value -> {
          inspected.add(value);
          return value == 0 ? List.of("zero") : List.of();
        },
        (value, issues) -> 1);

    assertEquals(1, removeNegative.andThen(removeZero).apply(-2));
    assertEquals(List.of(-2, 0), inspected);
  }
}
