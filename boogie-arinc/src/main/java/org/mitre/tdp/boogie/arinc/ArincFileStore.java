package org.mitre.tdp.boogie.arinc;

import java.io.File;
import java.util.function.Function;

/**
 * A static collection of known and pre-configured lookup utilities for ARINC-424 files from various filesystems.
 * <br>
 * This class is mainly provided for convenience. The REST API wrapper can be configured to point at different locations on a
 * visible file system through use of environment variables at launch time - and as such this list should stay relatively static
 * as generally there is no need for different deployments of the boogie-arinc API to add values/paths here.
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
  MITRE_LIDO(new PatternBasedFileLocator("/data/cda/raw/lido/nifi/20{yy}/LidoMIT_{yy}{cc}.dat")),
  /**
   * Historical CIFP data (back to 2018) as copied from the HADOOP HDFS archive maintained by the MITRE PBN team.
   * <br>
   * The transferred version which exists at the below location on NFS can be accessed without any of those pesky hadoop deps.
   */
  MITRE_CIFP(new PatternBasedFileLocator("/data/cda/raw/cifp/copy/{yy}{cc}/FAACIFP18"));

  private final Function<String, File> fileLocator;

  ArincFileStore(Function<String, File> fileLocator) {
    this.fileLocator = fileLocator;
  }

  @Override
  public File apply(String cycle) {
    return fileLocator.apply(cycle);
  }
}
