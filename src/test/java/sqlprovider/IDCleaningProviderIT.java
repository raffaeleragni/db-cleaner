package sqlprovider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IDCleaningProviderIT {

  StringIDCleaningProvider provider;
  Connection connection;

  @BeforeEach
  public void setup() throws SQLException {
    connection = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");

    runSQL(connection, "create table if not exists tablex (id varchar(20) primary key, stamp timestamp)");
    // Always delete everything before any test
    runSQL(connection, "truncate table tablex");

    provider = new StringIDCleaningProvider(
      connection,
      "select id from tablex order by stamp asc limit 1",
      "delete from tablex where id = ?");
  }

  @Test
  public void testNoNeedForCleaning() {
    assertThat(provider.needsCleaning(), is(false));
  }

  @Test
  public void testOneNeedsCleaning() throws SQLException {
    addItem("uno");

    assertThat(provider.needsCleaning(), is(true));
  }

  @Test
  public void testCleanOneAndExpectClean() throws SQLException {
    addItem("uno");

    provider.cleanItem(provider.nextItemToClean());

    assertThat(provider.needsCleaning(), is(false));
  }

  @Test
  public void testCleanOneAndExpectOne() throws SQLException {
    addItem("uno");
    addItem("due");
    addItem("tre");

    provider.cleanItem(provider.nextItemToClean());

    assertThat(provider.needsCleaning(), is(true));
    // As it is sorted by stamp, the 2nd is remaining to be cleaned
    assertThat(provider.nextItemToClean(), is("due"));
  }

  private void addItem(String id) throws SQLException {
    runSQL(connection, "insert into tablex (id, stamp) values('"+id+"', CURRENT_TIMESTAMP)");
  }

  private int runSQL(Connection connection, String sql) throws SQLException {
    try (var statement = connection.prepareStatement(sql)) {
      return statement.executeUpdate();
    }
  }
}
