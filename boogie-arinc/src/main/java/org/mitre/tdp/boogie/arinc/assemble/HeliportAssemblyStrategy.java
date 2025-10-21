package org.mitre.tdp.boogie.arinc.assemble;

import org.mitre.tdp.boogie.Helipad;
import org.mitre.tdp.boogie.Heliport;

public interface HeliportAssemblyStrategy<HPT, HLPD> {
  //todo
  static HeliportAssemblyStrategy<Heliport, Helipad> standard() {
    return new Standard();
  }

  final class Standard implements HeliportAssemblyStrategy<Heliport, Helipad> {
    //todo
  }
}
