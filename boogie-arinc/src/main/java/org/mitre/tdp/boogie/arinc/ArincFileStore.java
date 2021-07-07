package org.mitre.tdp.boogie.arinc;

import java.io.File;
import java.util.function.Function;

/**
 * A static collection of known and pre-configured lookup utilities for ARINC-424 files from various filesystems.
 * <br>
 * The MITRE CIFP archive is currently hosted on HDP1 (which requires Hadoop dependencies to talk to) so it isn't included in
 * this pre-canned list.
 */
public enum ArincFileStore implements Function<String, File> {
  /**
   * Historical Jeppesen data (2011-2020) - MITRE received from GE packaged as MS-access database files and then archived as
   * converted ARINC 424 via the JACAL library used by TARGETS. Global.
   * <br>
   * Directory is on the NETAPP/linux cluster.
   */
  MITRE_JEPPESEN(new PatternBasedFileLocator("/data/cda/raw/jepp-424/ARINC-424-18_{yy}{cc}.dat")),
  /**
   * Historical LIDO data (2020-Present) - MITRE receives via subscription in the native 424 format. Global.
   * <br>
   * Directory is on the NETAPP/linux cluster.
   */
  MITRE_LIDO(new PatternBasedFileLocator("/data/cda/raw/lido/nifi/20{yy}/LidoMIT_{yy}{cc}.dat"));

  private final Function<String, File> fileLocator;

  ArincFileStore(Function<String, File> fileLocator) {
    this.fileLocator = fileLocator;
  }

  @Override
  public File apply(String cycle) {
    return fileLocator.apply(cycle);
  }
}
