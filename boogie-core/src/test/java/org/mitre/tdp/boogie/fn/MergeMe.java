package org.mitre.tdp.boogie.fn;

import java.time.Instant;


final class MergeMe {

  public String name;
  public long val;

  public MergeMe(long val) {
    this("", val);
  }

  public MergeMe(String n, long v) {
    this.name = n;
    this.val = v;
  }

  public Instant getTime() {
    return Instant.ofEpochMilli(val);
  }
}
