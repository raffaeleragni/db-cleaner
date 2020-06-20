package incrementalwork;

import static java.lang.Math.min;
import static java.lang.Math.max;
import static java.lang.String.format;
import static logger.Logger.log;

public class Stretcher {

  private final Worker worker;
  private final long minWaitMillis;
  private final long maxWaitMillis;
  private final double scaleFactor;
  private long waitMillis;

  public Stretcher(Worker worker,
    long minWaitMillis,
    long maxWaitMillis,
    double scaleFactor) {

    this.worker = worker;
    this.minWaitMillis = minWaitMillis;
    this.maxWaitMillis = maxWaitMillis;
    this.scaleFactor = scaleFactor;
    this.waitMillis = minWaitMillis;
  }

  public long run() {
    var worked = worker.work();
    if (worked > 0) {
      log(format("Deleted records: %d", worked));
      waitMillis = minWaitMillis;
      return maxWaitMillis;
    }

    waitMillis *= scaleFactor;

    waitMillis = boundary(waitMillis);

    log(format("Increasing wait to: %d", waitMillis));
    return waitMillis;
  }

  private long boundary(long value) {
    return max(min(value, maxWaitMillis), minWaitMillis);
  }

}
