package incrementalwork;


import incrementalwork.Stretcher;
import incrementalwork.Worker;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


public class StretcherTest {

  Worker worker;
  Stretcher stretcher;

  @BeforeEach
  public void setup() {
    worker = mock(Worker.class);
    stretcher = new Stretcher(worker, 1_000, 60_000, 2.0);
  }

  @Test
  public void testStretchToMaxWhenSomethingIsProcessed() {
    given(worker.work()).willReturn(1);

    var waitMillis = stretcher.run();

    assertThat(waitMillis, is(60_000L));
  }

  @Test
  public void testStretchBeyondMax() {
    given(worker.work()).willReturn(0);

    var waitMillis = stretcher.run();
    assertThat(waitMillis, is(2_000L));

    waitMillis = stretcher.run();
    assertThat(waitMillis, is(4_000L));

    waitMillis = stretcher.run();
    assertThat(waitMillis, is(8_000L));

    waitMillis = stretcher.run();
    assertThat(waitMillis, is(16_000L));

    waitMillis = stretcher.run();
    assertThat(waitMillis, is(32_000L));

    waitMillis = stretcher.run();
    assertThat(waitMillis, is(60_000L));
  }
}
