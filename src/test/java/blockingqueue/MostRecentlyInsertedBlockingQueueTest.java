package blockingqueue;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;

import static org.hamcrest.CoreMatchers.is;


@RunWith(ConcurrentTestRunner.class)
public class MostRecentlyInsertedBlockingQueueTest {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final int CAPACITY = 5;
    private final MostRecentlyInsertedBlockingQueue<Integer> TEST_MOST_RECENTLY_INSERTED_BLOCKING_QUEUE = new MostRecentlyInsertedBlockingQueue<Integer>(CAPACITY);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public Stopwatch stopwatch = new Stopwatch() {
        private void logInfo(Description description, long nanos) {
            log.info(String.format("Test %s spent %d microseconds",
                    description.getMethodName(), TimeUnit.NANOSECONDS.toMicros(nanos)));
        }

        @Override
        protected void finished(long nanos, Description description) {
            logInfo(description, nanos);
        }
    };

    @Before
    public void offer() throws Exception{
        TEST_MOST_RECENTLY_INSERTED_BLOCKING_QUEUE.offer(1);
        TEST_MOST_RECENTLY_INSERTED_BLOCKING_QUEUE.offer(2);
        TEST_MOST_RECENTLY_INSERTED_BLOCKING_QUEUE.offer(3);
        TEST_MOST_RECENTLY_INSERTED_BLOCKING_QUEUE.offer(4);
        TEST_MOST_RECENTLY_INSERTED_BLOCKING_QUEUE.offer(5);
    }

    @Test
    public void pollTest() throws Exception{
        Thread.sleep(3000);
        TEST_MOST_RECENTLY_INSERTED_BLOCKING_QUEUE.poll(3,TimeUnit.SECONDS);
    }

}