package org.mitre.tdp.boogie.dafif.model;

import java.io.Serializable;

public interface DafifModel extends Serializable {
  DafifFileType getFileType();
}
