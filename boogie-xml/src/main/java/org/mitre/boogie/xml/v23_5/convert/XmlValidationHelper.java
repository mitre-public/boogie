package org.mitre.boogie.xml.v23_5.convert;

import static java.util.Objects.nonNull;

import java.util.function.BiConsumer;

import org.mitre.boogie.xml.v23_5.generated.A424Point;
import org.mitre.boogie.xml.v23_5.generated.A424Record;

final class XmlValidationHelper {

  private XmlValidationHelper() {
  }

  static <T> boolean nonNullField(T record, String fieldName, Object fieldValue, BiConsumer<T, String> missingFieldConsumer) {
    boolean present = nonNull(fieldValue);
    if (!present) {
      missingFieldConsumer.accept(record, fieldName);
    }
    return present;
  }

  static <T extends A424Record> boolean validateRecordFields(T record, BiConsumer<T, String> consumer) {
    return nonNullField(record, "recordType", record.getRecordType(), consumer)
        && nonNullField(record, "cycleDate", record.getCycleDate(), consumer);
  }

  static <T extends A424Point> boolean validatePointFields(T point, BiConsumer<T, String> consumer) {
    return validateRecordFields(point, consumer)
        && nonNullField(point, "identifier", point.getIdentifier(), consumer)
        && nonNullField(point, "icaoCode", point.getIcaoCode(), consumer)
        && nonNullField(point, "location", point.getLocation(), consumer)
        && nonNullField(point, "location.latitude", point.getLocation().getLatitude(), consumer)
        && nonNullField(point, "location.longitude", point.getLocation().getLongitude(), consumer);
  }
}
