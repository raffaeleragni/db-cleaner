
import java.sql.Connection;
import java.sql.SQLException;

public class TestDatabaseWrapper {

  private final Connection connection;

  public TestDatabaseWrapper(Connection connection) {
    this.connection = connection;
  }

  public void createTable() throws SQLException {
    runStatement("create table if not exists clean_test (id varchar(20) primary key, stamp timestamp)");
  }

  public void addItems(String... ids) throws SQLException {
    for (String id: ids)
      runStatement("insert into clean_test (id, stamp) values('"+id+"', CURRENT_TIMESTAMP)");
  }

  public int countItems() throws SQLException {
    try (var statement = connection.prepareStatement("select count(*) from clean_test")) {
      try (var result = statement.executeQuery()) {
        result.next();
        return result.getInt(1);
      }
    }
  }

  private int runStatement(String sql) throws SQLException {
    try (var statement = connection.prepareStatement(sql)) {
      return statement.executeUpdate();
    }
  }
}
