package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

/**
 * Contains information to identify the data supplier. Content defined
 * by the data supplier.
 */
public final class DataSupplierIdent extends TrimmableString {
  @Override
  public int fieldLength() {
    return 16;
  }

  @Override
  public String fieldCode() {
    return "6.2.1i";
  }
}
