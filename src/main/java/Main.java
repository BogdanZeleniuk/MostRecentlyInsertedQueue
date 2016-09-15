import concurrentqueue.ConcurrentMostRecentlyInsertedQueue;
import simplequeue.MostRecentlyInsertedQueue;

/**
 * Created by Admin on 15.09.2016.
 */
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
    }
}
