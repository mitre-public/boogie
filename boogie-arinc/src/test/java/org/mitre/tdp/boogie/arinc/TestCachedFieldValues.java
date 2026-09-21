package org.mitre.tdp.boogie.arinc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.PathTerm;
import org.mitre.tdp.boogie.arinc.v18.field.TurnDirection;

class TestCachedFieldValues {

  private static final Map<FieldSpec<String>, String> ROUTE_QUALIFIERS = Map.of(
      new org.mitre.tdp.boogie.arinc.v18.field.RouteTypeQualifier(), "ABCDEJLNPRSTUVW",
      new org.mitre.tdp.boogie.arinc.v19.field.RouteTypeQualifier(), "ABCDEFHIJLNPRSTUVW",
      new org.mitre.tdp.boogie.arinc.v21.field.RouteTypeQualifier(), "ABCDEFGHIJLNPRSTUVWXYZ",
      new org.mitre.tdp.boogie.arinc.v22.field.RouteTypeQualifier(), "ABCDEFGHIJLMNPRSTUVWXYZ"
  );

  private static final Map<FieldSpec<String>, String> ALTITUDE_DESCRIPTIONS = Map.of(
      new org.mitre.tdp.boogie.arinc.v18.field.AltitudeDescription(), "+-@ BCGHIJVXY",
      new org.mitre.tdp.boogie.arinc.v19.field.AltitudeDescription(), "+-@ BCDGHIJVXY",
      new org.mitre.tdp.boogie.arinc.v20.field.AltitudeDescription(), "+-@ BCDGVXY",
      new org.mitre.tdp.boogie.arinc.v21.field.AltitudeDescription(), "+-@ BCDGOVXY",
      new org.mitre.tdp.boogie.arinc.v22.field.AltitudeDescription(), "+-@ BCDGO"
  );

  @Test
  void customerAreaCodesAcceptOnlyTheirTenNames() {
    List<CustomerAreaCode> expected = List.of(
        CustomerAreaCode.USA, CustomerAreaCode.CAN, CustomerAreaCode.PAC, CustomerAreaCode.LAM,
        CustomerAreaCode.SAM, CustomerAreaCode.SPA, CustomerAreaCode.EUR, CustomerAreaCode.EEU,
        CustomerAreaCode.MES, CustomerAreaCode.AFR
    );
    for (CustomerAreaCode code : expected) {
      assertParsed(CustomerAreaCode.SPEC, code.name(), Optional.of(code));
      assertParsed(CustomerAreaCode.SPEC, " \t" + code.name() + "\r\n", Optional.of(code));
      assertParsed(CustomerAreaCode.SPEC, code.name().toLowerCase(Locale.ROOT), Optional.empty());
    }
    for (String invalid : List.of("", " ", "SPEC", "U", "US", "USAA", "ZZZ", "U SA", "USA CAN")) {
      assertParsed(CustomerAreaCode.SPEC, invalid, Optional.empty());
    }
    for (char character = 0; character < 128; character++) {
      String input = "US" + character;
      assertParsed(CustomerAreaCode.SPEC, input,
          character == 'A' ? Optional.of(CustomerAreaCode.USA) : Optional.empty());
    }
  }

  @Test
  void pathTermsMatchTheKnownTwoLetterVocabulary() {
    Set<String> expected = Set.of(
        "IF", "TF", "CF", "DF", "FA", "FC", "FD", "FM", "CA", "CD", "CI", "CR",
        "RF", "AF", "VA", "VD", "VI", "VM", "VR", "PI", "HA", "HF", "HM"
    );
    FieldSpec<PathTerminator> field = new PathTerm();
    for (char first = 0; first < 128; first++) {
      for (char second = 0; second < 128; second++) {
        String input = new String(new char[]{first, second});
        assertParsed(field, input, expected.contains(input)
            ? Optional.of(PathTerminator.valueOf(input)) : Optional.empty());
      }
    }
    for (String invalid : List.of("", "T", "TF ", " TF", " tf ", "T F", "T\u00a0", "\u0100F")) {
      assertParsed(field, invalid, Optional.empty());
    }
  }

  @Test
  void turnDirectionsRequireOneExactUppercaseCharacter() {
    Map<String, TurnDirection> expected = Map.of("L", TurnDirection.L, "R", TurnDirection.R, "E", TurnDirection.E);
    for (char character = 0; character < 128; character++) {
      String input = String.valueOf(character);
      assertParsed(TurnDirection.SPEC, input, Optional.ofNullable(expected.get(input)));
    }
    for (String invalid : List.of("", "SPEC", "LL", " L", "R ", "\tE", "\u014c")) {
      assertParsed(TurnDirection.SPEC, invalid, Optional.empty());
    }
  }

  @Test
  void routeQualifierVocabularyAndTrimmingFollowEachVersion() {
    ROUTE_QUALIFIERS.forEach((field, allowed) -> {
      for (char character = 0; character < 128; character++) {
        String input = String.valueOf(character);
        Optional<String> expected = allowed.indexOf(character) >= 0 ? Optional.of(input) : Optional.empty();
        assertParsed(field, input, expected);
        assertParsed(field, " \t" + input + "\r\n", expected);
      }
      for (String invalid : List.of("", " ", "AA", "A B", "SPEC")) {
        assertParsed(field, invalid, Optional.empty());
      }
    });
  }

  @Test
  void altitudeDescriptionsPreserveVersionChangesAndBlankMapping() {
    ALTITUDE_DESCRIPTIONS.forEach((field, allowed) -> {
      for (char character = 0; character < 128; character++) {
        String input = String.valueOf(character);
        Optional<String> expected = allowed.indexOf(character) >= 0
            ? Optional.of(character == ' ' ? "@" : input) : Optional.empty();
        assertParsed(field, input, expected);
      }
      for (String invalid : List.of("", "  ", " B", "B ", "BB", "\tB\n")) {
        assertParsed(field, invalid, Optional.empty());
      }
    });
  }

  @Test
  void onlyCustomerCodesAndRouteQualifiersTrimAsciiPadding() {
    for (char padding = 0; padding <= ' '; padding++) {
      assertParsed(CustomerAreaCode.SPEC, padding + "USA" + padding, Optional.of(CustomerAreaCode.USA));
      for (FieldSpec<String> field : ROUTE_QUALIFIERS.keySet()) {
        assertParsed(field, padding + "A" + padding, Optional.of("A"));
        assertParsed(field, String.valueOf(padding), Optional.empty());
      }
      assertParsed(new PathTerm(), padding + "TF" + padding, Optional.empty());
      assertParsed(TurnDirection.SPEC, padding + "L" + padding, Optional.empty());
      for (FieldSpec<String> field : ALTITUDE_DESCRIPTIONS.keySet()) {
        assertParsed(field, padding + "B" + padding, Optional.empty());
      }
    }
    for (String padding : List.of("\u0085", "\u00a0", "\u1680", "\u2003", "\u2028", "\u202f", "\u3000", "\ufeff")) {
      assertParsed(CustomerAreaCode.SPEC, padding + "USA" + padding, Optional.empty());
      for (FieldSpec<String> field : ROUTE_QUALIFIERS.keySet()) {
        assertParsed(field, padding + "A" + padding, Optional.empty());
        assertParsed(field, padding, Optional.empty());
      }
      for (FieldSpec<String> field : ALTITUDE_DESCRIPTIONS.keySet()) {
        assertParsed(field, padding, Optional.empty());
      }
    }
  }

  @Test
  void parsersValidateRangesEvenWhenTheirContentsWouldBeRejected() {
    List<FieldSpec<?>> fields = new ArrayList<>(ROUTE_QUALIFIERS.keySet());
    fields.addAll(ALTITUDE_DESCRIPTIONS.keySet());
    fields.add(CustomerAreaCode.SPEC);
    fields.add(new PathTerm());
    fields.add(TurnDirection.SPEC);
    for (FieldSpec<?> field : fields) {
      String description = field.getClass().getName();
      assertThrows(IndexOutOfBoundsException.class, () -> field.parse("???", -1, 1), description);
      assertThrows(IndexOutOfBoundsException.class, () -> field.parse("???", 0, 4), description);
      assertThrows(IndexOutOfBoundsException.class, () -> field.parse("???", 2, 1), description);
      assertThrows(IndexOutOfBoundsException.class, () -> field.parse("???", -1, -1), description);
      assertThrows(IndexOutOfBoundsException.class, () -> field.parse("???", 4, 4), description);
      assertEquals(Optional.empty(), field.parse("???", 1, 1), description);
    }
  }

  private static <T> void assertParsed(FieldSpec<T> field, String input, Optional<T> expected) {
    String description = field.getClass().getName() + " input " + input.chars().boxed().toList();
    Optional<T> standalone = field.apply(input);
    Optional<T> embedded = field.parse("prefix" + input + "suffix", 6, 6 + input.length());
    assertEquals(expected, standalone, description);
    assertEquals(expected, embedded, description + " at nonzero offset");
    if (expected.isPresent()) {
      assertSame(standalone, embedded, description + " should reuse the parsed result");
      assertSame(standalone, field.apply(input), description + " on repeated parsing");
    }
  }
}
