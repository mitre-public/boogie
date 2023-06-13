package org.mitre.tdp.boogie.arinc.v18;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.model.ArincAirportPrimaryExtension;

import nl.jqno.equalsverifier.EqualsVerifier;

public class TestAirportContModel {
  @Test
  void verify() {
    EqualsVerifier.forClass(ArincAirportPrimaryExtension.class).verify();
  }
}
