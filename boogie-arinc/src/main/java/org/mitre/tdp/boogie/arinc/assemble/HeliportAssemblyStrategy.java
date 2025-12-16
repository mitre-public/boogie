package org.mitre.tdp.boogie.arinc.assemble;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.AiracCycle;
import org.mitre.tdp.boogie.Declinations;
import org.mitre.tdp.boogie.Helipad;
import org.mitre.tdp.boogie.Heliport;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.arinc.model.ArincHelipad;
import org.mitre.tdp.boogie.arinc.model.ArincHeliport;

/**
 * This class defines how to convert an arinc heliport into an {@link Heliport.Standard} and {@link Helipad.Standard} from Arinc 424 record.
 * @param <H> the heliport class.
 * @param <P> the helipad class.
 */
public interface HeliportAssemblyStrategy<H, P> {
  /**
   * This strategy will build the standard records with simple defs in boogie.
   * @return the strategy.
   */
  static HeliportAssemblyStrategy<Heliport, Helipad> standard() {
    return new Standard();
  }

  /**
   * This is to convert the 424 records to the user defined records for later use.
   * @param port the 424 heliport.
   * @param convertedHelipads the already converted helipads.
   * @return the user defined heliport.
   */
  H convertHeliport(ArincHeliport port, List<P> convertedHelipads);

  /**
   * This is to convert the arinc 424 helipads defined by the user.
   * @param pad the 424 helipad
   * @return the user defined helipad.
   */
  P convertHelipad(ArincHelipad pad);

  /**
   * This method is used to create a fake helipad at legacy 424 heliport locations (which are their own pad records).
   * @param port the heliport to make into a pad
   * @return the helipad for the heliport (aka the landing surface at the heliport)
   */
  Optional<P> convertToHelipad(ArincHeliport port);

  final class Standard implements HeliportAssemblyStrategy<Heliport, Helipad> {

    private Standard() {
    }

    @Override
    public Heliport convertHeliport(ArincHeliport port, List<Helipad> convertedHelipads) {
      LatLong place = LatLong.of(port.latitude(), port.longitude());
      List<Helipad> padsOrFromPort = Optional.ofNullable(convertedHelipads).filter(l -> !l.isEmpty())
          .or(() -> convertToHelipad(port).map(List::of))
          .orElse(null);
      return Heliport.builder()
          .heliportIdentifier(port.heliportIdentifier())
          .magneticVariation(magneticVariation(port))
          .helipads(padsOrFromPort)
          .latLong(place)
          .build();
    }

    @Override
    public Helipad convertHelipad(ArincHelipad pad) {
      return Helipad.builder()
          .origin(LatLong.of(pad.latitude(), pad.longitude()))
          .padIdentifier(pad.helipadIdentifier())
          .build();
    }

    @Override
    public Optional<Helipad> convertToHelipad(ArincHeliport port) {
      LatLong place = LatLong.of(port.latitude(), port.longitude());
      return port.padIdentifier().map(i -> Helipad.builder().origin(place).padIdentifier(i).build());
    }

    private MagneticVariation magneticVariation(ArincHeliport port) {

      Supplier<Optional<Double>> goodCalculation = () -> port.cycleDate()
          .map(AiracCycle::startDate)
          .map(i -> Declinations.declination(port.latitude(), port.longitude(), i));

      Supplier<Optional<Double>> badCalcualtion = () -> Optional.of(Declinations.approx(port.latitude(), port.longitude()));

      return port.magneticVariation()
          .or(goodCalculation)
          .or(badCalcualtion)
          .map(MagneticVariation::ofDegrees)
          .orElse(MagneticVariation.ZERO);
    }
  }
}

