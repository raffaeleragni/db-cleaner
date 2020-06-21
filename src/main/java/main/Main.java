package main;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static logger.Logger.log;

public final class Main {

  private Main() {
  }

  public static void main(String[] args) throws Exception {
    if (args.length < 1)
      throw new IllegalArgumentException("ERROR, Need argument: configuration file name");

    var task = CleanerFromFile.build(args[0]);

    log("Starting thread loop.");
    while (true) { //NOSONAR
      var nextSleep = task.run();
      log(format("Sleeping for %dms", nextSleep));
      MILLISECONDS.sleep(nextSleep);
    }
  }
}
