package sqlprovider;

import incrementalwork.DataProviderForCleaning;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class StringIDCleaningProvider implements DataProviderForCleaning<String> {

  private final Connection connection;
  private final String selectNextId;
  private final String deleteWhereId;
  private Optional<String> lastFetchedItem;

  public StringIDCleaningProvider(
    Connection connection,
    String selectNextId,
    String deleteWhereId) {

    this.connection = connection;
    this.selectNextId = selectNextId;
    this.deleteWhereId = deleteWhereId;
    this.lastFetchedItem = Optional.empty();
  }

  @Override
  public boolean needsCleaning() {
    return fetchAndCacheNextId().isPresent();
  }

  @Override
  public String nextItemToClean() {
    return fetchAndCacheNextId().orElseThrow();
  }

  @Override
  public void cleanItem(String item) {
    try {
      deleteWhereId(item);
    } finally {
      lastFetchedItem = Optional.empty();
    }
  }

  private Optional<String> fetchAndCacheNextId() {
    return lastFetchedItem.or(this::forceSelectNextId);
  }

  private void deleteWhereId(String id) {
    try (var statement = connection.prepareStatement(deleteWhereId)) {
      statement.setString(1, id);
      statement.executeUpdate();
    } catch (SQLException ex) {
      throw new RuntimeException(ex.getMessage(), ex); //NOSONAR
    }
  }

  private Optional<String> forceSelectNextId() {
    try (var statement = connection.prepareStatement(selectNextId)) {
      return resultSetProcessor(statement);
    } catch (SQLException ex) {
      throw new RuntimeException(ex.getMessage(), ex); //NOSONAR
    }
  }

  private Optional<String> resultSetProcessor(final PreparedStatement statement) throws SQLException {
    try (var result = statement.executeQuery()) {
      return fetchSongleString(result);
    }
  }

  Optional<String> fetchSongleString(final ResultSet result) throws SQLException {
    if (!result.next())
      return Optional.empty();

    return Optional.ofNullable(result.getString(1));
  }

}
