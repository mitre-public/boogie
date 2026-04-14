package org.mitre.boogie.xml.assemble;

import java.util.List;

import org.mitre.tdp.boogie.Helipad;
import org.mitre.tdp.boogie.Heliport;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.boogie.xml.model.ArincHelipad;
import org.mitre.boogie.xml.model.ArincHeliport;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincPortInfo;
import org.mitre.boogie.xml.util.MagneticVariationResolver;

/**
 * Strategy class for generating user-defined records from XML heliport information. Used with {@link HeliportAssembler}.
 *
 * <p>Because the XML model is hierarchical (helipads are nested within the heliport's port info), the assembler does not
 * require an external terminal area database — all associated data is contained within the {@link ArincHeliport} itself.
 */
public interface HeliportAssemblyStrategy<H, P> {

  static HeliportAssemblyStrategy<Heliport, Helipad> standard() {
    return new Standard();
  }

  /**
   * Converts the incoming XML heliport record and a collection of helipads into an output user-defined type.
   *
   * @param heliport         the input XML heliport definition record
   * @param convertedHelipads the collection of helipads associated with the heliport converted with {@code convertHelipad}
   */
  H convertHeliport(ArincHeliport heliport, List<P> convertedHelipads);

  /**
   * Convert a helipad record into a single user-defined helipad type.
   */
  P convertHelipad(ArincHelipad pad);

  final class Standard implements HeliportAssemblyStrategy<Heliport, Helipad> {

    private Standard() {
    }

    @Override
    public Heliport convertHeliport(ArincHeliport heliport, List<Helipad> convertedHelipads) {
      ArincPortInfo portInfo = heliport.portInfo();
      ArincPointInfo point = portInfo.pointInfo();
      MagneticVariation magvar = MagneticVariationResolver.INSTANCE.apply(portInfo.pointInfo(), portInfo.recordInfo().cycleDate());

      return Heliport.builder()
          .heliportIdentifier(point.identifier())
          .latLong(point.latLong())
          .magneticVariation(magvar)
          .helipads(convertedHelipads)
          .build();
    }

    @Override
    public Helipad convertHelipad(ArincHelipad pad) {
      return Helipad.builder()
          .padIdentifier(pad.pointInfo().identifier())
          .origin(pad.pointInfo().latLong())
          .build();
    }
  }
}
