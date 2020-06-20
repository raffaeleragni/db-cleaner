package incrementalwork;


public interface DataProviderForCleaning<T> {
  boolean needsCleaning();
  T nextItemToClean();
  void cleanItem(T item);
}
