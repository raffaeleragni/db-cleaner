package main;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import sqlprovider.StringIDCleaningProvider;

public final class CleaningProviderFromFile {

  private CleaningProviderFromFile() {
  }

  public static StringIDCleaningProvider build(String fileName) throws IOException, SQLException {
    var properties = new Properties();
    try (var reader = new FileReader(fileName)) {
      properties.load(reader);

      Connection connection = DriverManager.getConnection(
        properties.getProperty("url"),
        properties.getProperty("username"),
        properties.getProperty("password"));

      var selectQuery = properties.getProperty("selectNextId");
      var deleteQuery = properties.getProperty("deleteWhereId");

      return new StringIDCleaningProvider(connection, selectQuery, deleteQuery);
    }
  }

}
