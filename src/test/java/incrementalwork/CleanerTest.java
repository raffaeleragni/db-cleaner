package incrementalwork;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CleanerTest {

  Cleaner<Integer> cleaner;
  DataProviderForCleaning<Integer> provider;

  @BeforeEach
  public void setup() {
    provider = mock(DataProviderForCleaning.class);
    cleaner = new Cleaner<>(provider, 5);
  }

  @Test
  public void testNothingToClean() {
    given(provider.needsCleaning()).willReturn(false);

    var cleanCount = cleaner.work();

    assertThat(cleanCount, is(0));
  }

  @Test
  public void testSomethingToClean() {
    given(provider.needsCleaning()).willReturn(true, false);

    var cleanCount = cleaner.work();

    assertThat(cleanCount, is(1));
  }

  @Test
  public void testTwoItemsToClean() {
    given(provider.needsCleaning()).willReturn(true, true, false);
    given(provider.nextItemToClean()).willReturn(1, 2);

    var cleanCount = cleaner.work();

    assertThat(cleanCount, is(2));

    verify(provider).cleanItem(1);
    verify(provider).cleanItem(2);
  }

  @Test
  public void testMaxBatchRun() {
    given(provider.needsCleaning()).willReturn(true, true, false);
    given(provider.nextItemToClean()).willReturn(1, 2);

    var cleanCount = cleaner.work();

    assertThat(cleanCount, is(2));

    verify(provider).cleanItem(1);
    verify(provider).cleanItem(2);
  }

  @Test
  public void testMaxBatchRunThenStops() {
    given(provider.needsCleaning())
      .willReturn(true, true, true, true, true, true, false);
    given(provider.nextItemToClean())
      .willReturn(1,    2,    3,    4,    5,    6);

    var cleanCount = cleaner.work();

    assertThat(cleanCount, is(5));

    verify(provider).cleanItem(1);
    verify(provider).cleanItem(2);
    verify(provider).cleanItem(3);
    verify(provider).cleanItem(4);
    verify(provider).cleanItem(5);
    verify(provider, times(0)).cleanItem(6);
  }
}
