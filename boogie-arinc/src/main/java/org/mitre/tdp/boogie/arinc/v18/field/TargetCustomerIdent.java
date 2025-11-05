package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

/**
 * Contains information to identify the data user/customer (for
 * example, the customer name(s), file codes). Content defined by the
 * data supplier and/or customer.
 */
public final class TargetCustomerIdent extends TrimmableString {
  @Override
  public int fieldLength() {
    return 16;
  }

  @Override
  public String fieldCode() {
    return "6.2.1j";
  }
}
