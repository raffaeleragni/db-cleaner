import java.util.concurrent.Executors;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static logger.Logger.log;

public class Main {
  public static void main(String[] args) throws Exception {
    if (args.length < 1)
      throw new IllegalArgumentException("ERROR, Need argument: configuration file name");

    var task = CleanerFromFile.build(args[0]);

    var executor = Executors.newFixedThreadPool(1);
    log("Starting thread loop.");
    while (true)
      MILLISECONDS.sleep(executor.submit(task::run).get());
  }
}
