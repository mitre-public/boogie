package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.database.ArincFixDatabase;
import org.mitre.tdp.boogie.arinc.database.ArincTerminalAreaDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincAirport;
import org.mitre.tdp.boogie.arinc.model.ArincGnssLandingSystem;
import org.mitre.tdp.boogie.arinc.model.ArincHelipad;
import org.mitre.tdp.boogie.arinc.model.ArincHeliport;
import org.mitre.tdp.boogie.arinc.model.ArincLocalizerGlideSlope;
import org.mitre.tdp.boogie.arinc.model.ArincModel;
import org.mitre.tdp.boogie.arinc.model.ArincNdbNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;
import org.mitre.tdp.boogie.arinc.model.ArincVhfNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincWaypoint;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

class TestFixDispatch {

  private static final String IDENTIFIER = "FIX01";
  private static final String AIRPORT = "KAAA";
  private static final String REGION = "K2";

  private static final List<DispatchCase<?>> SUPPORTED = List.of(
      new DispatchCase<>(SectionCode.P, "A", ArincAirport.class,
          (fix, terminal) -> fix.airport(IDENTIFIER, REGION), FixAssemblyStrategy::convertAirport),
      new DispatchCase<>(SectionCode.H, "A", ArincHeliport.class,
          (fix, terminal) -> fix.heliport(IDENTIFIER, REGION), FixAssemblyStrategy::convertHeliport),
      new DispatchCase<>(SectionCode.D, "B", ArincNdbNavaid.class,
          (fix, terminal) -> fix.enrouteNdbNavaid(IDENTIFIER, REGION), FixAssemblyStrategy::convertNdbNavaid),
      new DispatchCase<>(SectionCode.P, "N", ArincNdbNavaid.class,
          (fix, terminal) -> fix.terminalNdbNavaid(IDENTIFIER, REGION), FixAssemblyStrategy::convertNdbNavaid),
      new DispatchCase<>(SectionCode.D, null, ArincVhfNavaid.class,
          (fix, terminal) -> fix.vhfNavaid(IDENTIFIER, REGION), FixAssemblyStrategy::convertVhfNavaid),
      new DispatchCase<>(SectionCode.D, "", ArincVhfNavaid.class,
          (fix, terminal) -> fix.vhfNavaid(IDENTIFIER, REGION), FixAssemblyStrategy::convertVhfNavaid),
      new DispatchCase<>(SectionCode.E, "A", ArincWaypoint.class,
          (fix, terminal) -> fix.enrouteWaypoint(IDENTIFIER, REGION), FixAssemblyStrategy::convertWaypoint),
      new DispatchCase<>(SectionCode.P, "C", ArincWaypoint.class,
          (fix, terminal) -> terminal.waypointAt(AIRPORT, REGION, IDENTIFIER), FixAssemblyStrategy::convertWaypoint),
      new DispatchCase<>(SectionCode.H, "C", ArincWaypoint.class,
          (fix, terminal) -> terminal.heliportsWaypoint(AIRPORT, REGION, IDENTIFIER), FixAssemblyStrategy::convertWaypoint),
      new DispatchCase<>(SectionCode.P, "G", ArincRunway.class,
          (fix, terminal) -> terminal.runwayAt(AIRPORT, IDENTIFIER), FixAssemblyStrategy::convertRunway),
      new DispatchCase<>(SectionCode.P, "I", ArincLocalizerGlideSlope.class,
          (fix, terminal) -> terminal.localizerGlideSlopeAt(AIRPORT, IDENTIFIER), FixAssemblyStrategy::convertLocalizerGlideSlope),
      new DispatchCase<>(SectionCode.H, "I", ArincLocalizerGlideSlope.class,
          (fix, terminal) -> terminal.heliportsGlideSlopeAt(AIRPORT, IDENTIFIER), FixAssemblyStrategy::convertLocalizerGlideSlope),
      new DispatchCase<>(SectionCode.P, "T", ArincGnssLandingSystem.class,
          (fix, terminal) -> terminal.gnssLandingSystemAt(AIRPORT, IDENTIFIER), FixAssemblyStrategy::convertGnssLandingSystem),
      new DispatchCase<>(SectionCode.H, "T", ArincGnssLandingSystem.class,
          (fix, terminal) -> terminal.heliportsGnssLandingSystemAt(AIRPORT, IDENTIFIER), FixAssemblyStrategy::convertGnssLandingSystem),
      new DispatchCase<>(SectionCode.P, "H", ArincHelipad.class,
          (fix, terminal) -> terminal.helipadAt(AIRPORT, REGION, IDENTIFIER), FixAssemblyStrategy::convertHelipad),
      new DispatchCase<>(SectionCode.H, "H", ArincHelipad.class,
          (fix, terminal) -> terminal.heliportsHelipadAt(AIRPORT, REGION, IDENTIFIER), FixAssemblyStrategy::convertHelipad)
  );

  @Test
  void supportedAddressesSelectTheExactAssemblyStrategy() {
    SUPPORTED.forEach(TestFixDispatch::assertAssemblyDispatch);
  }

  @Test
  void supportedAddressesSelectTheExactDatabaseLookup() {
    SUPPORTED.forEach(TestFixDispatch::assertLookupDispatch);
  }

  @Test
  void missingLookupsReturnEmptyWithoutCallingTheAssembler() {
    ArincFixDatabase fix = mock(ArincFixDatabase.class);
    ArincTerminalAreaDatabase terminal = mock(ArincTerminalAreaDatabase.class);
    FixAssembler<Object> assembler = assembler();
    FixDereferencer<Object> dereferencer = new FixDereferencer<>(assembler, terminal, fix);
    for (DispatchCase<?> address : SUPPORTED) {
      assertEquals(Optional.empty(), dereferencer.dereference(IDENTIFIER, AIRPORT, REGION, address.section(), address.subsection()));
    }
    verifyNoInteractions(assembler);
  }

  @Test
  void runwayLookupPrefersTheAirportAndFallsBackToTheHeliport() {
    ArincFixDatabase fix = mock(ArincFixDatabase.class);
    ArincTerminalAreaDatabase terminal = mock(ArincTerminalAreaDatabase.class);
    FixDereferencer<ArincModel> dereferencer = new FixDereferencer<>(model -> model, terminal, fix);
    ArincRunway airportRunway = mock(ArincRunway.class);
    ArincRunway heliportRunway = mock(ArincRunway.class);
    when(terminal.runwayAt(AIRPORT, IDENTIFIER)).thenReturn(Optional.of(airportRunway));
    when(terminal.heliportsRunwayAt(AIRPORT, IDENTIFIER)).thenReturn(Optional.of(heliportRunway));

    assertSame(airportRunway, dereferencer.dereference(IDENTIFIER, AIRPORT, REGION, SectionCode.P, "G").orElseThrow());
    verify(terminal, never()).heliportsRunwayAt(AIRPORT, IDENTIFIER);

    when(terminal.runwayAt(AIRPORT, IDENTIFIER)).thenReturn(Optional.empty());
    clearInvocations(terminal);
    assertSame(heliportRunway, dereferencer.dereference(IDENTIFIER, AIRPORT, REGION, SectionCode.P, "G").orElseThrow());
    verify(terminal).runwayAt(AIRPORT, IDENTIFIER);
    verify(terminal).heliportsRunwayAt(AIRPORT, IDENTIFIER);
    verifyNoMoreInteractions(terminal);
    verifyNoInteractions(fix);
  }

  @Test
  void nullAirportSkipsGuardedLookupsButIsForwardedForTerminalWaypoints() {
    ArincFixDatabase fix = mock(ArincFixDatabase.class);
    ArincTerminalAreaDatabase terminal = mock(ArincTerminalAreaDatabase.class);
    FixDereferencer<ArincModel> dereferencer = new FixDereferencer<>(model -> model, terminal, fix);
    for (SectionCode section : List.of(SectionCode.P, SectionCode.H)) {
      for (String subsection : List.of("I", "T", "H")) {
        assertEquals(Optional.empty(), dereferencer.dereference(IDENTIFIER, null, REGION, section, subsection));
      }
    }
    assertEquals(Optional.empty(), dereferencer.dereference(IDENTIFIER, null, REGION, SectionCode.P, "G"));
    verifyNoInteractions(fix, terminal);

    ArincWaypoint airportWaypoint = mock(ArincWaypoint.class);
    ArincWaypoint heliportWaypoint = mock(ArincWaypoint.class);
    when(terminal.waypointAt(null, REGION, IDENTIFIER)).thenReturn(Optional.of(airportWaypoint));
    when(terminal.heliportsWaypoint(null, REGION, IDENTIFIER)).thenReturn(Optional.of(heliportWaypoint));
    assertSame(airportWaypoint, dereferencer.dereference(IDENTIFIER, null, REGION, SectionCode.P, "C").orElseThrow());
    assertSame(heliportWaypoint, dereferencer.dereference(IDENTIFIER, null, REGION, SectionCode.H, "C").orElseThrow());
    verify(terminal).waypointAt(null, REGION, IDENTIFIER);
    verify(terminal).heliportsWaypoint(null, REGION, IDENTIFIER);
    verifyNoMoreInteractions(terminal);
  }

  @Test
  void unsupportedAddressesRetainTheLegacyExceptionAndMessage() {
    ArincFixDatabase fix = mock(ArincFixDatabase.class);
    ArincTerminalAreaDatabase terminal = mock(ArincTerminalAreaDatabase.class);
    FixAssemblyStrategy<Object> strategy = strategy();
    FixAssembler<Object> assembler = FixAssembler.withStrategy(strategy);
    FixDereferencer<Object> dereferencer = new FixDereferencer<>(assembler, terminal, fix);
    List<Address> unsupported = List.of(
        new Address(SectionCode.H, "G"), new Address(SectionCode.H, "N"),
        new Address(SectionCode.D, " "), new Address(SectionCode.D, "BB"),
        new Address(SectionCode.P, "n"), new Address(SectionCode.P, "a"),
        new Address(SectionCode.P, "AA"), new Address(SectionCode.P, null),
        new Address(SectionCode.E, "A "), new Address(SectionCode.E, ""),
        new Address(SectionCode.SPEC, "A"), new Address(SectionCode.U, "C"),
        new Address(SectionCode.A, ""), new Address(SectionCode.R, null), new Address(SectionCode.T, "A")
    );
    for (Address address : unsupported) {
      ArincModel model = model(ArincModel.class, address.section(), address.subsection());
      String expectedMessage = unknownAddress(address.section(), address.subsection());
      assertEquals(expectedMessage, assertThrows(IllegalStateException.class, () -> assembler.assemble(model)).getMessage());
      assertEquals(expectedMessage, assertThrows(IllegalStateException.class,
          () -> dereferencer.dereference(IDENTIFIER, AIRPORT, REGION, address.section(), address.subsection())).getMessage());
    }
    verifyNoInteractions(fix, terminal, strategy);
  }

  @Test
  void recognizedAddressesStillRejectModelsOfTheWrongType() {
    FixAssemblyStrategy<Object> strategy = strategy();
    FixAssembler<Object> assembler = FixAssembler.withStrategy(strategy);
    for (DispatchCase<?> address : SUPPORTED) {
      ArincModel wrongType = model(ArincModel.class, address.section(), address.subsection());
      assertThrows(ClassCastException.class, () -> assembler.assemble(wrongType));
    }
    verifyNoInteractions(strategy);
  }

  @Test
  void nullModelAndSectionRemainInvalid() {
    FixAssemblyStrategy<Object> strategy = strategy();
    FixAssembler<Object> assembler = FixAssembler.withStrategy(strategy);
    ArincModel noSection = mock(ArincModel.class);
    assertThrows(NullPointerException.class, () -> assembler.assemble(null));
    assertThrows(NullPointerException.class, () -> assembler.assemble(noSection));
    verify(noSection, never()).subSectionCode();

    ArincModel noSubsectionOptional = mock(ArincModel.class);
    when(noSubsectionOptional.sectionCode()).thenReturn(SectionCode.D);
    when(noSubsectionOptional.subSectionCode()).thenReturn(null);
    assertThrows(NullPointerException.class, () -> assembler.assemble(noSubsectionOptional));

    ArincFixDatabase fix = mock(ArincFixDatabase.class);
    ArincTerminalAreaDatabase terminal = mock(ArincTerminalAreaDatabase.class);
    FixDereferencer<Object> dereferencer = new FixDereferencer<>(assembler, terminal, fix);
    assertThrows(NullPointerException.class, () -> dereferencer.dereference(IDENTIFIER, AIRPORT, REGION, null, "A"));
    verifyNoInteractions(fix, terminal, strategy);
  }

  @Test
  void navaidFallbackPreservesItsExistingOrderAndUnsupportedLowercaseAddress() {
    ArincFixDatabase fix = mock(ArincFixDatabase.class);
    ArincTerminalAreaDatabase terminal = mock(ArincTerminalAreaDatabase.class);
    FixDereferencer<ArincModel> dereferencer = new FixDereferencer<>(model -> model, terminal, fix);
    ArincVhfNavaid vhf = mock(ArincVhfNavaid.class);
    ArincNdbNavaid ndb = mock(ArincNdbNavaid.class);
    when(fix.vhfNavaid(IDENTIFIER, REGION)).thenReturn(Optional.of(vhf));
    when(fix.enrouteNdbNavaid(IDENTIFIER, REGION)).thenReturn(Optional.of(ndb));
    assertSame(vhf, dereferencer.dereferenceNavaid(IDENTIFIER, REGION).orElseThrow());
    verify(fix, never()).enrouteNdbNavaid(IDENTIFIER, REGION);

    when(fix.vhfNavaid(IDENTIFIER, REGION)).thenReturn(Optional.empty());
    assertSame(ndb, dereferencer.dereferenceNavaid(IDENTIFIER, REGION).orElseThrow());

    when(fix.enrouteNdbNavaid(IDENTIFIER, REGION)).thenReturn(Optional.empty());
    assertEquals(unknownAddress(SectionCode.P, "n"), assertThrows(IllegalStateException.class,
        () -> dereferencer.dereferenceNavaid(IDENTIFIER, REGION)).getMessage());
    verify(fix, never()).terminalNdbNavaid(IDENTIFIER, REGION);
    verifyNoInteractions(terminal);
  }

  private static <T extends ArincModel> void assertAssemblyDispatch(DispatchCase<T> address) {
    T model = model(address.modelClass(), address.section(), address.subsection());
    FixAssemblyStrategy<Object> strategy = strategy();
    Object expected = new Object();
    when(address.convert().apply(strategy, model)).thenReturn(expected);
    assertSame(expected, FixAssembler.withStrategy(strategy).assemble(model));
    address.convert().apply(verify(strategy), model);
    verifyNoMoreInteractions(strategy);
  }

  private static <T extends ArincModel> void assertLookupDispatch(DispatchCase<T> address) {
    ArincFixDatabase fix = mock(ArincFixDatabase.class);
    ArincTerminalAreaDatabase terminal = mock(ArincTerminalAreaDatabase.class);
    T model = mock(address.modelClass());
    when(address.lookup().apply(fix, terminal)).thenReturn(Optional.of(model));
    FixDereferencer<ArincModel> dereferencer = new FixDereferencer<>(value -> value, terminal, fix);
    assertSame(model, dereferencer.dereference(IDENTIFIER, AIRPORT, REGION, address.section(), address.subsection()).orElseThrow());
  }

  private static <T extends ArincModel> T model(Class<T> type, SectionCode section, String subsection) {
    T model = mock(type);
    when(model.sectionCode()).thenReturn(section);
    when(model.subSectionCode()).thenReturn(Optional.ofNullable(subsection));
    return model;
  }

  private static String unknownAddress(SectionCode section, String subsection) {
    return "Unknown referenced section/subsection for lookup of location: " + section.name() + (subsection == null ? "" : subsection);
  }

  @SuppressWarnings("unchecked")
  private static FixAssemblyStrategy<Object> strategy() {
    return mock(FixAssemblyStrategy.class);
  }

  @SuppressWarnings("unchecked")
  private static FixAssembler<Object> assembler() {
    return mock(FixAssembler.class);
  }

  private record Address(SectionCode section, String subsection) {
  }

  private record DispatchCase<T extends ArincModel>(SectionCode section, String subsection, Class<T> modelClass,
      BiFunction<ArincFixDatabase, ArincTerminalAreaDatabase, Optional<T>> lookup,
      BiFunction<FixAssemblyStrategy<Object>, T, Object> convert) {
  }
}
