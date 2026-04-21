package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum CommunicationType {
  ACC,
  ACP,
  AIR,
  APP,
  ARR,
  ASO,
  ATI,
  AWI,
  AWO,
  AWS,
  CBA,
  CCA,
  CLD,
  CPT,
  CTA,
  CTF,
  CTL,
  DEP,
  DIR,
  EFS,
  EMR,
  FSS,
  GCO,
  GND,
  GTE,
  HEL,
  INF,
  MBZ,
  MIL,
  MUL,
  OPS,
  PAL,
  RDO,
  RDR,
  RFS,
  RMP,
  RSA,
  TCA,
  TMA,
  TML,
  TRS,
  TWE,
  TWR,
  UAC,
  UNI;
  public static final Set<String> VALID = Arrays.stream(CommunicationType.values()).map(CommunicationType::name).collect(Collectors.toUnmodifiableSet());
}
