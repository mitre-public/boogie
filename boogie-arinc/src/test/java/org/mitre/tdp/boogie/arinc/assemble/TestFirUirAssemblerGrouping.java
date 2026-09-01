package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.model.ArincFirUirLeg;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.FirUirIndicator;

class TestFirUirAssemblerGrouping {

  private static final FirUirAssembler<AssembledFirUir> ASSEMBLER = FirUirAssembler.usingStrategy(
      new FirUirAssemblyStrategy<AssembledFirUir, Integer>() {
        @Override
        public Stream<AssembledFirUir> convertFirUir(ArincFirUirLeg representative, List<Integer> allLegs) {
          return Stream.of(new AssembledFirUir(representative, allLegs));
        }

        @Override
        public Integer convertFirUirLeg(ArincFirUirLeg leg) {
          return leg.sequenceNumber();
        }
      }
  );

  @Test
  void groupsByTheFourFirUirIdentityFieldsAndSortsSequences() {
    ArincFirUirLeg first = leg(CustomerAreaCode.USA, "KZAB", null, FirUirIndicator.F, 20);
    ArincFirUirLeg second = leg(CustomerAreaCode.USA, "KZAB", "", FirUirIndicator.F, 10);

    List<AssembledFirUir> assembled = ASSEMBLER.assemble(List.of(
        first,
        leg(CustomerAreaCode.CAN, "KZAB", null, FirUirIndicator.F, 30),
        leg(CustomerAreaCode.USA, "KZAK", null, FirUirIndicator.F, 30),
        leg(CustomerAreaCode.USA, "KZAB", "ZOZX", FirUirIndicator.F, 30),
        leg(CustomerAreaCode.USA, "KZAB", null, FirUirIndicator.U, 30),
        second
    )).toList();

    AssembledFirUir base = assembled.stream()
        .filter(candidate -> candidate.representative() == first)
        .findFirst()
        .orElseThrow();

    assertAll(
        () -> assertEquals(5, assembled.size()),
        () -> assertSame(first, base.representative()),
        () -> assertEquals(List.of(10, 20), base.sequenceNumbers())
    );
  }

  private static ArincFirUirLeg leg(
      CustomerAreaCode customerAreaCode,
      String identifier,
      String address,
      FirUirIndicator indicator,
      int sequenceNumber
  ) {
    return new ArincFirUirLeg.Builder()
        .customerAreaCode(customerAreaCode)
        .firUirIdentifier(identifier)
        .firUirAddress(address)
        .firUirIndicator(indicator)
        .sequenceNumber(sequenceNumber)
        .build();
  }

  private record AssembledFirUir(ArincFirUirLeg representative, List<Integer> sequenceNumbers) {}
}
