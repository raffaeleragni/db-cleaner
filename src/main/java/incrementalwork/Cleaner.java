package incrementalwork;


public class Cleaner<T> implements Worker {

  private final DataProviderForCleaning<T> provider;
  private final int maxBatch;

  public Cleaner(DataProviderForCleaning<T> provider, int maxBatch) {
    this.provider = provider;
    this.maxBatch = maxBatch;
  }

  @Override
  public int work() {
    int cleanedCounter = 0;
    while (provider.needsCleaning() && cleanedCounter < maxBatch) {
      cleanItem(getNextItem());
      cleanedCounter++;
    }

    return cleanedCounter;
  }

  private T getNextItem() {
    return provider.nextItemToClean();
  }

  private void cleanItem(T item) {
    provider.cleanItem(item);
  }

}
