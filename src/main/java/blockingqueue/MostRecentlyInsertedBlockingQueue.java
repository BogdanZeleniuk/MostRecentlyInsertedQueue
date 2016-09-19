package blockingqueue;

import concurrentqueue.ConcurrentMostRecentlyInsertedQueue;

import java.io.Serializable;
import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MostRecentlyInsertedBlockingQueue<E> extends AbstractQueue<E> implements BlockingQueue<E>, Serializable {

    private static final long serialVersionUID = 1L;

    private volatile int capacity;
    private AtomicInteger countOfNodes;
    private Node<E> head;
    private Node<E> tail;

    private final Lock lock = new ReentrantLock();

    private static class Node<E> {
        volatile E element;
        volatile Node<E> next;

        public Node() {
        }

        public Node(E element) {
            this.element = element;
        }

        public Node(E element, Node<E> next) {
            this.element = element;
            this.next = next;
        }

        public E getElement() {
            return element;
        }

        public void setElement(E element) {
            this.element = element;
        }

        public Node<E> getNext() {
            return next;
        }

        public void setNext(Node<E> next) {
            this.next = next;
        }
    }

    public MostRecentlyInsertedBlockingQueue(int capacity) {
        this.capacity = capacity;
        countOfNodes = new AtomicInteger(0);
        tail = head = new Node<E>(null);
    }

    public int size() {
        return countOfNodes.get();
    }

    public void put(E element) throws InterruptedException {
        if (element == null)
            throw new NullPointerException("You can not to put the empty element into the queue.");
        lock.lockInterruptibly();
        try {
            addNodeToTail(element);
        }
        finally {
            lock.unlock();
        }

    }

    public boolean offer(E element, long timeout, TimeUnit unit) throws InterruptedException {
        if (element == null)
            throw new NullPointerException("You can not to offer an empty element!");
        long nanos = unit.toNanos(timeout);
        lock.lockInterruptibly();
        if(countOfNodes.intValue()<capacity){
            try {
                addNodeToTail(element);
                countOfNodes.incrementAndGet();
            }
            finally {
                lock.unlock();
            }
            return true;
        }
        else if (countOfNodes.intValue()>=capacity){
            try {
                if (nanos<=0)
                    return false;
                poll();
                addNodeToTail(element);
                countOfNodes.incrementAndGet();
                lock.newCondition().awaitNanos(nanos);
            } catch (InterruptedException e) {
                System.out.println("Exception in offer(E element) -----> "+e.getMessage());
            }
            finally {
                lock.unlock();
            }
            return true;
        }
        return false;
    }

    public E take() throws InterruptedException {
        E elementForTaking;
        lock.lockInterruptibly();
        try {
            while (countOfNodes.intValue() == 0){
                lock.newCondition().await();
            }
            elementForTaking = head.next.element;
            head = head.getNext();
            countOfNodes.decrementAndGet();
            return elementForTaking;
        }
        finally {
            lock.unlock();
        }
    }

    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        if (countOfNodes.get() == 0)
            return null;
        E elementForRemoving;
        long nanos = unit.toNanos(timeout);
        lock.lockInterruptibly();
        try {
            if (nanos<=0)
                return null;
            elementForRemoving = head.next.element;
            head = head.next;
            countOfNodes.decrementAndGet();
            lock.newCondition().awaitNanos(nanos);
            return elementForRemoving;
        }
        finally {
            lock.unlock();
        }
    }

    public int remainingCapacity() {
        return capacity - countOfNodes.intValue();
    }

    public int drainTo(Collection<? super E> collection) {
        return drainTo(collection, size());
    }

    public int drainTo(Collection<? super E> collection, int maxElements) {
        if (collection.size() == 0)
            throw new NullPointerException("You can remove and add an empty collection.");
        lock.lock();
        try {
            int numberOfElements = (capacity>maxElements) ? maxElements : capacity;
            while (collection.size() != numberOfElements-1){
                Node<E> currentNode = head;
                head = new Node();
                collection.add(head.element);
                currentNode.next = head;
            }
            return numberOfElements;
        }
        finally {
            lock.unlock();
        }
    }

    public boolean offer(E element) {
        if (element == null)
            throw new NullPointerException("You can not to offer an empty element!");
        lock.lock();
        if(countOfNodes.intValue()<capacity){
            try {
                addNodeToTail(element);
                countOfNodes.incrementAndGet();
            } catch (InterruptedException e) {
                System.out.println("Exception in offer(E element) -----> "+e.getMessage());
            }
            finally {
                lock.unlock();
            }
            return true;
        }
        else if (countOfNodes.intValue()>=capacity){
            try {
                poll();
                addNodeToTail(element);
                countOfNodes.incrementAndGet();
            } catch (InterruptedException e) {
                System.out.println("Exception in offer(E element) -----> "+e.getMessage());
            }
            finally {
                lock.unlock();
            }
            return true;
        }
        return false;
    }

    private synchronized void addNodeToTail(E element) throws InterruptedException {
        Node<E> currentNode = tail;
        tail = new Node();
        tail.element = element;
        currentNode.next = tail;
    }

    public E poll() {
        if (countOfNodes.get() == 0)
            return null;
        E elementForRemoving;
        lock.lock();
        try {
            elementForRemoving = head.element;
            head = head.next;
            countOfNodes.decrementAndGet();
            return elementForRemoving;
        } finally {
            lock.unlock();
        }
    }

    public E peek() {
        if (countOfNodes.get() == 0)
            return null;
        E elementForGetting;
        lock.lock();
        try {
            elementForGetting = head.next.element;
            head = head.next;
            countOfNodes.decrementAndGet();
            if (elementForGetting == null)
                return null;
            else
                return elementForGetting;
        }
        finally {
            lock.unlock();
        }
    }

    public Iterator<E> iterator() {
        return new MostRecentlyInsertedBlockingIterator();
    }
    private class MostRecentlyInsertedBlockingIterator implements Iterator<E> {

        private Node<E> currentNode;
        private Node<E> lastNode;
        private E currentElement;

        public MostRecentlyInsertedBlockingIterator() {
            lock.lock();
            try {
                if (currentNode != null)
                    currentNode = head.getNext();
                currentElement = currentNode.element;
            }
            finally {
                lock.unlock();
            }
        }

        public boolean hasNext() {
            return currentNode!=null;
        }

        public E next() {
            return null;
        }

        public void remove() {

        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node tmp = head.getNext();
        sb.append("[");
        while (tmp != null) {
            if (tmp == tail)
                sb.append(tmp.getElement()).append("");
            else
                sb.append(tmp.getElement()).append(", ");
            tmp = tmp.getNext();
        }
        sb.append("]");
        return sb.toString();
    }
}
