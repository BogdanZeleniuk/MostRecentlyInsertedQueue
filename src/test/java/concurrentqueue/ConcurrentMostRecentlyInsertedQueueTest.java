package concurrentqueue;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simplequeue.MostRecentlyInsertedQueue;

import java.util.concurrent.TimeUnit;

/**
 * Created by Admin on 15.09.2016.
 */
@RunWith(JUnit4.class)
public class ConcurrentMostRecentlyInsertedQueueTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final int CAPACITY = 5;
    private ConcurrentMostRecentlyInsertedQueue<Integer> TEST_CONCURRENT_MOST_RECENTLY_INSERTED_QUEUE = new ConcurrentMostRecentlyInsertedQueue<Integer>(CAPACITY);
    private static final int ELEMENT_ONE = 1;
    private static final int ELEMENT_TWO = 2;
    private static final int ELEMENT_TRE = 3;
    private static final int ELEMENT_FOUR = 4;
    private static final int ELEMENT_FIVE = 5;
    private static final int ELEMENT_SIX = 6;

    @Before
    public void setUp() throws Exception {
        {
            TEST_CONCURRENT_MOST_RECENTLY_INSERTED_QUEUE.offer(ELEMENT_ONE);
            TEST_CONCURRENT_MOST_RECENTLY_INSERTED_QUEUE.offer(ELEMENT_TWO);
            TEST_CONCURRENT_MOST_RECENTLY_INSERTED_QUEUE.offer(ELEMENT_TRE);
            TEST_CONCURRENT_MOST_RECENTLY_INSERTED_QUEUE.offer(ELEMENT_FOUR);
            TEST_CONCURRENT_MOST_RECENTLY_INSERTED_QUEUE.offer(ELEMENT_FIVE);
        }
    }

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

    @Test
    public void newQueueSizeTest() throws Exception{
        Assert.assertEquals(TEST_CONCURRENT_MOST_RECENTLY_INSERTED_QUEUE.size(),5);
    }

    @Test
    public void offerTest() throws Exception{
        Assert.assertEquals("[1, 2, 3, 4, 5]",TEST_CONCURRENT_MOST_RECENTLY_INSERTED_QUEUE.toString());
    }

    @Test
    public void peekTest() throws Exception{
        int headElement = TEST_CONCURRENT_MOST_RECENTLY_INSERTED_QUEUE.peek();
        Assert.assertEquals(headElement,ELEMENT_ONE);
    }

    @Test
    public void pollTest() throws Exception{
        TEST_CONCURRENT_MOST_RECENTLY_INSERTED_QUEUE.poll();
        Assert.assertEquals(TEST_CONCURRENT_MOST_RECENTLY_INSERTED_QUEUE.size(),4);
    }

    @Test
    public void offerSixthElementTest() throws Exception{
        TEST_CONCURRENT_MOST_RECENTLY_INSERTED_QUEUE.offer(ELEMENT_SIX);
        Assert.assertEquals("[2, 3, 4, 5, 6]",TEST_CONCURRENT_MOST_RECENTLY_INSERTED_QUEUE.toString());
    }

    @Test
    public void clearTest() throws Exception{
        TEST_CONCURRENT_MOST_RECENTLY_INSERTED_QUEUE.clear();
        Assert.assertEquals(TEST_CONCURRENT_MOST_RECENTLY_INSERTED_QUEUE.size(),0);
    }

    @Test
    public void offerOfEmptyElement(){
        thrown.expect(NullPointerException.class);
        TEST_CONCURRENT_MOST_RECENTLY_INSERTED_QUEUE.offer(null);
    }

    @Test
    public void iteratorHasNextTest(){
        int count = 0;
        for (int i=0; i<TEST_CONCURRENT_MOST_RECENTLY_INSERTED_QUEUE.size(); i++){
            if (TEST_CONCURRENT_MOST_RECENTLY_INSERTED_QUEUE.iterator().hasNext()){
                count++;
            }
        }
        Assert.assertEquals(count,5);
    }

    @Test
    public void iteratorNextTest(){
        MostRecentlyInsertedQueue<Integer> queue = new MostRecentlyInsertedQueue<Integer>(CAPACITY);
        for (int i=0; i<TEST_CONCURRENT_MOST_RECENTLY_INSERTED_QUEUE.size(); i++){
            int addElement = TEST_CONCURRENT_MOST_RECENTLY_INSERTED_QUEUE.iterator().next();
            queue.offer(addElement);
        }
        Assert.assertEquals("[1, 1, 1, 1, 1]", queue.toString());
    }
}