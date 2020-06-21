import main.Main;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ApplicationITTest {

  static Connection connection;
  static TestDatabaseWrapper dbWrapper;

  @BeforeAll
  public static void setup() throws IOException, SQLException {
    connection = DriverManager.getConnection("jdbc:h2:mem:test2", "sa", "");
    dbWrapper = new TestDatabaseWrapper(connection);
    dbWrapper.createTable();
    dbWrapper.addItems("1", "3", "4");
  }

  @AfterEach
  public void teardown() throws SQLException {
    connection.close();
  }

  @Test
  public void testNoParams() {
    var ex = assertThrows(RuntimeException.class, () -> {
      try {
        Main.main(new String[]{});
      } catch (Exception ex2) {
        throw new RuntimeException(ex2.getMessage(), ex2);
      }
    });

    assertThat(ex.getMessage(), containsString("ERROR, Need argument: configuration file name"));
  }

  @Test
  public void testMain() throws Exception {
    var t = new Thread(() -> {
      try {
        Main.main(new String[]{"test_configuration.properties"});
      } catch (Exception ex) {
        throw new RuntimeException(ex.getMessage(), ex);
      }
    });

    t.start();
    MILLISECONDS.sleep(100);
    t.interrupt();

    var count = dbWrapper.countItems();

    assertThat(count, is(0));
  }
}
