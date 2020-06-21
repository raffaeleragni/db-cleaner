package sqlprovider;

import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class IDCleaningProviderExceptionsTest {
  @Test
  public void testExceptions() throws SQLException {
    var connection = mock(Connection.class);
    var provider = new StringIDCleaningProvider(connection, "", "");

    given(connection.prepareStatement(any()))
      .willThrow(SQLException.class);

    assertThrows(RuntimeException.class, () -> {
      provider.cleanItem("");
    });

    assertThrows(RuntimeException.class, () -> {
      provider.nextItemToClean();
    });
  }
}
