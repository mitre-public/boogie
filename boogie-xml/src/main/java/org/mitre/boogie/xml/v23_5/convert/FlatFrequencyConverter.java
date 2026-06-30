package org.mitre.boogie.xml.v23_5.convert;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.fields.FreqUnitOfMeasure;
import org.mitre.boogie.xml.v23_5.generated.Frequency;

final class FlatFrequencyConverter implements Function<Frequency, Optional<FlatFrequency>> {
  static final FlatFrequencyConverter INSTANCE = new FlatFrequencyConverter();

  private FlatFrequencyConverter() {
  }

  @Override
  public Optional<FlatFrequency> apply(Frequency freq) {
    return Optional.ofNullable(freq)
        .map(f -> new FlatFrequency(
            Optional.ofNullable(f.getFrequencyValue()).map(BigDecimal::doubleValue).orElse(null),
            Optional.ofNullable(f.getFreqUnitOfMeasure()).map(Enum::name).filter(FreqUnitOfMeasure.VALID::contains).orElse(null)));
  }
}
