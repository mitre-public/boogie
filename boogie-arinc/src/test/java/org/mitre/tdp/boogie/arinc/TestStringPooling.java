package org.mitre.tdp.boogie.arinc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.v18.field.AirportHeliportIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.BlankSpec;
import org.mitre.tdp.boogie.arinc.v18.field.FixIdentifier;
import org.mitre.tdp.boogie.arinc.v18.field.RecommendedNavaid;

class TestStringPooling {

  @Test
  void sharesIdentifiersAcrossFieldsAndRecordSpecifications() {
    FixIdentifier fix = new FixIdentifier();
    RecommendedNavaid navaid = new RecommendedNavaid();
    AirportHeliportIdentifier airport = new AirportHeliportIdentifier();
    ArincRecordParser parser = ArincRecordParser.standard(
        recordSpec("F", new RecordField<>("fix", fix), new RecordField<>("navaid", navaid)),
        recordSpec("A", new RecordField<>("airport", airport), new RecordField<>("fix", fix))
    );

    ArincRecord first = parser.parse("FBHM  BHM ").orElseThrow();
    ArincRecord second = parser.parse("ABHM BHM  ").orElseThrow();
    String identifier = first.requiredField("fix");

    assertEquals("BHM", identifier);
    assertSame(identifier, first.requiredField("navaid"));
    assertSame(identifier, second.requiredField("airport"));
    assertSame(identifier, second.requiredField("fix"));
    assertSame(fix, first.specForField("fix").orElseThrow());
    assertSame(navaid, first.specForField("navaid").orElseThrow());
    assertSame(airport, second.specForField("airport").orElseThrow());
  }

  @Test
  void preservesTrimmingBlanksAndRawFields() {
    TrimmableString text = new WidthRestrictedString(8, 8);
    ArincRecordParser parser = ArincRecordParser.standard(recordSpec("T", new RecordField<>("text", text)));
    String padded = "\u0000\t BHM \u001f";
    ArincRecord record = parser.parse("T" + padded).orElseThrow();
    ArincRecord blank = parser.parse("T\u0000\t    \r\n").orElseThrow();
    ArincRecord nonAsciiPadding = parser.parse("T \u00a0BHM\u00a0  ").orElseThrow();

    assertEquals("BHM", record.requiredField("text"));
    assertEquals(padded, record.rawField("text"));
    assertEquals("T" + padded, record.rawRecord());
    assertEquals(Optional.empty(), blank.optionalField("text"));
    assertEquals("\u00a0BHM\u00a0", nonAsciiPadding.requiredField("text"));
    assertEquals(Optional.of("BHM"), text.apply(padded));
  }

  @Test
  void honorsCustomSourceWidthValidationBeforePooling() {
    TrimmableString accepted = new WidthRestrictedString(5, 5);
    TrimmableString rejected = new WidthRestrictedString(5, 4);
    ArincRecordParser parser = ArincRecordParser.standard(recordSpec(
        "T", new RecordField<>("accepted", accepted), new RecordField<>("rejected", rejected)));
    ArincRecord record = parser.parse("T BHM  BHM ").orElseThrow();

    assertEquals(Optional.of("BHM"), record.optionalField("accepted"));
    assertEquals(Optional.empty(), record.optionalField("rejected"));
    assertEquals(Optional.of("BHM"), accepted.apply(" BHM "));
    assertEquals(Optional.empty(), accepted.apply("BHM"));
    assertEquals(Optional.of("BHM"), rejected.apply("BHM "));
    assertThrows(IndexOutOfBoundsException.class, () -> accepted.parse("BHM", -1, 3));
    assertThrows(IndexOutOfBoundsException.class, () -> accepted.parse("BHM", 0, 4));
  }

  @Test
  void hashCollisionsPreserveValuesAndPreviouslyDecodedRecords() {
    ArincRecordParser parser = identifierParser();
    assertEquals("Aa".hashCode(), "BB".hashCode());

    ArincRecord first = parser.parse("FAa   ").orElseThrow();
    String original = first.requiredField("fix");
    for (String expected : List.of("BB", "Aa", "BB", "Aa")) {
      ArincRecord record = parser.parse(identifierRecord(expected)).orElseThrow();
      assertEquals(expected, record.requiredField("fix"));
    }

    assertEquals("Aa", first.requiredField("fix"));
    assertSame(original, first.requiredField("fix"));
  }

  @Test
  void directFieldParsingAndSeparateParsersShareShortValues() {
    RecordSpec spec = recordSpec("F", new RecordField<>("fix", new FixIdentifier()));
    ArincRecordParser first = ArincRecordParser.standard(spec);
    ArincRecordParser second = ArincRecordParser.standard(spec);
    String firstIdentifier = first.parse("FBHM  ").orElseThrow().requiredField("fix");
    String secondIdentifier = second.parse("FBHM  ").orElseThrow().requiredField("fix");

    assertEquals(firstIdentifier, secondIdentifier);
    assertSame(firstIdentifier, secondIdentifier);
    assertSame(firstIdentifier, new FixIdentifier().apply(" BHM ").orElseThrow());
    assertSame(firstIdentifier, new AirportHeliportIdentifier().parse("!BHM !", 1, 5).orElseThrow());
    assertSame(firstIdentifier, first.parse("FBHM  ").orElseThrow().requiredField("fix"));
    assertSame(secondIdentifier, second.parse("FBHM  ").orElseThrow().requiredField("fix"));
  }

  @Test
  void leavesLongDescriptionsUnpooled() {
    String description = "A LONG AIRPORT DESCRIPTION";
    TrimmableString text = new WidthRestrictedString(description.length(), description.length());
    ArincRecordParser parser = ArincRecordParser.standard(recordSpec("T", new RecordField<>("text", text)));
    String first = parser.parse("T" + description).orElseThrow().requiredField("text");
    String second = parser.parse("T" + description).orElseThrow().requiredField("text");

    assertEquals(description, first);
    assertEquals(first, second);
    assertNotSame(first, second);
  }

  @Test
  void poolHitsShareResultsAcrossOffsetsAndPreserveUnicode() {
    StringPool pool = new StringPool();
    for (String value : List.of("BHM", " BHM ", "\u00e9", "\u0100", "\ud83d\ude80", "\ud83d", "\ude80", "A\u00a0B")) {
      Optional<String> parsed = pool.canonicalize("!" + value + "?", 1, 1 + value.length());
      assertEquals(Optional.of(value), parsed);
      assertSame(parsed, pool.canonicalize(value, 0, value.length()));
      assertSame(parsed, pool.canonicalize("\ud83d\ude80|" + value + "|suffix", 3, 3 + value.length()));
    }
  }

  @Test
  void poolCachesEightUtf16CharactersAndBypassesNineWithoutEviction() {
    StringPool pool = new StringPool();
    for (String value : List.of("ABCDEFGH", "\ud83d\ude80".repeat(4))) {
      assertEquals(8, value.length());
      Optional<String> cached = pool.canonicalize("!" + value + "?", 1, 9);
      assertSame(cached, pool.canonicalize("prefix" + value, 6, 14));

      String longValue = "\u0000" + value;
      assertEquals(9, longValue.length());
      assertEquals(value.hashCode(), longValue.hashCode(), "Bypassing the pool must not evict this colliding short value");
      Optional<String> first = pool.canonicalize("!" + longValue + "?", 1, 10);
      Optional<String> second = pool.canonicalize("prefix" + longValue, 6, 15);
      assertEquals(Optional.of(longValue), first);
      assertEquals(first, second);
      assertNotSame(first, second);
      assertNotSame(first.orElseThrow(), second.orElseThrow());
      assertSame(cached, pool.canonicalize(value, 0, value.length()));
    }
  }

  @Test
  void poolPreservesStringHashCollisionsIncludingDifferentLengths() {
    List<List<String>> collisions = List.of(
        List.of("Aa", "BB"),
        List.of("\u0008\u0008", "\u0100"),
        List.of("\ud83d\ude80", "\ud83e\ude61"),
        List.of("", "\u0000"),
        List.of("\u0000", "\u0000\u0000")
    );
    for (List<String> pair : collisions) {
      StringPool pool = new StringPool();
      String original = pair.get(0);
      String replacement = pair.get(1);
      assertEquals(original.hashCode(), replacement.hashCode());
      Optional<String> before = pool.canonicalize("!" + original + "?", 1, 1 + original.length());
      Optional<String> after = pool.canonicalize("prefix" + replacement, 6, 6 + replacement.length());
      assertEquals(Optional.of(original), before);
      assertEquals(Optional.of(replacement), after);
      assertNotSame(before, after);
      assertSame(after, pool.canonicalize(replacement, 0, replacement.length()));

      Optional<String> reloaded = pool.canonicalize(original, 0, original.length());
      assertEquals(before, reloaded);
      assertNotSame(before, reloaded, "The colliding value must have replaced the previous cache entry");
      assertSame(reloaded, pool.canonicalize("prefix" + original, 6, 6 + original.length()));
    }
  }

  @Test
  void poolValidatesSourceRangesAndAcceptsEmptySlices() {
    StringPool pool = new StringPool();
    for (int[] range : new int[][]{{-1, 1}, {0, 6}, {4, 2}, {-1, -1}, {6, 6}}) {
      assertThrows(IndexOutOfBoundsException.class, () -> pool.canonicalize("ABCDE", range[0], range[1]));
    }
    assertThrows(NullPointerException.class, () -> pool.canonicalize(null, 0, 0));
    assertEquals(Optional.of(""), pool.canonicalize("ABCDE", 5, 5));
    assertSame(pool.canonicalize("", 0, 0), pool.canonicalize("ABCDE", 2, 2));
  }

  @Test
  void decodesDistinctRecordsConcurrentlyEvenWhenValuesCollide() throws Exception {
    ArincRecordParser parser = identifierParser();
    List<String> identifiers = List.of("Aa", "BB", "BHM", "SHARP", "KJFK");
    List<ArincRecord> records = IntStream.range(0, 2_000)
        .mapToObj(index -> parser.parse(identifierRecord(identifiers.get(index % identifiers.size()))).orElseThrow())
        .toList();
    int workerCount = 4;
    ExecutorService executor = Executors.newFixedThreadPool(workerCount);
    CountDownLatch start = new CountDownLatch(1);

    try {
      List<Future<?>> workers = new ArrayList<>();
      for (int worker = 0; worker < workerCount; worker++) {
        int firstRecord = worker;
        workers.add(executor.submit(() -> {
          start.await();
          for (int index = firstRecord; index < records.size(); index += workerCount) {
            ArincRecord record = records.get(index);
            String identifier = record.requiredField("fix");
            assertEquals(identifiers.get(index % identifiers.size()), identifier);
            assertSame(identifier, record.requiredField("fix"));
            assertEquals(identifier, new FixIdentifier().apply(identifier).orElseThrow());
          }
          return null;
        }));
      }
      start.countDown();
      for (Future<?> worker : workers) {
        worker.get(30, TimeUnit.SECONDS);
      }
    } finally {
      start.countDown();
      executor.shutdownNow();
    }
  }

  private static ArincRecordParser identifierParser() {
    return ArincRecordParser.standard(recordSpec("F", new RecordField<>("fix", new FixIdentifier())));
  }

  private static String identifierRecord(String identifier) {
    return "F" + identifier + " ".repeat(5 - identifier.length());
  }

  private static RecordSpec recordSpec(String prefix, RecordField<?>... recordFields) {
    List<RecordField<?>> fields = new ArrayList<>();
    fields.add(new RecordField<>("recordType", new BlankSpec(prefix.length())));
    fields.addAll(List.of(recordFields));
    return new RecordSpec(List.of(RecordDiscriminator.prefix(prefix))) {
      @Override
      public int recordLength() {
        return fields.stream().mapToInt(field -> field.fieldSpec().fieldLength()).sum();
      }

      @Override
      public List<RecordField<?>> recordFields() {
        return fields;
      }
    };
  }

  private static final class WidthRestrictedString extends TrimmableString {

    private final int fieldLength;
    private final int acceptedSourceLength;

    private WidthRestrictedString(int fieldLength, int acceptedSourceLength) {
      this.fieldLength = fieldLength;
      this.acceptedSourceLength = acceptedSourceLength;
    }

    @Override
    public int fieldLength() {
      return fieldLength;
    }

    @Override
    public String fieldCode() {
      return "TEST";
    }

    @Override
    protected boolean acceptsSourceLength(int length) {
      return length == acceptedSourceLength;
    }
  }
}
