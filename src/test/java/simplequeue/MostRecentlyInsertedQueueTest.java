package simplequeue;

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

import java.util.*;
import java.util.concurrent.TimeUnit;

@RunWith(JUnit4.class)
public class MostRecentlyInsertedQueueTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final int CAPACITY = 5;
    private Queue<Integer> TEST_LINKED_QUEUE = new LinkedList<Integer>();
    private MostRecentlyInsertedQueue<Integer> TEST_MOST_RECENTLY_INSERTED_QUEUE = new MostRecentlyInsertedQueue<Integer>(CAPACITY);
    private static final int ELEMENT_ONE = 1;
    private static final int ELEMENT_TWO = 2;
    private static final int ELEMENT_THREE = 3;
    private static final int ELEMENT_FOUR = 4;
    private static final int ELEMENT_FIVE = 5;
    private static final int ELEMENT_SIX = 6;

    @Before
    public void setUp() throws Exception {
        {
            TEST_LINKED_QUEUE.offer(ELEMENT_ONE);
            TEST_LINKED_QUEUE.offer(ELEMENT_TWO);
            TEST_LINKED_QUEUE.offer(ELEMENT_THREE);
            TEST_LINKED_QUEUE.offer(ELEMENT_FOUR);
            TEST_LINKED_QUEUE.offer(ELEMENT_FIVE);
        }
        {
            TEST_MOST_RECENTLY_INSERTED_QUEUE.offer(ELEMENT_ONE);
            TEST_MOST_RECENTLY_INSERTED_QUEUE.offer(ELEMENT_TWO);
            TEST_MOST_RECENTLY_INSERTED_QUEUE.offer(ELEMENT_THREE);
            TEST_MOST_RECENTLY_INSERTED_QUEUE.offer(ELEMENT_FOUR);
            TEST_MOST_RECENTLY_INSERTED_QUEUE.offer(ELEMENT_FIVE);
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
    public void newQueueIsEmptyTest(){
        Assert.assertEquals(TEST_MOST_RECENTLY_INSERTED_QUEUE.isEmpty(),false);
    }

    @Test
    public void offerTest() throws Exception{
        Assert.assertEquals(TEST_LINKED_QUEUE.toString(),TEST_MOST_RECENTLY_INSERTED_QUEUE.toString());
    }

    @Test
    public void newQueueSizeTest() throws Exception{
        Assert.assertEquals(TEST_MOST_RECENTLY_INSERTED_QUEUE.size(),5);
    }

    @Test
    public void peekTest() throws Exception{
        int headElement = TEST_MOST_RECENTLY_INSERTED_QUEUE.peek();
        Assert.assertEquals(headElement,ELEMENT_ONE);
    }

    @Test
    public void pollTest() throws Exception{
        TEST_MOST_RECENTLY_INSERTED_QUEUE.poll();
        Assert.assertEquals(TEST_MOST_RECENTLY_INSERTED_QUEUE.size(),4);
    }

    @Test
    public void offerSixthElementTest() throws Exception{
        TEST_LINKED_QUEUE.poll();
        TEST_LINKED_QUEUE.offer(ELEMENT_SIX);
        TEST_MOST_RECENTLY_INSERTED_QUEUE.offer(ELEMENT_SIX);
        Assert.assertEquals(TEST_LINKED_QUEUE.toString(),TEST_MOST_RECENTLY_INSERTED_QUEUE.toString());
    }

    @Test
    public void clearTest() throws Exception{
        TEST_MOST_RECENTLY_INSERTED_QUEUE.clear();
        Assert.assertEquals(TEST_MOST_RECENTLY_INSERTED_QUEUE.size(),0);
    }

    @Test
    public void peekElementOnEmptyQueue(){
        thrown.expect(NoSuchElementException.class);
        TEST_MOST_RECENTLY_INSERTED_QUEUE.clear();
        TEST_MOST_RECENTLY_INSERTED_QUEUE.peek();
    }

    @Test
    public void pollElementOnEmptyQueue(){
        thrown.expect(NoSuchElementException.class);
        TEST_MOST_RECENTLY_INSERTED_QUEUE.clear();
        TEST_MOST_RECENTLY_INSERTED_QUEUE.poll();
    }

    @Test
    public void iteratorHasNextTest(){
        int count = 0;
        for (int i=0; i<TEST_MOST_RECENTLY_INSERTED_QUEUE.size(); i++){
            if (TEST_MOST_RECENTLY_INSERTED_QUEUE.iterator().hasNext()){
                count++;
            }
        }
        Assert.assertEquals(count,5);
    }

    @Test
    public void iteratorNextTest(){
        MostRecentlyInsertedQueue<Integer> queue = new MostRecentlyInsertedQueue<Integer>(CAPACITY);
        for (int i=0; i<TEST_MOST_RECENTLY_INSERTED_QUEUE.size(); i++){
            int addElement = TEST_MOST_RECENTLY_INSERTED_QUEUE.iterator().next();
            queue.offer(addElement);
        }
        Assert.assertEquals("[2, 2, 2, 2, 2]", queue.toString());
    }
}