Implementation MostRecentlyInsertedQueue based on java.util.Queue with special properties: 
the queue (bounded in size) always accept new elements at the tail. If queue is already full - 
the oldest element (head) will be removing and than new element is added.
There is also thread-safe version of MostRecentlyInsertedQueue - ConcurrentMostRecentlyInsertedQueue.
