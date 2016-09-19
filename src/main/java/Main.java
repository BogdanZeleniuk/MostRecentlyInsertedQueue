import blockingqueue.MostRecentlyInsertedBlockingQueue;
import concurrentqueue.ConcurrentMostRecentlyInsertedQueue;
import simplequeue.MostRecentlyInsertedQueue;

import static java.util.concurrent.TimeUnit.SECONDS;


public class Main {
    public static void main(String[] args) {
        MostRecentlyInsertedQueue<Integer> queue = new MostRecentlyInsertedQueue<Integer>(3);
        System.out.println(queue.toString());
            queue.offer(1);
        System.out.println(queue.toString());
            queue.offer(2);
        System.out.println(queue.toString());
            queue.offer(3);
        System.out.println(queue.toString());
            queue.offer(4);
        System.out.println(queue.toString());
            queue.offer(5);
        System.out.println(queue.toString());
        int firstPeekCount = queue.peek();
        System.out.println(queue.toString() + " ----------> count = "+ firstPeekCount);
        int secondPeekCount = queue.peek();
        System.out.println(queue.toString() + " ----------> count = "+ secondPeekCount);
        queue.clear();
        System.out.println(queue.toString());

        System.out.println("--------------------------------------------------------------------");

        ConcurrentMostRecentlyInsertedQueue<Integer> concurrentQueue = new ConcurrentMostRecentlyInsertedQueue<Integer>(3);
        System.out.println(concurrentQueue.toString());
        concurrentQueue.offer(1);
        System.out.println(concurrentQueue.toString());
        concurrentQueue.offer(2);
        System.out.println(concurrentQueue.toString());
        concurrentQueue.offer(3);
        System.out.println(concurrentQueue.toString());
        concurrentQueue.offer(4);
        System.out.println(concurrentQueue.toString());
        concurrentQueue.offer(5);
        System.out.println(concurrentQueue.toString());
        int thirdPeekCount = concurrentQueue.peek();
        System.out.println(concurrentQueue.toString() + " ----------> count = "+ thirdPeekCount);
        int fourthPeekCount = concurrentQueue.peek();
        System.out.println(concurrentQueue.toString() + " ----------> count = "+ fourthPeekCount);
        concurrentQueue.clear();
        System.out.println(concurrentQueue.toString());

        System.out.println("--------------------------------------------------------------------");

        MostRecentlyInsertedBlockingQueue<Integer> blockingQueue = new MostRecentlyInsertedBlockingQueue<Integer>(3);
        System.out.println(blockingQueue.toString());
        blockingQueue.offer(1);
        System.out.println(blockingQueue.toString());
        blockingQueue.offer(2);
        System.out.println(blockingQueue.toString());
        blockingQueue.offer(3);
        System.out.println(blockingQueue.toString());
        blockingQueue.offer(4);
        System.out.println(blockingQueue.toString());
        blockingQueue.offer(5);
        System.out.println(blockingQueue.toString());
        try {
            blockingQueue.offer(6,3,SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(blockingQueue.toString());
        try {
            int firstElementForRemoving = blockingQueue.poll(5,SECONDS);
            System.out.println("The element removing with timeout is " + firstElementForRemoving);
            System.out.println(blockingQueue.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        blockingQueue.offer(7);
        System.out.println(blockingQueue.toString());
        blockingQueue.offer(8);
        System.out.println(blockingQueue.toString());
        try {
            int elementForTaking = blockingQueue.take();
            System.out.println("The element taking and removing at the same time is " + elementForTaking);
            System.out.println(blockingQueue.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int fifthPeekCount = blockingQueue.peek();
        System.out.println(blockingQueue.toString() + " ----------> count = "+ fifthPeekCount );
        int sixthPeekCount = blockingQueue.peek();
        System.out.println(blockingQueue.toString() + " ----------> count = "+ sixthPeekCount);
        blockingQueue.clear();
        System.out.println(blockingQueue.toString());
    }
}
