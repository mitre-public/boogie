package org.mitre.tdp.boogie.fn;

import java.time.Instant;


final class MergeMe implements Mergeable<MergeMe> {

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

  @Override
  public boolean mergeable(MergeMe obj) {
    return name.equals(obj.name) && Math.abs(val - obj.val) <= 1000L;
  }

  @Override
  public MergeMe mergeLeft(MergeMe obj) {
    return this;
  }
}
