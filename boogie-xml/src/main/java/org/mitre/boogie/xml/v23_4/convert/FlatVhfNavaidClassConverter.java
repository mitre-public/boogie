package org.mitre.boogie.xml.v23_4.convert;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.fields.NavaidWeatherInfo;
import org.mitre.boogie.xml.model.fields.VhfNavaidCoverage;
import org.mitre.boogie.xml.v23_4.generated.VhfNavaidClass;

final class FlatVhfNavaidClassConverter implements Function<VhfNavaidClass, Optional<FlatVhfNavaidClass>> {
  static final FlatVhfNavaidClassConverter INSTANCE = new FlatVhfNavaidClassConverter();

  private FlatVhfNavaidClassConverter() {
  }

  @Override
  public Optional<FlatVhfNavaidClass> apply(VhfNavaidClass nc) {
    return Optional.ofNullable(nc)
        .map(n -> new FlatVhfNavaidClass(
            Optional.ofNullable(n.getVhfNavaidCoverage()).map(Enum::name).filter(VhfNavaidCoverage.VALID::contains).orElse(null),
            Optional.ofNullable(n.getVhfNavaidWeatherInfo()).map(Enum::name).filter(NavaidWeatherInfo.VALID::contains).orElse(null),
            n.isIsNotCollocated(),
            n.isIsBiased(),
            n.isIsNoVoice()));
  }
}
