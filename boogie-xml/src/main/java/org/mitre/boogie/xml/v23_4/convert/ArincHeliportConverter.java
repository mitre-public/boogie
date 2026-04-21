package org.mitre.boogie.xml.v23_4.convert;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.ArincHeliport;
import org.mitre.boogie.xml.model.fields.ArincPortInfo;
import org.mitre.boogie.xml.v23_4.generated.Heliport;

public final class ArincHeliportConverter implements Function<Heliport, Optional<ArincHeliport>> {
  public static final ArincHeliportConverter INSTANCE = new ArincHeliportConverter();

  private static final ArincHeliportValidator VALIDATOR = new ArincHeliportValidator();
  private static final ArincPortConverter PORT_CONVERTER = ArincPortConverter.INSTANCE;

  private ArincHeliportConverter() {
  }

  @Override
  public Optional<ArincHeliport> apply(Heliport heliport) {
    return Optional.ofNullable(heliport)
        .filter(VALIDATOR)
        .map(this::convert);
  }

  private ArincHeliport convert(Heliport heliport) {
    ArincPortInfo portInfo = PORT_CONVERTER.apply(heliport);
    String heliportType = Optional.ofNullable(heliport.getHeliportType())
        .map(Enum::name)
        .orElse(null);

    return ArincHeliport.builder()
        .portInfo(portInfo)
        .heliportType(heliportType)
        .build();
  }
}
