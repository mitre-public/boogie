package org.mitre.tdp.boogie.conformance.alg;

import java.util.List;
import java.util.stream.IntStream;

import org.jblas.DoubleMatrix;
import org.mitre.caasd.commons.HasTime;
import org.mitre.tdp.boogie.conformance.Scorable;

class ScoreSet {
  private long[] times;
  private DoubleMatrix scores;

  private ScoreSet(long[] times, DoubleMatrix scores) {
    this.times = times;
    this.scores = scores;
  }

  public static <H extends HasTime, L extends Scorable<H>> ScoreSet between(List<L> scorables, List<H> pts) {
    ScoreConsumer<H> consumer = new ScoreConsumer<>(pts.size(), scorables.size());
    IntStream.range(0, pts.size())
        .forEach(r -> IntStream.range(0, scorables.size())
            .forEach(c -> consumer.accept(scorables.get(c), pts.get(r), r, c)));
    return new ScoreSet(consumer.taus, consumer.scores);
  }

  public long[] times() {
    return times;
  }

  public DoubleMatrix scores() {
    return scores;
  }

  private static class ScoreConsumer<H extends HasTime> {

    private long[] taus;
    private DoubleMatrix scores;

    private ScoreConsumer(int r, int c) {
      this.taus = new long[r];
      this.scores = DoubleMatrix.zeros(r, c);
    }

    private void accept(Scorable<H> l, H p, int r, int c) {
      double cross = l.scorer().score(p);
      long tau = p.time().toEpochMilli();

      taus[r] = tau;
      scores.put(r, c, cross);
    }
  }
}
