import incrementalwork.Cleaner;
import incrementalwork.Stretcher;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import java.util.Properties;

public final class CleanerFromFile {

  private CleanerFromFile() {
  }

  public static Stretcher build(String fileName) {
    var properties = new Properties();
    try (var reader = new FileReader(fileName)) {
      properties.load(reader);

      var provider = CleaningProviderFromFile.build(fileName);

      var batchNumber = parseInt(properties.getProperty("batchNumber"));
      var minWaitMillis = parseLong(properties.getProperty("minWaitMillis"));
      var maxWaitMillis = parseLong(properties.getProperty("maxWaitMillis"));
      var scaleFactor = parseDouble(properties.getProperty("scaleFactor"));

      var worker = new Cleaner<>(provider, batchNumber);
      var task = new Stretcher(worker, minWaitMillis, maxWaitMillis, scaleFactor);

      return task;
    } catch (IOException ex) {
      throw new RuntimeException(ex.getMessage(), ex);
    }
  }

}
