package blockingqueue;

import simplequeue.MostRecentlyInsertedQueue;

import java.io.Serializable;
import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Admin on 18.09.2016.
 */
public class MostRecentlyInsertedBlockingQueue<E> extends AbstractQueue<E> implements BlockingQueue<E>, Serializable {

    private static final long serialVersionUID = 1L;

    private volatile int capacity;
    private AtomicInteger countOfNodes;
    private Node<E> head;
    private Node<E> tail;

    private class Node<E> {
        E element;
        Node<E> next;

        public Node() {
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
        head = null;
        tail = null;
    }

    public Iterator<E> iterator() {
        return null;
    }

    public int size() {
        return 0;
    }

    public void put(E e) throws InterruptedException {

    }

    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    public E take() throws InterruptedException {
        return null;
    }

    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    public int remainingCapacity() {
        return 0;
    }

    public int drainTo(Collection<? super E> c) {
        return 0;
    }

    public int drainTo(Collection<? super E> c, int maxElements) {
        return 0;
    }

    public boolean offer(E element) {
        if(countOfNodes.intValue()<capacity && element != null){
            try {
                addNodeToTail(element);
                notifyAll();
            } catch (InterruptedException e) {
                System.out.println("Exception in offer(E element) -----> "+e.getMessage());
            }
            countOfNodes.incrementAndGet();
            return true;
        }
        else if (countOfNodes.intValue()>=capacity && element != null){
            poll();
            try {
                addNodeToTail(element);
                notifyAll();
            } catch (InterruptedException e) {
                System.out.println("Exception in offer(E element) -----> "+e.getMessage());
            }
            countOfNodes.incrementAndGet();
            return true;
        }
        else if (element == null){
            throw new NullPointerException("The offered element is null");
        }
        return false;
    }

    private void addNodeToTail(E element) throws InterruptedException {
        Node currentNode = tail;
        tail = new Node();
        tail.element = element;
        if (isEmpty()) {
            head = tail;
        }
        else {
            currentNode.next = tail;
        }
    }

    public E poll() {
        if (isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Exception in offer(E element) -----> "+e.getMessage());
            }
        }
        E elementForRemoving = (E) head.element;
        head = head.next;
        countOfNodes.decrementAndGet();
        return elementForRemoving;
    }

    public E peek() {
        return null;
    }
}
