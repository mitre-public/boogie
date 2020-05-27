package org.mitre.tdp.boogie.v18;

import org.mitre.tdp.boogie.ArincRecord;
import org.mitre.tdp.boogie.Transition;

public interface ArincTransition extends Transition {

  ArincRecord arincRecord();
}
