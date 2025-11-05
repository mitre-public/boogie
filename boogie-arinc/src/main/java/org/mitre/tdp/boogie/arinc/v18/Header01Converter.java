package org.mitre.tdp.boogie.arinc.v18;

import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincHeaderOne;
import org.mitre.tdp.boogie.arinc.utils.ArincDateTimeParser;
import org.mitre.tdp.boogie.arinc.v18.field.ProductionTestFlag;

public final class Header01Converter implements Function<ArincRecord, Optional<ArincHeaderOne>> {
  private final Predicate<ArincRecord> isInvalidRecord = new Header01Validator().negate();

  @Override
  public Optional<ArincHeaderOne> apply(ArincRecord arincRecord) {
    requireNonNull(arincRecord, "Cannot convert null arinc records.");

    if (isInvalidRecord.test(arincRecord)) {
      return Optional.empty();
    }

    Optional<String> fileName = arincRecord.optionalField("fileName");
    Optional<Integer> version = arincRecord.optionalField("versionNumber");
    Optional<ProductionTestFlag>  productionTestFlag = arincRecord.optionalField("productionTestFlag");
    Optional<Integer> recordLength = arincRecord.optionalField("recordLength");
    Optional<Integer> recordCount = arincRecord.optionalField("recordCount");
    Optional<String> cycle = arincRecord.optionalField("cycle");

    Optional<Instant> creation = Optional.of(arincRecord)
        .filter(r -> arincRecord.containsParsedField("creationDate"))
        .filter(r -> arincRecord.containsParsedField("creationTime"))
        .map(r -> {
          String date = arincRecord.requiredField("creationDate");
          String time = arincRecord.requiredField("creationTime");
          return ArincDateTimeParser.INSTANCE.apply(date, time);
        });

    Optional<String> dataSupplierIdent = arincRecord.optionalField("dataSupplierIdent");
    Optional<String> targetCustomerIdent = arincRecord.optionalField("targetCustomerIdent");
    Optional<String> databasePartNumber = arincRecord.optionalField("databasePartNumber");
    Optional<String> fileCrc = arincRecord.optionalField("fileCrc");

    ArincHeaderOne header = ArincHeaderOne.builder()
        .headerNumber(arincRecord.requiredField("headerNumber"))
        .fileName(fileName.orElse(null))
        .version(version.orElse(null))
        .productionTestFlag(productionTestFlag.map(Enum::name).orElse(null))
        .recordLength(recordLength.orElse(null))
        .recordCount(recordCount.orElse(null))
        .cycle(cycle.orElse(null))
        .creationTime(creation.orElse(null))
        .dataSupplierIdentifier(dataSupplierIdent.orElse(null))
        .targetCustomerIdent(targetCustomerIdent.orElse(null))
        .databasePartNumber(databasePartNumber.orElse(null))
        .fileCrc(fileCrc.orElse(null))
        .build();

    return Optional.of(header);
  }
}
